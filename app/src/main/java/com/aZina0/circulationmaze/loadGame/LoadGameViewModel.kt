package com.aZina0.circulationmaze.loadGame

import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import com.aZina0.circulationmaze.GameBasicInfo
import com.aZina0.circulationmaze.SaveManager
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject


data class SaveInfo(
    val basic: GameBasicInfo,
    val imageBitmap: ImageBitmap?,
)

@HiltViewModel
class LoadGameViewModel @Inject constructor(
    saveManager: SaveManager,
) : ViewModel() {
    var saves = mutableListOf<SaveInfo>()

    init {
//        saveManager.deleteAllSaves()

        val filesList = saveManager.getSavesList()
        for (file in filesList) {
            val jsonObject = saveManager.readJsonObjectFromFile(file)!!
            val basicInfo = saveManager.getBasicInfo(jsonObject)
            val imageBitmap = saveManager.loadGameImageFromFile(basicInfo)
            saves.add(SaveInfo(basicInfo, imageBitmap))
        }

        saves.sortByDescending {
            LocalDateTime.parse(it.basic.lastModifiedDate, DateTimeFormatter.ISO_DATE_TIME)
        }
    }
}