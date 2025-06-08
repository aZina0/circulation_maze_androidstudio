package com.aZina0.circulationmaze.game

import android.os.SystemClock
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aZina0.circulationmaze.AccountManager
import com.aZina0.circulationmaze.BoardManager
import com.aZina0.circulationmaze.Global
import com.aZina0.circulationmaze.SaveManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val saveManager: SaveManager,
    private val accountManager: AccountManager,
    private val boardManager: BoardManager,
) : ViewModel() {
    var graphicsLayer: GraphicsLayer? = null
    var boardSolved by mutableStateOf(false)

    var shared by mutableStateOf(false)

    var pauseButtonVisible by mutableStateOf(true)

    var milliSeconds by mutableLongStateOf(0L)
    var timerRunning by mutableStateOf(false)

    init {
        milliSeconds = 0L
        val startType = savedStateHandle["startType"] ?: ""
        val uid = savedStateHandle["uid"] ?: ""
        val seed = savedStateHandle["seed"] ?: 0L
        val gridSize = savedStateHandle["gridSize"] ?: 0

        Game.solveCallback = { onBoardSolve() }

        if (startType == "newGame") {
            Game.createNewGame(
                uid,
                seed,
                gridSize,
            )
        } else if (startType == "loadGame") {
            val gameData = saveManager.loadGameFromFile(uid)!!
            milliSeconds = gameData.time
            Game.importGameData(gameData)
        }

        Game.triggerRedraw = !Game.triggerRedraw

        startTimer()
    }

    fun startTimer() {
        if (timerRunning) {
            return
        }

        timerRunning = true
        val startTime = SystemClock.elapsedRealtime() - milliSeconds
        viewModelScope.launch {
            while (timerRunning) {
                milliSeconds = SystemClock.elapsedRealtime() - startTime
                delay(10L)
            }
        }
    }

    fun pauseTimer() {
        timerRunning = false
    }

    fun resetTimer() {
        timerRunning = false
        milliSeconds = 0L
    }

    fun onPauseClicked() {
        if (timerRunning) {
            pauseTimer()
        } else {
            startTimer()
        }
    }

    fun startGenerationTest() {
        var seed = 0
        val size = 13
        viewModelScope.launch {
            while (true) {
                Global.print("Creating game with seed: %d, size %d".format(seed, size))
                Game.createNewGame(UUID.randomUUID().toString(), seed.toLong(), size)
                Game.triggerRedraw = !Game.triggerRedraw
                if (!Game.checkForBoardSolve()) {
                    Global.print("FAILED GENERATION")
                    break
                }
                seed++
                delay(4000)
            }
        }
    }

    fun onBoardSolve() {
        boardSolved = true
        pauseTimer()
        val gameData = Game.getGameData()
        gameData.time = milliSeconds

        Highlight.hide()
        Game.triggerRedraw = !Game.triggerRedraw
        viewModelScope.launch {
            val imageBitmap = graphicsLayer!!.toImageBitmap()
            boardManager.saveBoard(
                gameData = gameData,
                imageBitmap = imageBitmap,
                onSuccess = {
                    saveManager.deleteSaveGameFromFile(gameData.uid)
                    saveManager.deleteSaveGameFromCloud(
                        gameData.uid,
                        onSuccess = {},
                        onFailure = {}
                    )
                },
                onFailure = {}
            )
        }
    }

    fun onShareSolveClicked() {
        boardManager.shareBoard(
            boardUid = Game.uid,
            accountManager = accountManager,
            onSuccess = {
                shared = true
            },
            onFailure = {}
        )
    }

    fun onLockClicked() {
        val piece = Game.pieces[Highlight.coordinate]!!
        if (piece.locked) {
            piece.unlock()
        } else {
            piece.lock()
        }
    }

    fun onRotateCCW90Clicked() {
        Game.pieces[Highlight.coordinate]!!.rotateByCCW90()
    }

    fun onRotateCW90Clicked() {
        Game.pieces[Highlight.coordinate]!!.rotateByCW90()
    }

    fun onRotate180Clicked() {
        Game.pieces[Highlight.coordinate]!!.rotateBy180()
    }

    fun exitGameScreen() {
        if (boardSolved) {
            return
        }

        val gameData = Game.getGameData()
        gameData.time = milliSeconds
        saveManager.saveGame(gameData)

        Highlight.hide()
        Game.triggerRedraw = !Game.triggerRedraw
        viewModelScope.launch {
            val imageBitmap = graphicsLayer!!.toImageBitmap()
            saveManager.saveGameImage(gameData, imageBitmap)
        }
    }
}