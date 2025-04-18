package com.example.circulationmaze

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration

@Composable
fun LoginScreen(modifier: Modifier) {
    var usernameOrEmail by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    fun onRegisterClick() {
        Log.d("MyApp", "test")
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
                value = usernameOrEmail,
                onValueChange = {usernameOrEmail = it},
                label = { Text("Username or email") },
                singleLine = true,
            )
            OutlinedTextField(
                value = password,
                onValueChange = {password = it},
                label = { Text("Password") },
                singleLine = true,
            )
            Text(
                text = "Don't have an account?"
            )
            Text(
                text = "Register.",
                modifier = Modifier
                    .clickable {
                        onRegisterClick()
                    },
                style = MaterialTheme.typography.bodyLarge.copy(
                    textDecoration = TextDecoration.Underline,
                    color = MaterialTheme.colorScheme.onSurface
                ),
            )
        }
    }

}