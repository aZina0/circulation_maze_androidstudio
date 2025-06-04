package com.aZina0.circulationmaze.registerLogin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aZina0.circulationmaze.CustomHeader
import com.aZina0.circulationmaze.Global
import com.aZina0.circulationmaze.Global.relativeWidth
import com.aZina0.circulationmaze.RelativeVerticalSpacer


@Composable
fun LoginScreen(
    onSwapToRegisterClick: () -> Unit,
    onSuccessfulLogin: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {

    Box (
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column (
            modifier = Modifier
                .width(relativeWidth(.7f))
                .fillMaxHeight(),
        ) {
            CustomHeader("Log in", viewModel.loadingBarActive)

            RelativeVerticalSpacer(
                percent = 0.3f,
            )

            OutlinedTextField(
                value = viewModel.usernameOrEmail,
                onValueChange = { viewModel.onUsernameOrEmailChange(it) },
                label = { Text(text = "Username or email") },
                singleLine = true,
                isError = viewModel.usernameOrEmailError,
                supportingText = { Text(text = viewModel.usernameOrEmailErrorText) },
            )
            RelativeVerticalSpacer(
                percent = 0.005f,
            )
            OutlinedTextField(
                value = viewModel.password,
                onValueChange = { viewModel.onPasswordChange(it) },
                label = { Text(text = "Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                isError = viewModel.passwordError,
                supportingText = { Text(text = viewModel.passwordErrorText) },
            )
            RelativeVerticalSpacer(
                percent = 0.1f,
            )
            Text(
                text = "Don't have an account?",
            )
            Text(
                text = "Register.",
                modifier = Modifier
                    .clickable { onSwapToRegisterClick() },
                style = MaterialTheme.typography.bodyLarge.copy(
                    textDecoration = TextDecoration.Underline,
                ),
            )
            RelativeVerticalSpacer(
                percent = 0.05f,
            )

            Text (
                modifier = Modifier.fillMaxWidth(),
                text = viewModel.generalErrorText,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center,
                fontSize = Global.relativeFont(.018f)
            )

            RelativeVerticalSpacer(
                percent = 0.03f,
            )
            Button(
                onClick = { viewModel.onLoginClick(
                    onSuccessfulLogin = {
                        onSuccessfulLogin()
                    }
                ) },
                modifier = Modifier
                    .size(width = 150.dp, height = 60.dp)
                    .align(alignment = Alignment.End),
                shape = RoundedCornerShape(percent = 30),
            ) {
                Text (
                    text = "Log in",
                    fontSize = 24.sp,
                )
            }
        }
    }
}