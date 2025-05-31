package com.aZina0.circulationmaze.registerLogin

import androidx.compose.foundation.background
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
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aZina0.circulationmaze.Global.relativeFont
import com.aZina0.circulationmaze.Global.relativeHeight
import com.aZina0.circulationmaze.Global.relativeWidth
import com.aZina0.circulationmaze.RelativeVerticalSpacer

@Composable
fun RegisterScreen(
    onSwapToLoginClick: () -> Unit,
    onSuccessfulRegister: () -> Unit,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    Box (
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceContainerLowest),
        contentAlignment = Alignment.Center,
    ) {
        Column (
            modifier = Modifier
                .width(relativeWidth(.7f))
                .fillMaxHeight(),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .size(relativeHeight(0.075f)),
                contentAlignment = Alignment.Center,
            ) {
                Text (
                    text = "Register",
                    fontSize = relativeFont(.04f),
                )
            }
            RelativeVerticalSpacer(
                percent = 0.175f,
            )
            OutlinedTextField(
                value = viewModel.username,
                onValueChange = { viewModel.onUsernameChange(it) },
                label = { Text(text = "Username") },
                modifier = Modifier.width(relativeWidth(.7f)),
                singleLine = true,
                isError = viewModel.usernameError,
                supportingText = { Text(text = viewModel.usernameErrorText) },
            )
            RelativeVerticalSpacer(
                percent = 0.005f,
            )
            OutlinedTextField(
                value = viewModel.email,
                onValueChange = { viewModel.onEmailChange(it) },
                label = { Text(text = "Email") },
                modifier = Modifier.width(relativeWidth(.7f)),
                singleLine = true,
                isError = viewModel.emailError,
                supportingText = { Text(text = viewModel.emailErrorText) },
            )
            RelativeVerticalSpacer(
                percent = 0.005f,
            )
            OutlinedTextField(
                value = viewModel.password,
                onValueChange = { viewModel.onPasswordChange(it) },
                label = { Text(text = "Password") },
                modifier = Modifier.width(relativeWidth(.7f)),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
            )
            OutlinedTextField(
                value = viewModel.passwordAgain,
                onValueChange = { viewModel.onPasswordAgainChange(it) },
                label = { Text(text = "Confirm password") },
                modifier = Modifier.width(relativeWidth(.7f)),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                isError = viewModel.passwordError,
                supportingText = { Text(text = viewModel.passwordErrorText) },
            )
            RelativeVerticalSpacer(
                percent = 0.075f,
            )
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
            RelativeVerticalSpacer(
                percent = 0.075f,
            )
            Button(
                onClick = { viewModel.onRegisterClick(
                    onSuccessfulRegister = {
                        onSuccessfulRegister()
                    }
                ) },
                modifier = Modifier
                    .size(width = 150.dp, height = 60.dp)
                    .align(alignment = Alignment.End),
                shape = RoundedCornerShape(percent = 30)
            ) {
                Text (
                    text = "Register",
                    fontSize = 24.sp,
                )
            }
            RelativeVerticalSpacer(
                percent = 0.04f,
            )

            if (viewModel.progressBarActive) {
                LinearProgressIndicator(
                    modifier = Modifier.width(relativeWidth(.7f)),
                )
            }
        }
    }
}