package com.example.circulationmaze

import android.util.Log
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RegisterScreen(modifier: Modifier) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordAgain by remember { mutableStateOf("") }

    fun onRegisterClick() {
        Log.d("MyApp", "register clicked")
    }

    fun onLoginClick() {
        Log.d("MyApp", "login clicked")
    }

    Box (
        modifier = modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceContainerLowest)
    ) {
        Column (
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text(text = "Username") },
                singleLine = true,
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(text = "Email") },
                singleLine = true,
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(text = "Password") },
                singleLine = true,
            )
            OutlinedTextField(
                value = passwordAgain,
                onValueChange = { passwordAgain = it },
                label = { Text(text = "Confirm password") },
                singleLine = true,
            )
            Text(
                text = "Already have an account?"
            )
            Text(
                text = "Log in.",
                modifier = Modifier
                    .clickable { onLoginClick() },
                style = MaterialTheme.typography.bodyLarge.copy(
                    textDecoration = TextDecoration.Underline,
                ),
            )
            Button(
                onClick = { onRegisterClick() },
                modifier = Modifier.size(width = 150.dp, height = 60.dp),
                shape = RoundedCornerShape(percent = 30)
            ) {
                Text (
                    text = "Register",
                    fontSize = 24.sp,
                )
            }
        }
    }

}