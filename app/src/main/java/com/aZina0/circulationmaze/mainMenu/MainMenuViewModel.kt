package com.aZina0.circulationmaze.mainMenu

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import com.aZina0.circulationmaze.AccountManager
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainMenuViewModel @Inject constructor(
    private val accountManager: AccountManager,
): ViewModel() {

    var userLoggedIn by mutableStateOf(false)
        private set
    var profileEnable by mutableStateOf(false)
        private set
    var username by mutableStateOf("")
        private set
    var image by mutableStateOf<ImageBitmap?>(null)
        private set

    init {
        if (accountManager.isLoggedIn()) {
            userLoggedIn = true
            accountManager.getAccountInfo(
                userUid = Firebase.auth.currentUser!!.uid,
                onSuccess = {
                    username = it.username
                    image = it.image
                },
                onFailure = {}
            )
        }

        profileEnable = accountManager.isLoggedIn()
    }
}