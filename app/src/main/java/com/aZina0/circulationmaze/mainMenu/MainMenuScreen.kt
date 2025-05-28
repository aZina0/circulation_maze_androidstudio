package com.aZina0.circulationmaze.mainMenu

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MainMenuScreen(
    onContinueClick: () -> Unit,
    onNewGameClick: () -> Unit,
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MainMenuViewModel = MainMenuViewModel()
) {
    Column {

        Button(
            onClick = { onContinueClick() },
            modifier = Modifier
                .size(width = 150.dp, height = 60.dp),
            shape = RoundedCornerShape(percent = 30),
            enabled = viewModel.userLoggedIn
        ) {
            Text (
                text = "Continue",
                fontSize = 24.sp,
            )
        }

        Button(
            onClick = { onNewGameClick() },
            modifier = Modifier
                .size(width = 150.dp, height = 60.dp),
            shape = RoundedCornerShape(percent = 30)
        ) {
            Text (
                text = "New Game",
                fontSize = 24.sp,
            )
        }

        Button(
            onClick = { onLoginClick() },
            modifier = Modifier
                .size(width = 150.dp, height = 60.dp),
            shape = RoundedCornerShape(percent = 30)
        ) {
            Text (
                text = "login",
                fontSize = 24.sp,
            )
        }
    }
}