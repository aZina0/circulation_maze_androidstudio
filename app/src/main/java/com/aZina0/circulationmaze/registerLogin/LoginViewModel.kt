package com.aZina0.circulationmaze.registerLogin

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.aZina0.circulationmaze.AccountManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val accountManager: AccountManager,
) : ViewModel() {
    var usernameOrEmail by mutableStateOf("")
        private set
    var usernameOrEmailError by mutableStateOf(false)
        private set
    var usernameOrEmailErrorText by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set
    var passwordError by mutableStateOf(false)
        private set
    var passwordErrorText by mutableStateOf("")
        private set

    var generalErrorText by mutableStateOf("")

    var loadingBarActive by mutableStateOf(false)

    fun onUsernameOrEmailChange(newValue: String) {
        usernameOrEmail = newValue
        usernameOrEmailError = false
        usernameOrEmailErrorText = ""
        generalErrorText = ""
    }

    fun onPasswordChange(newValue: String) {
        password = newValue
        passwordError = false
        passwordErrorText = ""
        generalErrorText = ""
    }

    fun onLoginClick(
        onSuccessfulLogin: () -> Unit,
    ) {
        loadingBarActive = true

        accountManager.attemptLogin(
            usernameOrEmail = usernameOrEmail,
            password = password,
            onSuccess = {
                onSuccessfulLogin()
                loadingBarActive = false
            },
            onFailure = {
                generalErrorText = "Could not authenticate."
                loadingBarActive = false
            },
            onUsernameFailure = { message ->
                usernameOrEmailError = true
                usernameOrEmailErrorText = message
                loadingBarActive = false
            },
        )
    }
}