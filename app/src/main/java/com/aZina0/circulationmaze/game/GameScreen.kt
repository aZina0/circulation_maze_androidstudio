package com.aZina0.circulationmaze.game

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aZina0.circulationmaze.CustomHeader
import com.aZina0.circulationmaze.Global
import com.aZina0.circulationmaze.R
import com.aZina0.circulationmaze.RelativeHorizontalSpacer
import com.aZina0.circulationmaze.RelativeVerticalSpacer


@Composable
fun GameScreen(
    onReturnClicked: () -> Unit,
    onHomeClicked: () -> Unit,
    onStartNewGameClicked: () -> Unit,
    viewModel: GameViewModel = hiltViewModel()
) {
    if (!Game.initialized) {
        Piece.images = mapOf(
            Piece.Type.I to painterResource(id = R.drawable.i_piece),
            Piece.Type.L to painterResource(id = R.drawable.l_piece),
            Piece.Type.O to painterResource(id = R.drawable.o_piece),
            Piece.Type.T to painterResource(id = R.drawable.t_piece),
            Piece.Type.NONE to painterResource(id = R.drawable.nothing),
        )
        Game.initialized = true
    }

    Column {
        CustomHeader(
            displayProgressBar = false,
            firstComposable = {
                Row {
                    RelativeHorizontalSpacer(0.015f)
                    Button(
                        onClick = {
                            viewModel.exitGameScreen()
                            onHomeClicked()
                        },
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
                            painter = painterResource(id = R.drawable.home),
                            modifier = Modifier
                                .size(Global.relativeHeight(0.04f)),
                            contentDescription = "menu",
                        )
                    }
                    RelativeHorizontalSpacer(0.01f)
                    if (viewModel.pauseButtonVisible) {
                        Button(
                            onClick = {
                                viewModel.onPauseClicked()
                            },
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
                                painter =
                                if (viewModel.timerRunning) {
                                    painterResource(id = R.drawable.pause)
                                } else {
                                    painterResource(id = R.drawable.play)
                                },
                                modifier = Modifier
                                    .size(Global.relativeHeight(0.04f)),
                                contentDescription = "playPause",
                            )
                        }
                    }
                }
            },
            lastComposable = {
                Text (
                    text = Global.timerFormat(viewModel.milliSeconds),
                    fontSize = Global.relativeFont(0.04f),
                    modifier = Modifier.padding(end = 8.dp)
                )
            },
        )
        RelativeVerticalSpacer(0.075f)
        ActualGameComposable(viewModel)
        RelativeVerticalSpacer(0.05f)
        if (!viewModel.boardSolved) {
            ControlsComposable(viewModel)
        } else {
            BoardSolvedComposable(
                viewModel = viewModel,
                onStartNewGameClicked = {
                    viewModel.exitGameScreen()
                    onStartNewGameClicked()
                },
                onReturnClicked = {
                    viewModel.exitGameScreen()
                    onReturnClicked()
                }
            )
        }
    }

    BackHandler(enabled = true) {
        viewModel.exitGameScreen()
        onReturnClicked()
    }
}

@Composable
fun ActualGameComposable(viewModel: GameViewModel) {
    val graphicsLayer = rememberGraphicsLayer()
    viewModel.graphicsLayer = graphicsLayer

    Box (
        modifier = Modifier
            .size(Global.screenWidthDp!!.dp)
            .drawWithContent {
                graphicsLayer.record {
                    this@drawWithContent.drawContent()
                }
                drawLayer(graphicsLayer)
            }
            .blur(
                if (viewModel.timerRunning || viewModel.boardSolved) {
                    0.dp
                } else {
                    15.dp
                }
            )
    ) {
        Game.triggerRedraw
        for (piece in Game.pieces.values) {
            PieceComposable(
                modifier = Modifier
                    .offset(x = piece.position.x.dp, y = piece.position.y.dp),
                piece
            )
        }

        HighlightComposable()
    }

}


@Composable
fun ControlsComposable(viewModel: GameViewModel) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            val buttonSize = Global.relativeWidth(1f / 6) - 7.dp
            val iconSize = buttonSize - 20.dp
            Column {
                Row {
                    Box(modifier = Modifier.size(buttonSize))
                    Button(
                        onClick = {
                            viewModel.onLockClicked()
                        },
                        shape = RoundedCornerShape(percent = 30),
                        modifier = Modifier.size(buttonSize),
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                            contentColor = MaterialTheme.colorScheme.onSurface,
                        )
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.lock),
                            contentDescription = "ccw90",
                            modifier = Modifier.size(iconSize),
                        )
                    }
                    Box(modifier = Modifier.size(buttonSize))
                }
                Row {
                    Button(
                        onClick = { viewModel.onRotateCCW90Clicked() },
                        shape = RoundedCornerShape(percent = 30),
                        modifier = Modifier.size(buttonSize),
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                            contentColor = MaterialTheme.colorScheme.onSurface,
                        )
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ccw90),
                            contentDescription = "ccw90",
                            modifier = Modifier.size(iconSize),
                        )
                    }
                    Box(modifier = Modifier.size(buttonSize))
                    Button(
                        onClick = { viewModel.onRotateCW90Clicked() },
                        shape = RoundedCornerShape(percent = 30),
                        modifier = Modifier.size(buttonSize),
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                            contentColor = MaterialTheme.colorScheme.onSurface,
                        )
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.cw90),
                            contentDescription = "cw90",
                            modifier = Modifier.size(iconSize),
                        )
                    }
                }
                Row {
                    Box(modifier = Modifier.size(buttonSize))
                    Button(
                        onClick = { viewModel.onRotate180Clicked() },
                        shape = RoundedCornerShape(percent = 30),
                        modifier = Modifier.size(buttonSize),
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                            contentColor = MaterialTheme.colorScheme.onSurface,
                        )
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.r180),
                            contentDescription = "180",
                            modifier = Modifier.size(iconSize),
                        )
                    }
                    Box(modifier = Modifier.size(buttonSize))
                }
            }
            Column {
                Row {
                    Box(modifier = Modifier.size(buttonSize))
                    Button(
                        onClick = { Highlight.moveUp() },
                        shape = RoundedCornerShape(percent = 30),
                        modifier = Modifier.size(buttonSize),
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                            contentColor = MaterialTheme.colorScheme.onSurface,
                        )
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.arrow_up),
                            contentDescription = "goUp",
                            modifier = Modifier.size(iconSize),
                        )
                    }
                    Box(modifier = Modifier.size(buttonSize))
                }
                Row {
                    Button(
                        onClick = { Highlight.moveLeft() },
                        shape = RoundedCornerShape(percent = 30),
                        modifier = Modifier.size(buttonSize),
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                            contentColor = MaterialTheme.colorScheme.onSurface,
                        )
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.arrow_left),
                            contentDescription = "goLeft",
                            modifier = Modifier.size(iconSize),
                        )
                    }
                    Box(modifier = Modifier.size(buttonSize))
                    Button(
                        onClick = { Highlight.moveRight() },
                        shape = RoundedCornerShape(percent = 30),
                        modifier = Modifier.size(buttonSize),
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                            contentColor = MaterialTheme.colorScheme.onSurface,
                        )
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.arrow_right),
                            contentDescription = "goRight",
                            modifier = Modifier.size(iconSize),
                        )
                    }
                }
                Row {
                    Box(modifier = Modifier.size(buttonSize))
                    Button(
                        onClick = { Highlight.moveDown() },
                        shape = RoundedCornerShape(percent = 30),
                        modifier = Modifier.size(buttonSize),
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                            contentColor = MaterialTheme.colorScheme.onSurface,
                        )
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.arrow_down),
                            contentDescription = "goDown",
                            modifier = Modifier.size(iconSize),
                        )
                    }
                    Box(modifier = Modifier.size(buttonSize))
                }
            }
        }
    }
}

@Composable
fun BoardSolvedComposable(
    viewModel: GameViewModel,
    onStartNewGameClicked: () -> Unit,
    onReturnClicked: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.surfaceContainerLowest,
        shape = RoundedCornerShape(15.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = "Board solved!",
                fontSize = Global.relativeFont(0.04f),
            )
            Button(
                onClick = { viewModel.onShareSolveClicked() },
                modifier = Modifier
                    .width(Global.relativeWidth(.35f))
                    .height(Global.relativeHeight(.07f)),
                shape = RoundedCornerShape(percent = 30),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                )
            ) {
                Text(
                    text = if (!viewModel.shared) "Share it." else "Shared.",
                    fontSize = Global.relativeFont(0.02f),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        textDecoration = TextDecoration.Underline,
                    ),
                    color =
                        if (!viewModel.shared) {
                            MaterialTheme.colorScheme.onSurface
                        } else {
                            MaterialTheme.colorScheme.surfaceContainer
                        }
                )
            }

            Row {
                Button(
                    onClick = { onReturnClicked() },
                    modifier = Modifier
                        .width(Global.relativeWidth(.45f))
                        .height(Global.relativeHeight(.1f)),
                    shape = RoundedCornerShape(percent = 30),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                        contentColor = MaterialTheme.colorScheme.onSurface,
                    )
                ) {
                    Text(
                        text = "Return to main menu",
                        fontSize = Global.relativeFont(0.027f),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            textDecoration = TextDecoration.Underline,
                        ),
                    )
                }
                Button(
                    onClick = { onStartNewGameClicked() },
                    modifier = Modifier
                        .width(Global.relativeWidth(.45f))
                        .height(Global.relativeHeight(.1f)),
                    shape = RoundedCornerShape(percent = 30),
                ) {
                    Text(
                        text = "Start new game",
                        fontSize = Global.relativeFont(0.03f),
                    )
                }

            }
        }
    }
}