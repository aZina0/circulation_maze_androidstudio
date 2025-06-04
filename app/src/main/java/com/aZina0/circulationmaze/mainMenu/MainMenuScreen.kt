package com.aZina0.circulationmaze.mainMenu

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun MainMenuScreen(
    onContinueClick: () -> Unit,
    onNewGameClick: () -> Unit,
    onLeaderboardsClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onLoginClick: () -> Unit,
    onProfileClicked: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MainMenuViewModel = hiltViewModel()
) {
    Column {

        Box (
            modifier = Modifier
                .weight(.3f)
        ) {
            TitleComposable()
        }

        Column (
            modifier = Modifier
                .weight(.5f)
        ) {
            Button(
                onClick = { onNewGameClick() },
                modifier = Modifier
                    .size(width = 215.dp, height = 60.dp),
                shape = RoundedCornerShape(percent = 30)
            ) {
                Text (
                    text = "New Game",
                    fontSize = 24.sp,
                )
            }

            Button(
                onClick = { onContinueClick() },
                modifier = Modifier
                    .size(width = 215.dp, height = 60.dp),
                shape = RoundedCornerShape(percent = 30),
            ) {
                Text (
                    text = "Load game",
                    fontSize = 24.sp,
                )
            }

            Button(
                onClick = { onLeaderboardsClick() },
                modifier = Modifier
                    .size(width = 215.dp, height = 60.dp),
                shape = RoundedCornerShape(percent = 30)
            ) {
                Text (
                    text = "Leaderboards",
                    fontSize = 24.sp,
                )
            }

            Button(
                onClick = { onSettingsClick() },
                modifier = Modifier
                    .size(width = 215.dp, height = 60.dp),
                shape = RoundedCornerShape(percent = 30)
            ) {
                Text (
                    text = "Settings",
                    fontSize = 24.sp,
                )
            }

            Button(
                onClick = { onLoginClick() },
                modifier = Modifier
                    .size(width = 215.dp, height = 60.dp),
                shape = RoundedCornerShape(percent = 30)
            ) {
                Text (
                    text = "login",
                    fontSize = 24.sp,
                )
            }

            Button(
                onClick = { onProfileClicked() },
                modifier = Modifier
                    .size(width = 215.dp, height = 60.dp),
                shape = RoundedCornerShape(percent = 30),
                enabled = viewModel.profileEnable
            ) {
                Text (
                    text = "Profile",
                    fontSize = 24.sp,
                )
            }
        }

        Box (
            modifier = Modifier
                .weight(.2f),
            contentAlignment = Alignment.BottomStart
        ) {
            if (viewModel.userLoggedIn) {
                Text (
                    text = viewModel.username
                )
            } else {
                Text (
                    text = "not logged in"
                )
            }
        }
    }
    BackHandler(enabled = true) {}
}