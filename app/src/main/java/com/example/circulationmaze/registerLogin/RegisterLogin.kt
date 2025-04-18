package com.example.circulationmaze.registerLogin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun RegisterLogin(modifier: Modifier) {
    var isLoginScreen by rememberSaveable { mutableStateOf(true) }

    if (isLoginScreen) {
        LoginScreen(
            modifier = modifier,
            onRegisterClick = { isLoginScreen = false },
        )
    } else {
        RegisterScreen(
            modifier = modifier,
            onSwapToLogin = { isLoginScreen = true },
        )
    }
}