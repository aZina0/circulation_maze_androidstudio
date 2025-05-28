package com.aZina0.circulationmaze.newGame

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.aZina0.circulationmaze.game.Game
import kotlin.random.Random


class NewGameViewModel : ViewModel() {
    var gridSize by mutableStateOf("")
        private set
    var gridSizeInteger = 0
        private set
    var gridError = false
        private set
    var gridErrorText = ""
        private set

    var seed by mutableStateOf("")
        private set
    var seedLong: Long = 0
        private set
    var seedError = false
        private set
    var seedErrorText = ""
        private set

    var startGameEnabled = true
        private set

    init {
        gridSizeInteger = 13
        gridSize = gridSizeInteger.toString()

        seedLong = Random.Default.nextLong(from = 0L, until = Game.SEED_MAX)
        seed = seedLong.toString()
    }

    fun onGridSizeChanged(newValue: String) {
        gridSize = newValue
        val validInput: Boolean
        if (newValue.toIntOrNull() != null) {
            if (newValue.toInt() < Game.GRID_SIZE_MIN || newValue.toInt() > Game.GRID_SIZE_MAX) {
                validInput = false
            } else {
                validInput = true
            }
        } else {
            validInput = false
        }

        if (validInput) {
            gridSizeInteger = newValue.toInt()
            gridError = false
            gridErrorText = ""
        } else {
            gridError = true
            gridErrorText = "Grid size must be a number between %d and %d.".format(Game.GRID_SIZE_MIN, Game.GRID_SIZE_MAX)
        }
        startGameEnabled = !seedError && !gridError
    }

    fun onSeedChanged(newValue: String) {
        seed = newValue
        val validInput: Boolean
        if (newValue.toLongOrNull() != null) {
            if (newValue.toLong() < 0 || newValue.toLong() > Game.SEED_MAX) {
                validInput = false
            } else {
                validInput = true
            }
        } else {
            validInput = false
        }

        if (validInput) {
            seedLong = newValue.toLong()
            seedError = false
            seedErrorText = ""
        } else {
            seedError = true
            seedErrorText = "Seed must be a number between 0 and %d.".format(Game.SEED_MAX)
        }
        startGameEnabled = !seedError && !gridError
    }
}