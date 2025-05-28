package com.aZina0.circulationmaze.newGame

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.aZina0.circulationmaze.Global


class NewGameViewModel : ViewModel() {
    var gridSize by mutableStateOf("")
        private set
    var gridSizeInteger = 0
        private set

    fun onGridSizeChanged(newValue: String) {
        Global.print("YEYJH")
        gridSize = newValue
    }
}