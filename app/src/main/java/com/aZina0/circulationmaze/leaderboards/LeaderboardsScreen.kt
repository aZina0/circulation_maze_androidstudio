package com.aZina0.circulationmaze.leaderboards

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aZina0.circulationmaze.CustomHeader
import com.aZina0.circulationmaze.Global
import com.aZina0.circulationmaze.R
import kotlinx.coroutines.launch

@Composable
fun LeaderboardsScreen(
    onBoardClicked: (boardUid: String) -> Unit,
    onReturnClicked: () -> Unit,
    viewModel: LeaderboardsViewModel = hiltViewModel(),
) {
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

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
                    onClick = {
                        viewModel.onTabClicked(index)
                        scope.launch { scrollState.animateScrollTo(0) }
                    },
                    text = {
                        Text(
                            text = "%dx%d".format(gridSize, gridSize),
                            fontSize = Global.relativeFont(.02f),
                        )
                    }
                )
            }
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            Row(
                modifier = Modifier
                    .height(Global.relativeHeight(0.04f)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Rank",
                    fontSize = Global.relativeFont(.02f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .weight(0.2f)
                )
                Text(
                    text = "",
                    modifier = Modifier
                        .weight(0.02f)
                )
                Text(
                    text = "User",
                    fontSize = Global.relativeFont(.02f),
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .weight(0.38f)
                )
                Text(
                    text = "Time",
                    modifier = Modifier.weight(0.25f),
                    textAlign = TextAlign.End,
                )
                Text(
                    text = "",
                    modifier = Modifier
                        .weight(0.15f)
                )
            }

            Column(
                modifier = Modifier
                    .verticalScroll(scrollState),
            ) {
                for ((rank, board) in viewModel.bestBoardsInfo.withIndex()) {
                    Button(
                        onClick = {
                            onBoardClicked(
                                board.boardInfo.gameData.uid,
                            )
                        },
                        modifier = Modifier
                            .height(Global.relativeHeight(0.05f)),
                        contentPadding = PaddingValues(0.dp),
                        shape = RectangleShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor =
                            if (rank % 2 == 0) {
                                MaterialTheme.colorScheme.surfaceContainerLow
                            } else {
                                MaterialTheme.colorScheme.surfaceContainerLowest
                            },
                            contentColor = MaterialTheme.colorScheme.onSurface,
                        )
                    ) {
                        Row {
                            Text(
                                text = "%d.".format(rank + 1),
                                fontSize = Global.relativeFont(.02f),
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .weight(0.2f)
                            )
                            Text(
                                text = "",
                                modifier = Modifier
                                    .weight(0.02f)
                            )
                            Text(
                                text = board.username,
                                fontSize = Global.relativeFont(.02f),
                                modifier = Modifier
                                    .weight(0.38f)
                            )
                            Text(
                                text = board.boardInfo.time.toString(),
                                fontSize = Global.relativeFont(.02f),
                                textAlign = TextAlign.End,
                                modifier = Modifier
                                    .weight(0.25f)
                            )
                            Icon(
                                painter = painterResource(id = R.drawable.info),
                                contentDescription = "openBoard",
                                modifier = Modifier
                                    .size(Global.relativeHeight(0.023f))
                                    .weight(0.15f)
                            )
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