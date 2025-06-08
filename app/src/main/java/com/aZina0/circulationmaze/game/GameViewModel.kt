package com.aZina0.circulationmaze.game

import android.os.SystemClock
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
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

    var xp by mutableFloatStateOf(0f)
    var level by mutableIntStateOf(0)

    init {
        milliSeconds = 0L
        val startType = savedStateHandle["startType"] ?: ""
        val uid = savedStateHandle["uid"] ?: ""
        val seed = savedStateHandle["seed"] ?: 0L
        val gridSize = savedStateHandle["gridSize"] ?: 0

        accountManager.getXp(
            onSuccess = {
                val levelAndRemainder = accountManager.getLevelAndRemainder(it)
                level = levelAndRemainder.first
                xp = levelAndRemainder.second
            },
            onFailure = {

            }
        )


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
        pauseButtonVisible = false
        val gameData = Game.getGameData()
        gameData.time = milliSeconds

        Highlight.hide()

        for (piece in Game.pieces.values) {
            piece.unlock()
        }

        Game.triggerRedraw = !Game.triggerRedraw

        viewModelScope.launch {

            delay(500)

            if (Firebase.auth.currentUser != null) {
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
                        beginAnimation()
                    },
                    onFailure = {}
                )

                accountManager.addXp(
                    Game.gridRows * Game.gridRows,
                    onSuccess = {},
                    onFailure = {}
                )

            } else {
                beginAnimation()
            }
        }
    }

    fun beginAnimation() {

        var currentWave = mutableListOf<Piece>()
        var nextWave = mutableListOf<Piece>()

        for (piece in Game.rootPiece!!.linkedPieces) {
            currentWave.add(piece)
        }

        viewModelScope.launch {
            while (currentWave.isNotEmpty()) {
                var xpGained = currentWave.size
                level += (xpGained / 100)
                var xpLeft = xpGained - (xpGained / 100) * 100

                xp += xpLeft / 100f
                if (xp > 1f) {
                    level++
                    xp -= 1f
                }

                while (currentWave.size > 0) {
                    var piece = currentWave.removeAt(currentWave.lastIndex)

                    piece.makeGolden()
                    piece.triggerRedraw = !piece.triggerRedraw

                    for (linkedPiece in piece.linkedPieces) {
                        if (linkedPiece !in piece.sourcePieces) {
                            nextWave.add(linkedPiece)
                        }
                    }
                }

                currentWave = nextWave.toMutableList()
                nextWave.clear()
                delay((8 * Game.gridRows * Game.gridRows).toLong())
            }
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