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
fun RegisterScreen(
    onSwapToLoginClick: () -> Unit,
    onReturnClicked: () -> Unit,
    onSuccessfulRegister: () -> Unit,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    Column (
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomHeader(
            viewModel.progressBarActive,
            middleComposable = {
                Text(
                    text = "Register",
                    fontSize = Global.relativeFont(0.04f),
                    textAlign = TextAlign.Center
                )
            },
        )
        RelativeVerticalSpacer(
            percent = 0.15f,
        )
        OutlinedTextField(
            value = viewModel.username,
            onValueChange = { viewModel.onUsernameChange(it) },
            label = { Text(text = "Username") },
            modifier = Modifier.width(Global.relativeWidth(.7f)),
            singleLine = true,
            isError = viewModel.usernameError,
            supportingText = { Text(text = viewModel.usernameErrorText) },
            enabled = viewModel.registerEnabled,
        )
        RelativeVerticalSpacer(
            percent = 0.005f,
        )
        OutlinedTextField(
            value = viewModel.email,
            onValueChange = { viewModel.onEmailChange(it) },
            label = { Text(text = "Email") },
            modifier = Modifier.width(Global.relativeWidth(.7f)),
            singleLine = true,
            isError = viewModel.emailError,
            supportingText = { Text(text = viewModel.emailErrorText) },
            enabled = viewModel.registerEnabled,
        )
        RelativeVerticalSpacer(
            percent = 0.005f,
        )
        OutlinedTextField(
            value = viewModel.password,
            onValueChange = { viewModel.onPasswordChange(it) },
            label = { Text(text = "Password") },
            modifier = Modifier.width(Global.relativeWidth(.7f)),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            enabled = viewModel.registerEnabled,
        )
        OutlinedTextField(
            value = viewModel.passwordAgain,
            onValueChange = { viewModel.onPasswordAgainChange(it) },
            label = { Text(text = "Repeat password") },
            modifier = Modifier.width(Global.relativeWidth(.7f)),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            isError = viewModel.passwordError,
            supportingText = { Text(text = viewModel.passwordErrorText) },
            enabled = viewModel.registerEnabled,
        )
        Text (
            modifier = Modifier.fillMaxWidth(),
            text = viewModel.generalErrorText,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center,
            fontSize = Global.relativeFont(.018f)
        )

        RelativeVerticalSpacer(
            percent = 0.075f,
        )

        Column(
            modifier = Modifier
                .width(Global.relativeWidth(.7f))
        ) {
            Text(
                text = "Already have an account?"
            )
            Text(
                text = "Log in.",
                modifier = Modifier
                    .clickable { onSwapToLoginClick() },
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
                onClick = { viewModel.onRegisterClick(
                    onSuccessfulRegister = {
                        onSuccessfulRegister()
                    }
                ) },
                modifier = Modifier
                    .width(Global.relativeWidth(.37f))
                    .height(Global.relativeHeight(.07f)),
                shape = RoundedCornerShape(percent = 30),
                enabled = viewModel.registerEnabled,
            ) {
                Text (
                    text = "Register",
                    fontSize = Global.relativeFont(0.03f),
                )
            }
        }
    }
}