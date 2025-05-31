package com.aZina0.circulationmaze.loadGame

import androidx.lifecycle.ViewModel
import com.aZina0.circulationmaze.GameBasicInfo
import com.aZina0.circulationmaze.SaveManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoadGameViewModel @Inject constructor(
    saveManager: SaveManager,
) : ViewModel() {
    var saves = mutableListOf<GameBasicInfo>()

    init {
//        saveManager.deleteAllSaves()

        val filesList = saveManager.getSavesList()
        for (file in filesList) {
            val jsonObject = saveManager.readJsonObjectFromFile(file)!!
            val basicInfo = saveManager.getBasicInfo(jsonObject)
            saves.add(basicInfo)
        }

        saves.sortBy { it.lastModifiedDate }
    }
}