package com.aZina0.circulationmaze.boardDetails

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aZina0.circulationmaze.CustomHeader
import com.aZina0.circulationmaze.Global
import com.aZina0.circulationmaze.RelativeHorizontalSpacer
import com.aZina0.circulationmaze.RelativeVerticalSpacer
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import java.time.format.DateTimeFormatter

@Composable
fun BoardDetailsScreen(
    onProfileClicked: (userUid: String) -> Unit,
    onReturnClicked: () -> Unit,
    viewModel: BoardDetailsViewModel = hiltViewModel(),
) {
    val scrollState = rememberScrollState()
    Column {
        CustomHeader(
            displayProgressBar = viewModel.loadingBarActive,
            firstComposable = {},
            middleComposable = {
                Text(
                    text = "Solve details",
                    fontSize = Global.relativeFont(0.04f),
                )
            },
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (viewModel.boardInfo != null) {
                Image(
                    bitmap = viewModel.boardInfo!!.imageBitmap,
                    contentDescription = "picture",
                    modifier = Modifier
                        .width(Global.relativeWidth(.6f))
                        .height(Global.relativeWidth(.6f)),
                )
                RelativeVerticalSpacer(.02f)
                Surface (
                    modifier = Modifier
                        .width(Global.relativeWidth(.9f)),
                    color = MaterialTheme.colorScheme.surfaceContainerLow,
                    shape = RoundedCornerShape(15.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(Global.relativeWidth(0.03f)),
                    ) {
                        Text(
                            text = "Board details",
                            fontSize = Global.relativeFont(.025f),
                        )
//                        RelativeVerticalSpacer(0.007f)
                        Column(
                            modifier = Modifier
                                .padding(Global.relativeWidth(0.02f)),
                        ) {
                            Column {
                                Text(
                                    text = "grid size:",
                                    fontSize = Global.relativeFont(0.017f),
                                )
                                Text(
                                    text = "%dx%d".format(
                                        viewModel.boardInfo!!.gameData.gridSize,
                                        viewModel.boardInfo!!.gameData.gridSize
                                    ),
                                    fontSize = Global.relativeFont(0.025f),
                                )
                            }
                            RelativeVerticalSpacer(0.007f)
                            Column {
                                Text(
                                    text = "time:",
                                    fontSize = Global.relativeFont(0.017f),
                                )
                                Text(
                                    text = Global.timerFormat2(viewModel.boardInfo!!.time),
                                    fontSize = Global.relativeFont(0.025f),
                                )
                            }
                            RelativeVerticalSpacer(0.007f)
                            Column {
                                Text(
                                    text = "seed:",
                                    fontSize = Global.relativeFont(0.017f),
                                )
                                Text(
                                    text = viewModel.boardInfo!!.gameData.seed.toString(),
                                    fontSize = Global.relativeFont(0.025f),
                                )
                            }
                            RelativeVerticalSpacer(0.007f)
                            Column {
                                Text(
                                    text = "solved on:",
                                    fontSize = Global.relativeFont(0.017f),
                                )
                                Text(
                                    text = viewModel.boardInfo!!.gameData.lastModifiedDate.format(
                                        DateTimeFormatter.ofPattern("dd.MM.yyyy.")
                                    ),
                                    fontSize = Global.relativeFont(0.025f),
                                )
                            }
                        }
                    }
                }
                RelativeVerticalSpacer(.02f)
                Surface (
                    modifier = Modifier
                        .width(Global.relativeWidth(.9f)),
                    color = MaterialTheme.colorScheme.surfaceContainerLow,
                    shape = RoundedCornerShape(15.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(Global.relativeWidth(0.03f)),
                    ) {
                        Text(
                            text = "Solver",
                            fontSize = Global.relativeFont(.025f),
                        )
                        RelativeVerticalSpacer(0.007f)
                        Button(
                            onClick = { onProfileClicked(viewModel.boardInfo!!.userUid) },
                            contentPadding = PaddingValues(0.dp),
                            shape = RectangleShape,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                                contentColor = MaterialTheme.colorScheme.onSurface,
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                viewModel.userImage?.let {
                                    Image(
                                        bitmap = it,
                                        contentDescription = "picture",
                                        modifier = Modifier
                                            .width(Global.relativeWidth(.2f))
                                            .height(Global.relativeWidth(.2f)),
                                    )
                                }
                                RelativeHorizontalSpacer(0.04f)
                                Text(
                                    text = viewModel.username,
                                    fontSize = Global.relativeFont(.025f),
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        textDecoration = TextDecoration.Underline,
                                    ),
                                )
                            }
                        }
                    }
                }

                if (viewModel.userUid == (Firebase.auth.currentUser?.uid ?: "")) {
                    RelativeVerticalSpacer(.02f)
                    Surface(
                        modifier = Modifier
                            .width(Global.relativeWidth(.9f)),
                        color = MaterialTheme.colorScheme.surfaceContainerLow,
                        shape = RoundedCornerShape(15.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(Global.relativeWidth(0.03f)),
                        ) {
                            Text(
                                text = "Sharing",
                                fontSize = Global.relativeFont(.025f),
                            )
                            RelativeVerticalSpacer(0.007f)
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (viewModel.boardIsShared) {
                                    Text(
                                        text = "Board is shared.",
                                        fontSize = Global.relativeFont(0.02f),
                                        modifier = Modifier
                                            .weight(1f)
                                    )
                                    Button(
                                        onClick = { viewModel.onShareClicked() },
                                    ) {
                                        Text(
                                            text = "Unshare"
                                        )
                                    }
                                } else {
                                    Text(
                                        text = "Board is not shared.",
                                        fontSize = Global.relativeFont(0.02f),
                                        modifier = Modifier
                                            .weight(1f)
                                    )
                                    Button(
                                        onClick = { viewModel.onShareClicked() },
                                    ) {
                                        Text(
                                            text = "Share"
                                        )
                                    }
                                }
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