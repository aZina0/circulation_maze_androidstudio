package com.aZina0.circulationmaze.loadGame

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
    private val saveManager: SaveManager,
) : ViewModel() {
    var saves = mutableListOf<SaveInfo>()
    var redrawIndicator by mutableStateOf(false)
    var openDeleteDialog by mutableStateOf(false)
    private var uidToDelete: String? = null

    init {
//        saveManager.deleteAllSaves()
        updateSaveList()
    }

    private fun updateSaveList() {
        saves.clear()

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

        triggerRedraw()
    }

    fun deleteSaveClicked(uid: String) {
        uidToDelete = uid
        openDeleteDialog = true
    }

    fun deleteSave() {
        if (uidToDelete != null) {
            saveManager.deleteSaveGame(uidToDelete!!)
        }
        closeDialog()
        updateSaveList()
    }

    fun closeDialog() {
        openDeleteDialog = false
    }

    private fun triggerRedraw() {
        redrawIndicator = !redrawIndicator
    }
}