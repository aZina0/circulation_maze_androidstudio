package com.aZina0.circulationmaze.game

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aZina0.circulationmaze.FileManager
import com.aZina0.circulationmaze.Global
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val fileManager: FileManager
) : ViewModel() {

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
            val jsonString = fileManager.readGameFromFile(uid)
            Game.importGameFromJson(jsonString)
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

    fun loadGameFromFile(uid: String) {
        val jsonString = fileManager.readGameFromFile(uid)
        Game.importGameFromJson(jsonString)
    }

    fun exitGameScreen() {
        val result = Game.currentGameToJson()
        val uid = result.first
        val jsonString = result.second
        fileManager.saveGameToFile(uid, jsonString)
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