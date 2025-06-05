package com.aZina0.circulationmaze.loadGame

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.aZina0.circulationmaze.AccountManager
import com.aZina0.circulationmaze.SaveInfo
import com.aZina0.circulationmaze.SaveManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject



@HiltViewModel
class LoadGameViewModel @Inject constructor(
    private val saveManager: SaveManager,
    private val accountManager: AccountManager,
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

        saveManager.syncSavesAndReturn(
            accountManager = accountManager,
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
            saveManager.deleteSaveGameFromFile(uidToDelete!!)

            if (accountManager.isLoggedIn() && accountManager.isOnline()) {
                saveManager.deleteSaveGameFromCloud(
                    uid = uidToDelete!!,
                    onSuccess = {
                        closeDialog()
                        updateSaveList()
                    },
                    onFailure = {
                        closeDialog()
                        updateSaveList()
                    },
                )
            } else {
                closeDialog()
                updateSaveList()
            }
        } else {
            closeDialog()
        }
    }

    fun closeDialog() {
        openDeleteDialog = false
    }

    private fun triggerRedraw() {
        redrawIndicator = !redrawIndicator
    }
}