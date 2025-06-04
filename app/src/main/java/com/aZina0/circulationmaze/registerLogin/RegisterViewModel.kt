package com.aZina0.circulationmaze.registerLogin

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.aZina0.circulationmaze.AccountManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val accountManager: AccountManager,
) : ViewModel() {
    var username by mutableStateOf("")
        private set
    var usernameError by mutableStateOf(false)
        private set
    var usernameErrorText by mutableStateOf("")
        private set

    var email by mutableStateOf("")
        private set
    var emailError by mutableStateOf(false)
        private set
    var emailErrorText by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var passwordAgain by mutableStateOf("")
        private set
    var passwordError by mutableStateOf(false)
        private set
    var passwordErrorText by mutableStateOf("")
        private set

    var generalErrorText by mutableStateOf("")
        private set
    var progressBarActive by mutableStateOf(false)
        private set
    var registerEnabled by mutableStateOf(false)
        private set

    init {
        if (accountManager.isOnline()) {
            registerEnabled = true
        } else {
            generalErrorText = "No internet connection."
        }
    }

    fun onUsernameChange(newValue: String) {
        username = newValue
        usernameError = false
        usernameErrorText = ""
    }

    fun onEmailChange(newValue: String) {
        email = newValue
        emailError = false
        emailErrorText = ""
    }

    fun onPasswordChange(newValue: String) {
        password = newValue
        passwordError = false
        passwordErrorText = ""
    }

    fun onPasswordAgainChange(newValue: String) {
        passwordAgain = newValue
        passwordError = false
        passwordErrorText = ""
    }

    fun onRegisterClick(
        onSuccessfulRegister: () -> Unit,
    ) {
        if (password != passwordAgain) {
            passwordError = true
            passwordErrorText = "Passwords must match."
            return
        }
        if (password.length < 8) {
            passwordError = true
            passwordErrorText = "Passwords must be longer than 7 characters."
            return
        }
        if (!password.contains(Regex("\\d"))) {
            passwordError = true
            passwordErrorText = "Passwords must contain at least one digit."
            return
        }
        if (username.length <= 2) {
            usernameError = true
            usernameErrorText = "Username must be longer than 2 characters."
            return
        }

        progressBarActive = true

        accountManager.attemptRegister(
            username = username,
            email = email,
            password = password,
            onSuccess = {
                onSuccessfulRegister()
                progressBarActive = false
            },
            onUsernameFail = {
                usernameError = true
                usernameErrorText = "Username error."
                progressBarActive = false
            },
            onAccountExistsFail = {
                emailError = true
                emailErrorText = "Account already exists."
                progressBarActive = false
            },
        )

    }
}