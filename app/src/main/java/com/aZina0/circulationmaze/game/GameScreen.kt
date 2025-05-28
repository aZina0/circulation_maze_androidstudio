package com.aZina0.circulationmaze.game

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.aZina0.circulationmaze.Global
import com.aZina0.circulationmaze.R
import com.aZina0.circulationmaze.game.Game.createNewGame
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore


@Composable
fun GameScreen(
    gridSize: Int,
    seed: Long,
    onReturnClicked: () -> Unit,
) {
    Global.print(gridSize.toString())
    if (!Game.initialized) {
        Piece.images = mapOf(
            Piece.Type.I to painterResource(id = R.drawable.i_piece),
            Piece.Type.L to painterResource(id = R.drawable.l_piece),
            Piece.Type.O to painterResource(id = R.drawable.o_piece),
            Piece.Type.T to painterResource(id = R.drawable.t_piece),
            Piece.Type.NONE to painterResource(id = R.drawable.nothing),
        )

        Game.screenWidthDp = LocalConfiguration.current.screenWidthDp
        createNewGame(seed, gridSize, gridSize)

        Game.initialized = true
    }


    Column {
        TopBarComposable()
        ActualGameComposable()
        ControlsComposable()
    }

    BackHandler(enabled = true) {
        Game.initialized = false

        val user = Firebase.auth.currentUser

        if (user != null) {
            val db = FirebaseFirestore.getInstance()
            db.collection("users")
                .document(user.uid)
                .collection("saves")
                .document("save1")
                .set(Game.exportCurrentGame())
                .addOnSuccessListener {
                    Global.print("Document successfully written!")
                }
                .addOnFailureListener { e ->
                    Global.print("Error writing document")
                    Global.print(e.toString())
                }
        }

        onReturnClicked()
    }
}

@Composable
fun TopBarComposable() {

}

@Composable
fun ActualGameComposable() {
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

        HighlightComposable()
    }
}


@Composable
fun ControlsComposable() {
    Row {
        Column {
            Row {
                Button (
                    onClick = { Game.pieces[Highlight.coordinate]!!.rotateByCCW90() },
                    shape = RectangleShape
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.l_piece),
                        contentDescription = "rotateCCW90"
                    )
                }
                Button (
                    onClick = { Game.pieces[Highlight.coordinate]!!.rotateBy180() },
                    shape = RectangleShape
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.l_piece),
                        contentDescription = "rotate180"
                    )
                }
                Button (
                    onClick = { Game.pieces[Highlight.coordinate]!!.rotateByCW90() },
                    shape = RectangleShape
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.l_piece),
                        contentDescription = "rotateCW90"
                    )
                }
            }
        }
        Column {
            Row {
                Box (modifier = Modifier.size(70.dp))
                Button (
                    onClick = { Highlight.moveUp() },
                    shape = RectangleShape,
                    modifier = Modifier.size(70.dp),
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        contentColor = MaterialTheme.colorScheme.onBackground,
                    )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.arrow_up),
                        contentDescription = "goUp",
                        modifier = Modifier.size(50.dp),
                    )
                }
                Box (modifier = Modifier.size(70.dp))
            }
            Row {
                Button (
                    onClick = { Highlight.moveLeft() },
                    shape = RectangleShape,
                    modifier = Modifier.size(70.dp),
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        contentColor = MaterialTheme.colorScheme.onBackground,
                    )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.arrow_left),
                        contentDescription = "goLeft",
                        modifier = Modifier.size(50.dp),
                    )
                }
                Box (modifier = Modifier.size(70.dp))
                Button (
                    onClick = { Highlight.moveRight() },
                    shape = RectangleShape,
                    modifier = Modifier.size(70.dp),
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        contentColor = MaterialTheme.colorScheme.onBackground,
                )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.arrow_right),
                        contentDescription = "goRight",
                        modifier = Modifier.size(50.dp),
                    )
                }
            }
            Row {
                Box (modifier = Modifier.size(70.dp))
                Button (
                    onClick = { Highlight.moveDown() },
                    shape = RectangleShape,
                    modifier = Modifier.size(70.dp),
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        contentColor = MaterialTheme.colorScheme.onBackground,
                    )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.arrow_down),
                        contentDescription = "goDown",
                        modifier = Modifier.size(50.dp),
                    )
                }
                Box (modifier = Modifier.size(70.dp))
            }
        }
    }
}