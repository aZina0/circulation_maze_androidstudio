package com.aZina0.circulationmaze.loadGame

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.aZina0.circulationmaze.SaveInfo
import com.aZina0.circulationmaze.SaveManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject



@HiltViewModel
class LoadGameViewModel @Inject constructor(
    private val saveManager: SaveManager,
) : ViewModel() {
    var saves = mutableListOf<SaveInfo>()
    var redrawIndicator by mutableStateOf(false)
    var openDeleteDialog by mutableStateOf(false)
    var loadingBar by mutableStateOf(false)
    private var uidToDelete: String? = null

    init {
//        saveManager.deleteAllSaves()
        updateSaveList()
    }

    private fun updateSaveList() {
        loadingBar = true
        saves.clear()

//        val filesList = saveManager.getSavesList()
//        for (file in filesList) {
//            val gameData = saveManager.loadGameFromFile(file)!!
//            val imageBitmap = saveManager.loadGameImageFromFile(gameData)
//            saves.add(SaveInfo(gameData, imageBitmap))
//        }

        saveManager.syncSavesAndReturn(
            onFinished = { resultSaves ->
                saves = resultSaves.toMutableList()
                saves.sortByDescending {
                    it.gameData.lastModifiedDate
                }
                loadingBar = false
                triggerRedraw()
            }
        )
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