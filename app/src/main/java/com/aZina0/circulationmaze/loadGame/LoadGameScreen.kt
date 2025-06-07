package com.aZina0.circulationmaze.loadGame

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.aZina0.circulationmaze.CustomHeader
import com.aZina0.circulationmaze.Global
import com.aZina0.circulationmaze.Global.relativeFont
import com.aZina0.circulationmaze.Global.relativeHeight
import com.aZina0.circulationmaze.Global.relativeWidth
import com.aZina0.circulationmaze.R
import com.aZina0.circulationmaze.RelativeHorizontalSpacer
import com.aZina0.circulationmaze.RelativeVerticalSpacer
import java.time.format.DateTimeFormatter

@Composable
fun LoadGameScreen(
    onSaveClicked: (uid:String) -> Unit,
    onViewSolvedBoardsClicked: () -> Unit,
    onReturnClicked: () -> Unit,
    viewModel: LoadGameViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()


    Column {
        CustomHeader(
            displayProgressBar = viewModel.loadingBar,
            middleComposable = {
                Text (
                    text = "Load game",
                    fontSize = Global.relativeFont(0.04f),
                )
            },
            lastComposable = {
                Row {
                    Button(
                        onClick = { onViewSolvedBoardsClicked() },
                        modifier = Modifier
                            .width(Global.relativeWidth(0.12f)),
                        shape = RoundedCornerShape(percent = 30),
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                            contentColor = MaterialTheme.colorScheme.onSurface,
                        )
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.solved_history),
                            contentDescription = "solvedHistory",
                            tint = Color.Unspecified
                        )
                    }
                    RelativeHorizontalSpacer(0.03f)
                }
            }
        )

        Column (
            modifier = Modifier
                .verticalScroll(scrollState)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            viewModel.redrawIndicator
            for (saveInfo in viewModel.saves) {

                Button(
                    onClick = { onSaveClicked(saveInfo.gameData.uid) },
                    modifier = Modifier
                        .size(
                            width = relativeWidth(0.75f),
                            height = relativeHeight(0.38f),
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
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            if (saveInfo.imageBitmap != null) {
                                Image(
                                    bitmap = saveInfo.imageBitmap,
                                    contentDescription = "img",
                                    modifier = Modifier.size(relativeHeight(0.28f))
                                )
                            }
                            Column {
                                Button(
                                    onClick = { viewModel.deleteSaveClicked(saveInfo.gameData.uid) },
                                    shape = RoundedCornerShape(percent = 30),
                                    modifier = Modifier.size(40.dp),
                                    contentPadding = PaddingValues(0.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                                        contentColor = Color.Red,
                                    )
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.delete),
                                        contentDescription = "delete",
                                        modifier = Modifier.size(35.dp),
                                    )
                                }
                                RelativeVerticalSpacer(0.1f)
                                Text (
                                    text = "%dx%d".format(
                                        saveInfo.gameData.gridSize,
                                        saveInfo.gameData.gridSize,
                                    ),
                                    fontSize = relativeFont(.018f),
                                )
                                if (saveInfo.gameData.savedOnCloud) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.cloud),
                                        contentDescription = "delete",
                                        modifier = Modifier.size(35.dp),
                                    )
                                }
                            }
                        }
                        Text (
                            text = "last played:",
                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                            fontSize = relativeFont(.013f),
                            lineHeight = relativeFont(.007f),
                            modifier = Modifier.padding(top = relativeHeight(.009f))
                        )
                        Text (
                            text = saveInfo.gameData.lastModifiedDate
                                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm")),
                            fontSize = relativeFont(.018f),
                        )
                        Text (
                            text = "seed:",
                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                            fontSize = relativeFont(.013f),
                            lineHeight = relativeFont(.007f),
                            modifier = Modifier.padding(top = relativeHeight(.009f))
                        )
                        Text (
                            text = saveInfo.gameData.seed.toString(),
                            fontSize = relativeFont(.018f),
                        )
                    }
                }
            }
        }
    }

    when {
        viewModel.openDeleteDialog -> {
            Dialog(
                onDismissRequest = { viewModel.closeDialog() }
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .width(relativeWidth(0.7f))
                        .height(relativeHeight(0.2f)),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    )
                ) {
                    Column (
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        RelativeVerticalSpacer(0.065f)
                        Text (
                            "Delete save file permanently?",
                            fontSize = 19.sp,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                        RelativeVerticalSpacer(0.04f)
                        Row (
                            modifier = Modifier
                                .width(relativeWidth(0.7f)),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Button(
                                onClick = { viewModel.closeDialog() },
                                modifier = Modifier
                                    .height(relativeHeight(0.05f)),
                                shape = RoundedCornerShape(percent = 30),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                                    contentColor = MaterialTheme.colorScheme.onSurface,
                                )
                            ) {
                                Text(
                                    text = "Dismiss",
                                    fontSize = 20.sp,
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        textDecoration = TextDecoration.Underline,
                                    ),
                                )
                            }

                            Button(
                                onClick = { viewModel.deleteSave() },
                                modifier = Modifier
                                    .height(relativeHeight(0.05f)),
                                shape = RoundedCornerShape(percent = 30),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                                    contentColor = Color.Red
                                )
                            ) {
                                Text (
                                    text = "Confirm",
                                    fontSize = 20.sp,
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        textDecoration = TextDecoration.Underline,
                                    ),
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    BackHandler(enabled = true) {
        onReturnClicked()
    }
}