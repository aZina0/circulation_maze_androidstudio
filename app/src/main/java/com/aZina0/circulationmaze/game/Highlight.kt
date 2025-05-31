package com.aZina0.circulationmaze.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.aZina0.circulationmaze.R


@Composable
fun HighlightComposable() {
    if (Highlight.active) {
        Image(
            painter = painterResource(id = R.drawable.highlight),
            contentDescription = "highlightImg",
            colorFilter = ColorFilter.tint(Color.Red),
            modifier = Modifier
                .size((Piece.BASE_SIZE * Piece.scale).dp)
                .offset(Highlight.position_x.dp, Highlight.position_y.dp)
        )
    }
}

object Highlight {
    var coordinate = IntOffset(0, 0)
    var active by mutableStateOf(false)

    var position_x by mutableFloatStateOf(0f)
    var position_y by mutableFloatStateOf(0f)

    private fun calculatePosition() {
        position_x = Game.spacing / 2 + coordinate.x * Piece.scale * Piece.BASE_SIZE + Game.spacing * coordinate.x
        position_y = Game.spacing / 2 + coordinate.y * Piece.scale * Piece.BASE_SIZE + Game.spacing * coordinate.y
    }

    fun show() {
        active = true
    }

    fun hide() {
        active = false
    }

    fun moveUp() {
        val newLocation = coordinate + UP

        if (newLocation.y < 0) {
            coordinate = IntOffset(
                newLocation.x,
                Game.gridRows - 1,
            )
        } else {
            coordinate = IntOffset(
                newLocation.x,
                newLocation.y,
            )
        }
        calculatePosition()
        show()
    }

    fun moveDown() {
        val newLocation = coordinate + DOWN

        coordinate = IntOffset(
            newLocation.x % Game.gridColumns,
            newLocation.y % Game.gridRows,
        )
        calculatePosition()
        show()
    }

    fun moveLeft() {
        val newLocation = coordinate + LEFT

        if (newLocation.x < 0) {
            coordinate = IntOffset(
                Game.gridColumns - 1,
                newLocation.y,
            )
        } else {
            coordinate = IntOffset(
                newLocation.x,
                newLocation.y,
            )
        }
        calculatePosition()
        show()
    }

    fun moveRight() {
        val newLocation = coordinate + RIGHT

        coordinate = IntOffset(
            newLocation.x % Game.gridColumns,
            newLocation.y % Game.gridRows,
        )
        calculatePosition()
        show()
    }
}