package com.aZina0.circulationmaze.registerLogin

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

data class RegisterUiState(
    var username: String = "",
    var email: String = "",
    var password: String = "",
    var passwordAgain: String = "",
)

class RegisterViewModel : ViewModel() {
    var uiState = mutableStateOf(RegisterUiState())
        private set

    fun onUsernameChange(newValue: String) {
        uiState.value = uiState.value.copy(username = newValue)
    }

    fun onEmailChange(newValue: String) {
        uiState.value = uiState.value.copy(email = newValue)
    }

    fun onPasswordChange(newValue: String) {
        uiState.value = uiState.value.copy(password = newValue)
    }

    fun onPasswordAgainChange(newValue: String) {
        uiState.value = uiState.value.copy(passwordAgain = newValue)
    }

    fun onRegisterClick() {

    }
}