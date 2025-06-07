package com.aZina0.circulationmaze.newGame

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.hilt.navigation.compose.hiltViewModel
import com.aZina0.circulationmaze.CustomHeader
import com.aZina0.circulationmaze.Global
import com.aZina0.circulationmaze.RelativeHorizontalSpacer
import java.util.UUID

@Composable
fun NewGameScreen(
    onStartClicked: (uid:String, seed: Long, gridSize: Int) -> Unit,
    onReturnClicked: () -> Unit,
    viewModel: NewGameViewModel = hiltViewModel()
) {
    Column (
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomHeader(
            false,
            middleComposable = {
                Text(
                    text = "New game",
                    fontSize = Global.relativeFont(0.04f),
                    textAlign = TextAlign.Center
                )
            },
        )
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier.weight(0.45f))
            Column(
                modifier = Modifier.weight(0.4f)
            ) {
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
            }

            Box(
                modifier = Modifier.weight(0.15f)
            ) {
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
                    RelativeHorizontalSpacer(0.01f)
                    Button(
                        onClick = {
                            onStartClicked(
                                UUID.randomUUID().toString(),
                                viewModel.seedLong,
                                viewModel.gridSizeInteger,
                            )
                        },
                        modifier = Modifier
                            .width(Global.relativeWidth(.45f))
                            .height(Global.relativeHeight(.07f)),
                        shape = RoundedCornerShape(percent = 30),
                        enabled = viewModel.startGameEnabled
                    ) {
                        Text(
                            text = "Start game",
                            fontSize = Global.relativeFont(0.03f),
                        )
                    }
                }
            }


        }
    }

    BackHandler(enabled = true) {
        onReturnClicked()
    }
}