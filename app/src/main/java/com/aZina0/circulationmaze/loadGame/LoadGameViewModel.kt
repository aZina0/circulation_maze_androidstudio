package com.aZina0.circulationmaze.loadGame

import androidx.lifecycle.ViewModel
import com.aZina0.circulationmaze.FileManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoadGameViewModel @Inject constructor(
    private val fileManager: FileManager
) : ViewModel() {
    var filesList = emptyList<String>()

    init {
        filesList = fileManager.getSavesList()
    }
}