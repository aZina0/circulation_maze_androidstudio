package com.aZina0.circulationmaze.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aZina0.circulationmaze.Global
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GameViewModel : ViewModel() {

    init {
        startGenerationTest()
    }

    fun startGenerationTest() {
        var seed = 0
        var size = 13
        viewModelScope.launch {
            while (true) {
                Global.print("Creating game with seed: %d, size %d".format(seed, size))
                Game.createNewGame(seed.toLong(), size, size)
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
}