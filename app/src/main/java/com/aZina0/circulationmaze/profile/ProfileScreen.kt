package com.aZina0.circulationmaze.profile

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aZina0.circulationmaze.CustomHeader
import com.aZina0.circulationmaze.Global
import com.aZina0.circulationmaze.R
import com.aZina0.circulationmaze.RelativeHorizontalSpacer
import com.aZina0.circulationmaze.RelativeVerticalSpacer
import java.time.format.DateTimeFormatter

@Composable
fun ProfileScreen(
    onEditProfileClicked: (userUid: String) -> Unit,
    onSignOut: () -> Unit,
    onReturnClicked: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val padding = Global.relativeWidth(0.03f)

    Column {
        CustomHeader(
            displayProgressBar = viewModel.loadingBarActive,
            middleComposable = {
                Text(
                    text = "Profile",
                    fontSize = Global.relativeFont(0.04f),
                )
            },
            lastComposable = {
                Row {
                    Button(
                        onClick = { onEditProfileClicked(viewModel.userUid) },
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
                            painter = painterResource(id = R.drawable.edit_profile),
                            contentDescription = "editProfile",
                        )
                    }
                    RelativeHorizontalSpacer(.02f)
                    Button(
                        onClick = {
                            viewModel.onLogoutClick()
                            onSignOut()
                        },
                        modifier = Modifier
                            .width(Global.relativeWidth(0.12f)),
                        shape = RoundedCornerShape(percent = 30),
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                            contentColor = Color.Red,
                        )
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.logout),
                            contentDescription = "logout",
                        )
                    }
                    RelativeHorizontalSpacer(.02f)
                }
            },
        )


        Column (
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface (
                modifier = Modifier
                    .width(Global.relativeWidth(.9f)),
                color = MaterialTheme.colorScheme.surfaceContainerLow,
                shape = RoundedCornerShape(15.dp)
            ) {
                Row (
                    modifier = Modifier
                        .padding(padding)
                ) {
                    Image(
                        bitmap = viewModel.picture,
                        contentDescription = "picture",
                        modifier = Modifier
                            .width(Global.relativeWidth(.4f))
                            .height(Global.relativeWidth(.4f)),
                    )
                    Column {
                        Text (
                            text = viewModel.username,
                            fontSize = Global.relativeFont(.04f)
                        )
                        Text (
                            text = "joined:",
                            fontSize = Global.relativeFont(.015f),
                            lineHeight = Global.relativeFont(.015f)
                        )
                        Text (
                            text = viewModel.dateJoined
                                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")),
                            fontSize = Global.relativeFont(.02f)
                        )
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
                        .padding(padding),
                ) {
                    Text (
                        text = "Level",
                        fontSize = Global.relativeFont(.025f),
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text (
                            text = viewModel.level.toString(),
                            fontSize = Global.relativeFont(.025f),
                        )
                        LinearProgressIndicator(
                            modifier = Modifier
                                .height(Global.relativeHeight(0.01f)),
                            color = Color.Green,
                            progress = { viewModel.xpRemainder },
                        )
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
                        .padding(padding),
                ) {
                    Text (
                        text = "Followers",
                        fontSize = Global.relativeFont(.025f),
                    )
                    Text (
                        text = "Followed",
                        fontSize = Global.relativeFont(.025f),
                    )
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
                        .padding(padding),
                ) {
                    Text (
                        text = "Leaderboards highlights",
                        fontSize = Global.relativeFont(.025f),
                    )
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
                        .padding(padding),
                ) {
                    Text (
                        text = "Shared games",
                        fontSize = Global.relativeFont(.025f),
                    )
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
                        .padding(padding),
                ) {
                    Row {
                        Text (
                            text = "All solved boards",
                            fontSize = Global.relativeFont(.025f),
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.hidden),
                            contentDescription = "hidden",
                        )
                    }
                    Row(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.surfaceContainer)
                    ) {
                        Text(
                            text = "Grid size",
                            modifier = Modifier
                                .weight(0.5f)
                        )
                        Text(
                            text = "Time",
                            modifier = Modifier
                                .weight(0.5f)
                        )
                    }

                    var index = 0
                    for (boardSmallInfo in viewModel.allSolvedBoardsSmallInfo) {
                        Row(
                            modifier = Modifier
                                .background(
                                    if (isSystemInDarkTheme()) {
                                        if (index % 2 == 0) {
                                            MaterialTheme.colorScheme.surfaceContainerLow
                                        } else {
                                            Color(0xFF161616)
                                        }
                                    } else {
                                        if (index % 2 == 0) {
                                            MaterialTheme.colorScheme.surfaceContainerLow
                                        } else {
                                            MaterialTheme.colorScheme.surfaceContainerLowest
                                        }
                                    }
                                )
                        ) {
                            Text(
                                text = "%dx%d".format(
                                    boardSmallInfo.gameData.gridSize,
                                    boardSmallInfo.gameData.gridSize,
                                ),
                                modifier = Modifier
                                    .weight(0.5f)
                            )
                            Text(
                                text = boardSmallInfo.time.toString(),
                                modifier = Modifier
                                    .weight(0.5f)
                            )
                        }
                        index++
                    }
                }
            }
        }
    }

    BackHandler(enabled = true) {
        onReturnClicked()
    }
}