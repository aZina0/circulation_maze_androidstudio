package com.aZina0.circulationmaze.profile

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.aZina0.circulationmaze.CustomHeader
import com.aZina0.circulationmaze.Global
import com.aZina0.circulationmaze.R
import com.aZina0.circulationmaze.RelativeHorizontalSpacer
import com.aZina0.circulationmaze.RelativeVerticalSpacer
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import java.time.format.DateTimeFormatter

@Composable
fun ProfileScreen(
    onEditProfileClicked: (userUid: String) -> Unit,
    onSignOut: () -> Unit,
    onFollowersClicked: (userUid: String) -> Unit,
    onFollowingClicked: (userUid: String) -> Unit,
    onBoardClicked: (boardUid: String) -> Unit,
    onReturnClicked: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(Unit) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.onStart()
        }
    }

    val padding = Global.relativeWidth(0.03f)
    val scrollState = rememberScrollState()

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
                if (viewModel.userUid == (Firebase.auth.currentUser?.uid ?: "")) {
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
                }
            },
        )


        Column (
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState),
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
                        if (viewModel.userUid != (Firebase.auth.currentUser?.uid ?: "")) {
                            Button(
                                onClick = { viewModel.onFollowClicked() }
                            ) {
                                if (!viewModel.userFollowed) {
                                    Text(
                                        text = "Follow"
                                    )
                                } else {
                                    Text(
                                        text = "Unfollow"
                                    )
                                }
                            }
                        }
                    }
                }
            }
            if (viewModel.description != "") {
                RelativeVerticalSpacer(.02f)
                Surface(
                    modifier = Modifier
                        .width(Global.relativeWidth(.9f)),
                    color = MaterialTheme.colorScheme.surfaceContainerLow,
                    shape = RoundedCornerShape(15.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(padding),
                    ) {
                        Text(
                            text = "About",
                            fontSize = Global.relativeFont(.025f),
                        )
                        Text (
                            text = viewModel.description,
                            fontSize = Global.relativeFont(.019f)
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
                Row(
                    modifier = Modifier
                        .padding(padding),
                ) {
                    Button(
                        onClick = {
                            onFollowersClicked(viewModel.userUid)
                        },
                        modifier = Modifier.weight(0.5f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                            contentColor = MaterialTheme.colorScheme.onSurface,
                        )
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text (
                                text = "Followers",
                                fontSize = Global.relativeFont(.025f),
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    textDecoration = TextDecoration.Underline,
                                ),
                            )
                            Text (
                                text = "%d".format(viewModel.followersCount),
                                fontSize = Global.relativeFont(.025f),
                            )
                        }

                    }
                    Button(
                        onClick = {
                            onFollowingClicked(viewModel.userUid)
                        },
                        modifier = Modifier.weight(0.5f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                            contentColor = MaterialTheme.colorScheme.onSurface,
                        )
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text (
                                text = "Following",
                                fontSize = Global.relativeFont(.025f),
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    textDecoration = TextDecoration.Underline,
                                ),
                            )
                            Text (
                                text = "%d".format(viewModel.followingCount),
                                fontSize = Global.relativeFont(.025f),
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
                    verticalArrangement = Arrangement.spacedBy(0.dp)

                ) {
                    Text (
                        text = "Shared games",
                        fontSize = Global.relativeFont(.025f),
                    )
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
                    for ((index, boardSmallInfo) in viewModel.allSharedBoardsSmallInfo.withIndex()) {
                        Button(
                            onClick = {
                                onBoardClicked(
                                    boardSmallInfo.gameData.uid,
                                )
                            },
                            modifier = Modifier
                                .height(Global.relativeHeight(0.05f)),
                            contentPadding = PaddingValues(0.dp),
                            shape = RectangleShape,
                            colors = ButtonDefaults.buttonColors(
                                containerColor =
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
                                },
                                contentColor = MaterialTheme.colorScheme.onSurface,
                            )
                        ) {
                            Row {
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
                        }
                    }
                }
            }
            RelativeVerticalSpacer(.02f)

            if (viewModel.userUid == (Firebase.auth.currentUser?.uid ?: "")) {
                Surface(
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
                            Text(
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

                        for ((index, boardSmallInfo) in viewModel.allSolvedBoardsSmallInfo.withIndex()) {
                            Button(
                                onClick = {
                                    onBoardClicked(
                                        boardSmallInfo.gameData.uid,
                                    )
                                },
                                modifier = Modifier
                                    .height(Global.relativeHeight(0.05f)),
                                contentPadding = PaddingValues(0.dp),
                                shape = RectangleShape,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor =
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
                                    },
                                    contentColor = MaterialTheme.colorScheme.onSurface,
                                )
                            ) {
                                Row {
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