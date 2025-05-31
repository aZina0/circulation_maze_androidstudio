package com.aZina0.circulationmaze.game

import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
) : ViewModel() {
    var graphicsLayer: GraphicsLayer? = null

    init {
        val startType = savedStateHandle["startType"] ?: ""
        val uid = savedStateHandle["uid"] ?: ""
        val seed = savedStateHandle["seed"] ?: 0L
        val gridSize = savedStateHandle["gridSize"] ?: 0

        if (startType == "newGame") {
            Game.createNewGame(
                uid,
                seed,
                gridSize,
            )
        } else if (startType == "loadGame") {
            val jsonObject = saveManager.readJsonObjectFromFile(uid)
            val basicInfo = saveManager.getBasicInfo(jsonObject!!)
            Game.importGame(basicInfo, jsonObject)
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
                if (!Game.checkForBoardComplete()) {
                    Global.print("FAILED GENERATION")
                    break
                }
                seed++
                delay(4000)
            }
        }
    }

    fun exitGameScreen() {
        val result = Game.exportGame()
        val basicInfo = result.first
        val jsonObject = result.second

        saveManager.saveGame(basicInfo, jsonObject.toString())

        Highlight.hide()
        Game.triggerRedraw = !Game.triggerRedraw
        viewModelScope.launch {
            val imageBitmap = graphicsLayer!!.toImageBitmap()
            saveManager.saveGameImage(basicInfo, imageBitmap)
        }
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
}