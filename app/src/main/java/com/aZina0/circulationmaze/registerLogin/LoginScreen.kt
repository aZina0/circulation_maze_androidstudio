package com.aZina0.circulationmaze.registerLogin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.hilt.navigation.compose.hiltViewModel
import com.aZina0.circulationmaze.CustomHeader
import com.aZina0.circulationmaze.Global
import com.aZina0.circulationmaze.RelativeHorizontalSpacer
import com.aZina0.circulationmaze.RelativeVerticalSpacer


@Composable
fun LoginScreen(
    onSwapToRegisterClick: () -> Unit,
    onReturnClicked: () -> Unit,
    onSuccessfulLogin: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {

    Column (
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomHeader("Log in", viewModel.loadingBarActive)

        RelativeVerticalSpacer(
            percent = 0.3f,
        )

        OutlinedTextField(
            modifier = Modifier
                .width(Global.relativeWidth(.7f)),
            value = viewModel.usernameOrEmail,
            onValueChange = { viewModel.onUsernameOrEmailChange(it) },
            label = { Text(text = "Username or email") },
            singleLine = true,
            isError = viewModel.usernameOrEmailError,
            supportingText = { Text(text = viewModel.usernameOrEmailErrorText) },
            enabled = viewModel.loginEnabled,
        )
        RelativeVerticalSpacer(
            percent = 0.005f,
        )
        OutlinedTextField(
            modifier = Modifier
                .width(Global.relativeWidth(.7f)),
            value = viewModel.password,
            onValueChange = { viewModel.onPasswordChange(it) },
            label = { Text(text = "Password") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            isError = viewModel.passwordError,
            supportingText = { Text(text = viewModel.passwordErrorText) },
            enabled = viewModel.loginEnabled,
        )
        Text (
            modifier = Modifier.fillMaxWidth(),
            text = viewModel.generalErrorText,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center,
            fontSize = Global.relativeFont(.018f)
        )
        RelativeVerticalSpacer(
            percent = 0.1f,
        )
        Column(
            modifier = Modifier
                .width(Global.relativeWidth(.7f))
        ) {
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
        }

        RelativeVerticalSpacer(
            percent = 0.075f,
        )

        Row {
            Button(
                onClick = { onReturnClicked() },
                modifier = Modifier
                    .width(Global.relativeWidth(.33f))
                    .height(Global.relativeHeight(.07f)),
                shape = RoundedCornerShape(percent = 30),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                )
            ) {
                Text (
                    text = "Return",
                    fontSize = Global.relativeFont(0.03f),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        textDecoration = TextDecoration.Underline,
                    ),
                )
            }
            RelativeHorizontalSpacer(.05f)
            Button(
                onClick = { viewModel.onLoginClick(
                    onSuccessfulLogin = {
                        onSuccessfulLogin()
                    }
                ) },
                modifier = Modifier
                    .width(Global.relativeWidth(.37f))
                    .height(Global.relativeHeight(.07f)),
                shape = RoundedCornerShape(percent = 30),
                enabled = viewModel.loginEnabled,
            ) {
                Text (
                    text = "Log in",
                    fontSize = Global.relativeFont(0.03f),
                )
            }
        }
    }
}