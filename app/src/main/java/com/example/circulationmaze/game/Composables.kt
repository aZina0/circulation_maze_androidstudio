package com.example.circulationmaze.game

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.circulationmaze.R
import com.example.circulationmaze.game.Game.createNewGame


@Composable
fun TopBarComposable() {

}


@Composable
fun GameComposable() {
    if (!Game.initialized) {
        Piece.images = mapOf(
            Piece.Type.I to painterResource(id = R.drawable.i_piece),
            Piece.Type.L to painterResource(id = R.drawable.l_piece),
            Piece.Type.O to painterResource(id = R.drawable.o_piece),
            Piece.Type.T to painterResource(id = R.drawable.t_piece),
            Piece.Type.NONE to painterResource(id = R.drawable.nothing),
        )

        Game.screenWidthDp = LocalConfiguration.current.screenWidthDp
        createNewGame(3095248787, 13, 13)

        Game.initialized = true
    }


    Box (
        modifier = Modifier
            .size(Game.screenWidthDp!!.dp)
    ) {
        for (piece in Game.pieces.values) {
            PieceComposable(
                modifier = Modifier
                    .offset(x = piece.position.x.dp, y = piece.position.y.dp),
                piece
            )
        }
    }
}


@Composable
fun ControlsComposable() {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(top = 100.dp)) {
        Button(
            onClick = {
            }
        ) {
            Text(text = "1")
        }
    }
}