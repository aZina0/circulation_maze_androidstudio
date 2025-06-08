package com.aZina0.circulationmaze.userList

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.aZina0.circulationmaze.CustomHeader
import com.aZina0.circulationmaze.Global
import com.aZina0.circulationmaze.RelativeHorizontalSpacer

@Composable
fun UserListScreen(
    onReturnClicked: () -> Unit,
    onUserClicked: (userUid: String) -> Unit,
    viewModel: UserListViewModel = hiltViewModel(),
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(Unit) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.onStart()
        }
    }

    Column {
        CustomHeader(
            displayProgressBar = viewModel.loadingBarActive,
            middleComposable = {
                Text(
                    text = viewModel.title,
                    fontSize = Global.relativeFont(0.04f),
                )
            },
        )

        Column {
            for ((index, accountInfo) in viewModel.userList.withIndex()) {
                Button(
                    onClick = {
                        onUserClicked(accountInfo.userUid)
                    },
                    modifier = Modifier
                        .height(Global.relativeHeight(0.09f))
                        .fillMaxWidth(),
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
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Image(
                            bitmap = accountInfo.image,
                            contentDescription = "picture",
                        )
                        RelativeHorizontalSpacer(0.02f)
                        Text(
                            text = accountInfo.username,
                            fontSize = Global.relativeFont(0.025f),
                            style = MaterialTheme.typography.bodyLarge.copy(
                                textDecoration = TextDecoration.Underline,
                            ),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}