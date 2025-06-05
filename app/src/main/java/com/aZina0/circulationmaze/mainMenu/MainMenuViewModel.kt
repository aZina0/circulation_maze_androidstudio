package com.aZina0.circulationmaze.mainMenu

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.aZina0.circulationmaze.AccountManager
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

    init {
        if (accountManager.isLoggedIn()) {
            userLoggedIn = true
            accountManager.getUsername(
                onSuccess = {
                    username = it
                },
                onFailure = {}
            )
        }

        profileEnable = accountManager.isLoggedIn()
    }
}