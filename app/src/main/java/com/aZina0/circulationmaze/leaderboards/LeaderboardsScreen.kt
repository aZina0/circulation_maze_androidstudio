package com.aZina0.circulationmaze.leaderboards

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aZina0.circulationmaze.CustomHeader
import com.aZina0.circulationmaze.Global

@Composable
fun LeaderboardsScreen(
    onReturnClicked: () -> Unit,
    viewModel: LeaderboardsViewModel = hiltViewModel(),
) {
    Column {
        CustomHeader(
            displayProgressBar = viewModel.loadingBarActive,
            firstComposable = {},
            middleComposable = {
                Text(
                    text = "Leaderboards",
                    fontSize = Global.relativeFont(0.04f),
                )
            },
        )
        ScrollableTabRow(
            selectedTabIndex = viewModel.selectedTabIndex,
            edgePadding = 8.dp
        ) {
            viewModel.tabs.forEachIndexed { index, gridSize ->
                Tab(
                    selected = viewModel.selectedTabIndex == index,
                    onClick = { viewModel.onTabClicked(gridSize, index) },
                    text = { Text(text = "%dx%d".format(gridSize, gridSize)) }
                )
            }
        }

        Column {
            Row {
                Text(
                    text = "Rank",
                    modifier = Modifier.weight(0.1f)
                )
                Text(
                    text = "User",
                    modifier = Modifier.weight(0.4f)
                )
                Text(
                    text = "Time",
                    modifier = Modifier.weight(0.5f)
                )

            }

            var rank = 0
            for (board in viewModel.bestBoardsInfo) {
                rank++
                Button(
                    onClick = {},
                    shape = RectangleShape
                ) {
                    Row {
                        Text(
                            text = "%d.".format(rank),
                            modifier = Modifier.weight(0.1f)
                        )
                        Text(
                            text = board.username,
                            modifier = Modifier.weight(0.4f)
                        )
                        Text(
                            text = board.boardInfo.time.toString(),
                            modifier = Modifier.weight(0.5f)
                        )

                    }
                }
            }
        }
    }
}