package com.aZina0.circulationmaze.loadGame

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun LoadGameScreen(
    onSaveClicked: (uid:String) -> Unit,
    viewModel: LoadGameViewModel = hiltViewModel()
) {
    Column {
        for (fileName in viewModel.filesList) {

            Button(
                onClick = { onSaveClicked(fileName) },
                modifier = Modifier
                    .size(width = 150.dp, height = 60.dp),
                shape = RoundedCornerShape(percent = 30),
            ) {
                Text (
                    text = fileName,
                    fontSize = 24.sp,
                )
            }
        }
    }
}