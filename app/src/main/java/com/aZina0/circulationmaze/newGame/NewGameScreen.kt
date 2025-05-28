package com.aZina0.circulationmaze.newGame

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun NewGameScreen(
    onStartClicked: (gridSize: Int, seed: Long) -> Unit,
    viewModel: NewGameViewModel = hiltViewModel()
) {
    Column {
        OutlinedTextField(
            value = viewModel.gridSize,
            onValueChange = { viewModel.onGridSizeChanged(it) },
            label = { Text(text = "Grid size") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = viewModel.gridError,
            supportingText = { Text(text = viewModel.gridErrorText) },
        )
        OutlinedTextField(
            value = viewModel.seed,
            onValueChange = { viewModel.onSeedChanged(it) },
            label = { Text(text = "Game seed") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = viewModel.seedError,
            supportingText = { Text(text = viewModel.seedErrorText) },
        )

        Button(
            onClick = { onStartClicked(viewModel.gridSizeInteger, viewModel.seedLong) },
            modifier = Modifier
                .size(width = 150.dp, height = 60.dp),
            shape = RoundedCornerShape(percent = 30),
            enabled = viewModel.startGameEnabled
        ) {
            Text (
                text = "Start game",
                fontSize = 24.sp,
            )
        }

    }
}