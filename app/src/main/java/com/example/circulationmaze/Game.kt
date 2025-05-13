package com.example.circulationmaze

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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.circulationmaze.Game.createNewGame
import kotlin.math.roundToInt
import kotlin.random.Random


@Composable
fun GameComposable(modifier: Modifier = Modifier) {
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
    ControlsComposable(modifier)

}

@Composable
fun ControlsComposable(modifier: Modifier = Modifier) {
    Column(modifier = modifier
        .fillMaxSize()
        .padding(top = 100.dp)) {
        Button(
            onClick = {
//                var generationSuccess = TreeGeneration.generate()
//                GlobalScope.launch {TreeGeneration.generate()}
            }
        ) {
            Text(text = "1")
        }
    }
}



object Game {


    val DELAY = 0.01F
    val ATTEMPT_AMOUNT = 10
    val TARGET_O_PIECE_RATIO = 0.25F

    var highlight = IntOffset(-1, -1)
    var pieces = mutableMapOf<IntOffset, Piece>()
    var pieceCount = 0
    var gridRows = 0
    var gridColumns = 0
    var gridCenterCoordinate = IntOffset(-1, -1)
    var targetOPieceCount = 0
    var rootPiece: Piece? = null

    var playerPlaying = false
    var loopPathingEnabled = false

    var screenWidthDp: Int? = null
    var initialized = false


    fun createNewGame(customSeed: Long, gridRows: Int, gridColumns: Int) {
        playerPlaying = false

//        Delete old pieces
        pieces.clear()

//        seed(customSeed)
        Piece.resetShuffledSides()

        this.gridRows = gridRows
        this.gridColumns = gridColumns
        pieceCount = gridRows * gridColumns
        gridCenterCoordinate = (IntOffset(gridColumns, gridRows) - IntOffset(1, 1)) / 2F

//        Reset highlight coordinate
        highlight = gridCenterCoordinate

//        print("GENERATING: ", customSeed)
//        print("Target O piece count: ", round(gridRows * gridColumns * TARGET_O_PIECE_RATIO))
        spawnPieces()
//        print("Spawned O piece count: ", countOPieces())

        var generationSuccess = TreeGeneration.generate()

//        print("Final O piece count: ", countOPieces())
//        if generationSuccess:
//        print("GENERATED")
//        else:
//        print("COULDNT GENERATE SUCCESSFULLY")

//        for piece: Piece in pieces.values():
//        piece.active = false

//        Randomize the seed, shuffle the pieces and reset the seed
//        randomize()
//        shufflePieces()
//        seed(customSeed)


//        for piece: Piece in pieces.values():
//            piece.locked = true

//        await AutoSolve . solve_board ()


//        var centerPiece: Piece = pieces[gridCenterCoordinate]
        connectSubgraph(rootPiece!!)
//        disconnectSubgraph(centerPiece)

        playerPlaying = true
    }


    fun spawnPieces() {
        val totalUnscaledPiecesSize = Piece.BASE_SIZE * gridColumns
        val spacing = 1f
        val spaceAvailableForEachPiece = screenWidthDp!!.toFloat() / gridColumns - spacing
        Piece.scale = spaceAvailableForEachPiece * gridColumns / totalUnscaledPiecesSize


        for (x in 0 until gridColumns) {
            for (y in 0 until gridRows) {
                val coordinate = IntOffset(x, y)
                var piece: Piece

                val position = Offset(
                    spacing / 2 + coordinate.x * Piece.scale * Piece.BASE_SIZE + spacing * coordinate.x,
                    spacing / 2 + coordinate.y * Piece.scale * Piece.BASE_SIZE + spacing * coordinate.y,
                )

                if (coordinate == gridCenterCoordinate) {
                    val randomPieceType = Piece.Type.entries.drop(1).dropLast(1).random()
                    piece = Piece(coordinate, position, randomPieceType)
                    rootPiece = piece
                    piece.activate()
                } else {
                    piece = Piece(coordinate, position, Piece.Type.NONE)
                }

                pieces[coordinate] = piece
            }

        }

        targetOPieceCount = (pieceCount * TARGET_O_PIECE_RATIO).roundToInt()
        var extraOPiecesCount = countOPieces() - targetOPieceCount

        while (extraOPiecesCount > 0) {
            val randomCoordinate = IntOffset(
                Random.nextInt(0, gridColumns),
                Random.nextInt(0, gridRows)
            )
            if (randomCoordinate == gridCenterCoordinate) {
                continue
            }
            if (pieces[randomCoordinate]!!.type != Piece.Type.O) {
                continue
            }
            pieces[randomCoordinate]!!.changeType(Piece.Type.NONE)
            extraOPiecesCount -= 1
        }


        while (extraOPiecesCount < 0) {
            var randomCoordinate = IntOffset(
                Random.nextInt(0, gridColumns),
                Random.nextInt(0, gridRows)
            )
            if (randomCoordinate == gridCenterCoordinate) {
                continue
            }
            if (pieces[randomCoordinate]!!.type == Piece.Type.O) {
                continue
            }
            pieces[randomCoordinate]!!.changeType(Piece.Type.O)
            extraOPiecesCount += 1
        }
    }



    fun getLoopyPieceList(reverse: Boolean = false): MutableList<Piece> {
        var leftBorder = 0
        var rightBorder = gridColumns - 1
        var upBorder = 0
        var downBorder = gridRows - 1
        var direction = "right"
        var x = 0
        var y = 0

        val loopyPieces: MutableList<Piece> = mutableListOf()

        while (true) {
            loopyPieces.add(pieces[IntOffset(x, y)]!!)

            if (IntOffset(x, y) == gridCenterCoordinate) {
                break
            }

            when (direction) {
                "right" -> { x += 1 }
                "down" -> { y += 1 }
                "left" -> { x -= 1 }
                "up" -> { y -= 1 }
            }

            if (x > rightBorder) {
                direction = "down"
                upBorder += 1
                x -= 1
                y += 1
            } else if (y > downBorder) {
                direction = "left"
                rightBorder -= 1
                y -= 1
                x -= 1
            } else if (x < leftBorder) {
                direction = "up"
                downBorder -= 1
                x += 1
                y -= 1
            } else if (y < upBorder) {
                direction = "right"
                leftBorder += 1
                x += 1
                y += 1
            }
        }

        if (reverse) {
            loopyPieces.reverse()
        }

        return loopyPieces
    }





    fun connectSubgraph(firstPiece: Piece): Int {
        val pieceQueue = mutableListOf(firstPiece)
        var oPiecesActivatedCount = 0

        while (pieceQueue.size > 0) {
            val piece: Piece = pieceQueue.removeAt(pieceQueue.size - 1)
            piece.activate()

            if (piece.type == Piece.Type.O) {
                oPiecesActivatedCount += 1
            }

            for (neighbourPiece: Piece in piece.getNeighbours()) {
                if (piece.connected(neighbourPiece) && !piece.sourcePieces.contains(neighbourPiece)) {
                    pieceQueue.add(neighbourPiece)
                    piece.linkedPieces.add(neighbourPiece)
                    neighbourPiece.sourcePieces.add(piece)
                }
            }

            piece.updateSourceArrows()
        }

        return oPiecesActivatedCount
    }


    fun disconnectSubgraph(firstPiece: Piece) {
        val pieceQueue = mutableListOf(firstPiece)

        while (pieceQueue.size > 0) {
            val piece: Piece = pieceQueue.removeAt(pieceQueue.size - 1)
            piece.deactivate()

            for (linkedPiece in piece.linkedPieces) {
                pieceQueue.add(linkedPiece)
            }

            piece.sourcePieces.clear()
            piece.linkedPieces.clear()
            piece.updateSourceArrows()
        }
    }



//    fun shufflePieces() {
//        var methods:= ["CW90", "180", "CCW90"]
//        for coordinate in pieces:
//            var piece: Piece = pieces[coordinate]
//            piece.locked = false
//            methods.shuffle()
//            piece.call("rotate_by_" + methods[0])
//
//            piece.get_node("symbol").rotation_degrees = piece.direction
//    }



//    fun _on_piece_rotated(piece: Piece) {
//        if not playerPlaying:
//            return
//
//        print("ROTATED")
//
//        var coordinate:= piece.coordinate
//        # connectSubgraph(piece)
//
//        var connectedNeighbourPieces: Array[Piece] = []
//        for side: IntOffset in [IntOffset.UP, IntOffset.RIGHT, IntOffset.DOWN, IntOffset.LEFT]:
//            var neighbourPiece_coordinate:= coordinate + side
//            if not validCoordinate(neighbourPiece_coordinate):
//                continue
//            var neighbourPiece: Piece = pieces[neighbourPiece_coordinate]
//            if piece.connected(neighbourPiece):
//                connectedNeighbourPieces.append(neighbourPiece)
//
//        if not piece.active:
//            for neighbourPiece in connectedNeighbourPieces:
//                if neighbourPiece.active:
//                    piece.sourcePieces.append(neighbourPiece)
//                    neighbourPiece.linkedPieces.append(piece)
//
//            if not piece.sourcePieces.is_empty():
//                piece.active = true
//                connectSubgraph(piece)
//
//        else:
//            var sourcePieceIndex:= 0
//            while sourcePieceIndex < len(piece.sourcePieces):
//                var sourcePiece: Piece = piece.sourcePieces[sourcePieceIndex]
//                if not piece.connected(sourcePiece):
//                    sourcePiece.linkedPieces.erase(piece)
//                    piece.sourcePieces.remove_at(sourcePieceIndex)
//                else:
//                    sourcePieceIndex += 1
//
//            if piece.sourcePieces.is_empty() && not piece.has_meta("root_piece"):
//                disconnectSubgraph(piece)
//            else:
//                for neighbourPiece in connectedNeighbourPieces:
//                    if neighbourPiece not in piece.linkedPieces && neighbourPiece not in piece.sourcePieces:
//                        piece.linkedPieces.append(neighbourPiece)
//                        neighbourPiece.sourcePieces.append(piece)
//                        connectSubgraph(neighbourPiece)
//
//                var linkedPiece_index:= 0
//                while linkedPiece_index < len(piece.linkedPieces):
//                    var linkedPiece:= piece.linkedPieces[linkedPiece_index] as Piece
//                    if linkedPiece not in connectedNeighbourPieces:
//                        piece.linkedPieces.remove_at(linkedPiece_index)
//                        linkedPiece.sourcePieces.erase(piece)
//                        disconnectSubgraph(linkedPiece)
//                    else:
//                        linkedPiece_index += 1
//
//        piece.updateSourceArrows()
//
//        # if loopPathingEnabled:
//        # 	await Loops.traverse(piece)
//
//        checkForBoardComplete()
//    }



//    fun checkForBoardComplete() {
//        var activePieceCount:= 0
//        for piece: Piece in pieces.values():
//            if piece.type == Piece.Type.O && piece.active:
//                activePieceCount += 1
//
//        if activePieceCount == countOPieces():
//            print("WIN")
//    }


//    fun updateHighlight(direction: IntOffset) {
//        if highlight == IntOffset(-1, -1):
//            highlight = IntOffset(0, 0)
//        else:
//            pieces[highlight].highlighted = false
//            highlight += direction
//
//        if highlight.x < 0:
//            highlight.x = gridColumns - 1
//        if highlight.y < 0:
//            highlight.y = gridRows - 1
//
//        highlight.x %= gridColumns
//        highlight.y %= gridRows
//        pieces[highlight].highlighted = true
//    }



    fun validCoordinate(coordinate: IntOffset): Boolean {
        if (
            coordinate.x >= 0 && coordinate.x < gridColumns &&
            coordinate.y >= 0 && coordinate.y < gridRows
        ) {
            return true
        } else {
            return false
        }
    }



    fun countOPieces(): Int {
        var count = 0
        for (piece: Piece in pieces.values) {
            if (piece.type == Piece.Type.O) {
                count += 1
            }
        }
        return count
    }

}