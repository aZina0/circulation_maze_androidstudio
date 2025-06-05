package com.aZina0.circulationmaze.editProfile

import android.app.Application
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.aZina0.circulationmaze.AccountManager
import com.aZina0.circulationmaze.Global
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val accountManager: AccountManager,
    savedStateHandle: SavedStateHandle,
    private val application: Application
): ViewModel() {
    var image by mutableStateOf(accountManager.defaultProfileImage)
        private set
    var loadedImage by mutableStateOf<ImageBitmap?>(null)
        private set
    var username by mutableStateOf("")
        private set
    var userUid by mutableStateOf("")
        private set
    var loadingBarActive by mutableStateOf(false)
        private set


    init {
        userUid = savedStateHandle["userUid"] ?: ""

        loadingBarActive = true

        var requestsSent = 0
        var responsesReceived = 0
        val checkForAllResponses: () -> Unit = {
            if (responsesReceived >= requestsSent) {
                loadingBarActive = false
            }
        }

        requestsSent++
        accountManager.getImage(
            onSuccess = {
                image = it
                responsesReceived++
                checkForAllResponses()
            },
            onFailure = {
                responsesReceived++
                checkForAllResponses()
            }
        )

        requestsSent++
        accountManager.getUsername(
            userUid = userUid,
            onSuccess = {
                username = it
                responsesReceived++
                checkForAllResponses()
            },
            onFailure = {
                responsesReceived++
                checkForAllResponses()
            },
        )
    }

    fun onOpenFiles() {

    }

    fun imageLoaded(imageUri: Uri) {
        val inputStream = application.applicationContext.contentResolver.openInputStream(imageUri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        loadedImage = Global.getCroppedScaledImageBitmap(
            bitmap = bitmap,
            targetSize = 300,
        )
    }

    fun onChangeImageClicked() {
        accountManager.updateImage(
            loadedImage!!,
            onSuccess = {
                accountManager.getImage(
                    onSuccess = {
                        image = it
                    },
                    onFailure = {

                    }
                )
            },
            onFailure = {

            }
        )
    }
}