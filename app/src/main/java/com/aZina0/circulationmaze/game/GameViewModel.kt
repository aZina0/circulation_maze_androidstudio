package com.aZina0.circulationmaze.game

import androidx.compose.runtime.getValue
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

    init {
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
            Game.importGameData(gameData)
        }

        Game.triggerRedraw = !Game.triggerRedraw

//        startGenerationTest()
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
        val gameData = Game.getGameData()

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
            onSuccess = {},
            onFailure = {}
        )
    }

    fun onMenuClicked() {

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
        saveManager.saveGame(gameData)

        Highlight.hide()
        Game.triggerRedraw = !Game.triggerRedraw
        viewModelScope.launch {
            val imageBitmap = graphicsLayer!!.toImageBitmap()
            saveManager.saveGameImage(gameData, imageBitmap)
        }
    }
}