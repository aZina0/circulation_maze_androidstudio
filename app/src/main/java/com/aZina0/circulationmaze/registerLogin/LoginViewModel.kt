package com.aZina0.circulationmaze.registerLogin

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel


data class LoginUiState(
    val usernameOrEmail: String = "",
    val password: String = ""
)

class LoginViewModel : ViewModel() {
    var uiState = mutableStateOf(LoginUiState())
        private set

    fun onUsernameOrEmailChange(newValue: String) {
        uiState.value = uiState.value.copy(usernameOrEmail = newValue)
    }

    fun onPasswordChange(newValue: String) {
        uiState.value = uiState.value.copy(password = newValue)
    }

    fun onRegisterClick() {

    }

    fun onLoginClick() {

    }
}