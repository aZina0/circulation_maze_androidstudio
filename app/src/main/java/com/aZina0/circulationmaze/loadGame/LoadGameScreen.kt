package com.aZina0.circulationmaze.loadGame

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aZina0.circulationmaze.Global

@Composable
fun LoadGameScreen(
    onSaveClicked: (uid:String) -> Unit,
    viewModel: LoadGameViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()

    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .size(Global.relativeHeight(0.075f)),
            contentAlignment = Alignment.Center,
        ) {
            Text (
                text = "Load game",
                fontSize = 30.sp,
            )
        }


        Column (
            modifier = Modifier
                .verticalScroll(scrollState)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            for (saveInfo in viewModel.saves) {

                Button(
                    onClick = { onSaveClicked(saveInfo.basic.uid) },
                    modifier = Modifier
                        .size(
                            width = Global.relativeWidth(0.75f),
                            height = Global.relativeHeight(0.38f),
                    ),
                    contentPadding = PaddingValues(0.dp),
                    shape = RoundedCornerShape(percent = 4),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                        contentColor = MaterialTheme.colorScheme.onSurface,
                    )
                ) {
                    Column {
                        Row (
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            if (saveInfo.imageBitmap != null) {
                                Image(
                                    bitmap = saveInfo.imageBitmap,
                                    contentDescription = "img",
                                    modifier = Modifier.size(250.dp)
                                )
                            }
                            Text (
                                text = "%dx%d".format(
                                    saveInfo.basic.gridSize,
                                    saveInfo.basic.gridSize,
                                ),
                                fontSize = 15.sp,
                            )
                        }
                        Text (
                            text = "last played:",
                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                            fontSize = 11.sp,
                            lineHeight = 7.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                        Text (
                            text = saveInfo.basic.lastModifiedDate,
                            fontSize = 15.sp,
                        )
                        Text (
                            text = "seed:",
                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                            fontSize = 11.sp,
                            lineHeight = 6.sp,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                        Text (
                            text = saveInfo.basic.seed.toString(),
                            fontSize = 15.sp,
                        )
                    }
                }
            }
        }
    }
}