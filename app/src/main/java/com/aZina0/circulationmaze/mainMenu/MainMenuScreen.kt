package com.aZina0.circulationmaze.mainMenu

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aZina0.circulationmaze.Global
import com.aZina0.circulationmaze.RelativeHorizontalSpacer
import com.aZina0.circulationmaze.RelativeVerticalSpacer
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun MainMenuScreen(
    onContinueClick: () -> Unit,
    onNewGameClick: () -> Unit,
    onLeaderboardsClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onProfileClicked: (userUid: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MainMenuViewModel = hiltViewModel()
) {

    Column {

        Box (
            modifier = Modifier
                .weight(.3f)
        ) {
            Column {
                RelativeVerticalSpacer(0.05f)
                TitleComposable()
            }
        }

        Box (
            modifier = Modifier
                .weight(.5f)
                .fillMaxWidth(),
            contentAlignment = Alignment.BottomEnd
        ) {
            Column {
                Button(
                    onClick = { onNewGameClick() },
                    contentPadding = PaddingValues(),
                    shape = RectangleShape,
                    modifier = Modifier
                        .height(Global.relativeHeight(0.065f))
                        .width(Global.relativeWidth(0.5f)),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(0x00000000),
                                        Color(0xFF1C1C1C)
                                    ) // example blue gradient
                                ),
                                shape = RectangleShape,
                            )
                            .height(Global.relativeHeight(0.075f))
                            .width(Global.relativeWidth(0.5f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "New Game",
                            fontSize = Global.relativeFont(.026f),
                            color = Color.White
                        )
                    }
                }

                RelativeVerticalSpacer(0.02f)
                Button(
                    onClick = { onContinueClick() },
                    contentPadding = PaddingValues(),
                    shape = RectangleShape,
                    modifier = Modifier
                        .height(Global.relativeHeight(0.065f))
                        .width(Global.relativeWidth(0.5f)),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(0x00000000),
                                        Color(0xFF1C1C1C)
                                    )
                                ),
                                shape = RectangleShape,
                            )
                            .height(Global.relativeHeight(0.075f))
                            .width(Global.relativeWidth(0.5f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Load game",
                            fontSize = Global.relativeFont(.026f),
                            color = Color.White
                        )
                    }
                }

                RelativeVerticalSpacer(0.02f)
                Button(
                    onClick = { onLeaderboardsClick() },
                    contentPadding = PaddingValues(),
                    shape = RectangleShape,
                    modifier = Modifier
                        .height(Global.relativeHeight(0.065f))
                        .width(Global.relativeWidth(0.5f)),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(0x00000000),
                                        Color(0xFF1C1C1C)
                                    ) // example blue gradient
                                ),
                                shape = RectangleShape,
                            )
                            .height(Global.relativeHeight(0.065f))
                            .width(Global.relativeWidth(0.5f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Leaderboards",
                            fontSize = Global.relativeFont(.026f),
                            color = Color.White
                        )
                    }
                }

                RelativeVerticalSpacer(0.02f)
                Button(
                    onClick = { onSettingsClick() },
                    contentPadding = PaddingValues(),
                    shape = RectangleShape,
                    modifier = Modifier
                        .height(Global.relativeHeight(0.065f))
                        .width(Global.relativeWidth(0.5f)),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(0x00000000),
                                        Color(0xFF1C1C1C)
                                    ) // example blue gradient
                                ),
                                shape = RectangleShape,
                            )
                            .height(Global.relativeHeight(0.075f))
                            .width(Global.relativeWidth(0.5f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Settings",
                            fontSize = Global.relativeFont(.026f),
                            color = Color.White
                        )
                    }
                }
            }
        }

        Box (
            modifier = Modifier
                .weight(.18f)
                .fillMaxWidth(),
            contentAlignment = Alignment.BottomStart
        ) {
            if (viewModel.userLoggedIn) {
                Button(
                    onClick = { onProfileClicked(Firebase.auth.currentUser!!.uid) },
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                        contentColor = MaterialTheme.colorScheme.onSurface,
                    ),
                    shape = RoundedCornerShape(
                            topStart = 0.dp,
                        topEnd = 16.dp,
                        bottomEnd = 16.dp,
                        bottomStart = 0.dp
                    ),
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .padding(start = Global.relativeWidth(0.02f))
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        viewModel.image?.let {
                            Image(
                                bitmap = it,
                                contentDescription = "picture",
                                modifier = Modifier
                                    .width(Global.relativeWidth(.15f))
                                    .height(Global.relativeWidth(.15f)),
                            )
                        }
                        RelativeHorizontalSpacer(0.02f)
                        Text(
                            text = viewModel.username,
                            fontSize = Global.relativeFont(.02f),
                            style = MaterialTheme.typography.bodyLarge.copy(
                                textDecoration = TextDecoration.Underline,
                            ),
                        )
                    }
                }
            } else {
                Column(
                    modifier = Modifier.padding(start = Global.relativeWidth(0.02f))
                ) {
                    Text (
                        text = "No logged in user.",
                        fontSize = Global.relativeFont(.017f),
                    )
                    Row {
                        Text(
                            text = "Log in",
                            modifier = Modifier
                                .clickable { onLoginClick() },
                            fontSize = Global.relativeFont(.022f),
                            style = MaterialTheme.typography.bodyLarge.copy(
                                textDecoration = TextDecoration.Underline,
                            ),
                        )
                        Text (
                            text = " or ",
                            fontSize = Global.relativeFont(.022f),
                        )
                        Text (
                            text = "register.",
                            modifier = Modifier
                                .clickable { onRegisterClick() },
                            fontSize = Global.relativeFont(.022f),
                            style = MaterialTheme.typography.bodyLarge.copy(
                                textDecoration = TextDecoration.Underline,
                            ),
                        )
                    }
                }
            }
        }
        Box(modifier = Modifier.weight(0.02f))
    }
    BackHandler(enabled = true) {}
}