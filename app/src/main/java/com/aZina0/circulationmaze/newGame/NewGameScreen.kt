package com.aZina0.circulationmaze.newGame

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NewGameScreen(
    onStartClicked: (gridSize: Int) -> Unit,
    viewModel: NewGameViewModel = NewGameViewModel()
) {
    Column {
        OutlinedTextField(
            value = viewModel.gridSize,
            onValueChange = { viewModel.onGridSizeChanged(it) },
            label = { Text(text = "Grid size") },
            singleLine = true,
        )

        Button(
            onClick = { onStartClicked(viewModel.gridSizeInteger) },
            modifier = Modifier
                .size(width = 150.dp, height = 60.dp),
            shape = RoundedCornerShape(percent = 30),
        ) {
            Text (
                text = "Start game",
                fontSize = 24.sp,
            )
        }

    }
}