package com.example.circulationmaze.registerLogin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.circulationmaze.RelativeVerticalSpacer

@Composable
fun LoginScreen(modifier: Modifier, onRegisterClick: () -> Unit) {
    var usernameOrEmail by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    fun onLoginClick() {

    }


    Box (
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceContainerLowest),
        contentAlignment = Alignment.Center,
    ) {
        Column {
            RelativeVerticalSpacer(
                percent = 0.2f,
            )
            OutlinedTextField(
                value = usernameOrEmail,
                onValueChange = { usernameOrEmail = it },
                label = { Text(text = "Username or email") },
                singleLine = true,
            )
            RelativeVerticalSpacer(
                percent = 0.01f,
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(text = "Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
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
                    .clickable { onRegisterClick() },
                style = MaterialTheme.typography.bodyLarge.copy(
                    textDecoration = TextDecoration.Underline,
                ),
            )
            RelativeVerticalSpacer(
                percent = 0.1f,
            )
            Button(
                onClick = { onLoginClick() },
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