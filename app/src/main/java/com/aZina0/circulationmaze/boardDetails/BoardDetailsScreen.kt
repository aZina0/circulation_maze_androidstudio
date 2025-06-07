package com.aZina0.circulationmaze.boardDetails

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.aZina0.circulationmaze.CustomHeader
import com.aZina0.circulationmaze.Global
import java.time.format.DateTimeFormatter

@Composable
fun BoardDetailsScreen(
    onReturnClicked: (
        sourceRoute: String,
        leaderboardsTabIndex: Int,
        profileUserUid: String,
    ) -> Unit,
    viewModel: BoardDetailsViewModel = hiltViewModel(),
) {
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
        ) {
            if (viewModel.boardInfo != null) {
                Image(
                    bitmap = viewModel.boardInfo!!.imageBitmap,
                    contentDescription = "picture",
                    modifier = Modifier
                        .width(Global.relativeWidth(.4f))
                        .height(Global.relativeWidth(.4f)),
                )
                Row {
                    Text(
                        text = "seed:"
                    )
                    Text(
                        text = viewModel.boardInfo!!.gameData.seed.toString()
                    )
                }
                Row {
                    Text(
                        text = "grid size:"
                    )
                    Text(
                        text = viewModel.boardInfo!!.gameData.gridSize.toString()
                    )
                }
                Row {
                    Text(
                        text = "solved on:"
                    )
                    Text(
                        text = viewModel.boardInfo!!.gameData.lastModifiedDate.format(
                            DateTimeFormatter.ofPattern("dd.MM.yyyy.")
                        )
                    )
                }
                Row {
                    Text(
                        text = "time:"
                    )
                    Text(
                        text = viewModel.boardInfo!!.time.toString()
                    )
                }
                Row {
                    viewModel.userImage?.let {
                        Image(
                            bitmap = it,
                            contentDescription = "picture",
                            modifier = Modifier
                                .width(Global.relativeWidth(.4f))
                                .height(Global.relativeWidth(.4f)),
                        )
                    }
                    Text(
                        text = viewModel.username
                    )
                }
            }
        }
    }

    BackHandler(enabled = true) {
        onReturnClicked(
            viewModel.sourceRoute,
            viewModel.leaderboardsTabIndex,
            viewModel.profileUserUid
        )
    }
}