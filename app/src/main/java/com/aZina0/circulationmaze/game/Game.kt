package com.aZina0.circulationmaze.game

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntOffset
import kotlinx.serialization.json.JsonPrimitive
import kotlin.math.roundToInt
import kotlin.random.Random


object Game {
    val TARGET_O_PIECE_RATIO = 0.25F
    val SEED_MAX = 999999999L
    val GRID_SIZE_MIN = 5
    val GRID_SIZE_MAX = 51

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
    var spacing = 1f

    var initialized = false

    var deterministicRandom = Random(0)


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
        deterministicRandom = Random(customSeed)
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



//        await AutoSolve . solve_board ()


//        var centerPiece: Piece = pieces[gridCenterCoordinate]
        connectSubgraph(rootPiece!!)
        disconnectSubgraph(rootPiece!!)

        for (piece in pieces.values) {
            piece.unlock()
        }
        shufflePieces()

        playerPlaying = true
    }


    fun spawnPieces() {
        val totalUnscaledPiecesSize = Piece.BASE_SIZE * gridColumns
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

    fun exportCurrentGame(): MutableMap<String, MutableMap<String, Any>> {
        val export = mutableMapOf<String, MutableMap<String, Any>>()
        for ((pieceCoordinate, piece) in pieces) {
            val pieceVals = mutableMapOf<String, Any>()
            pieceVals["direction"] = JsonPrimitive(piece.direction)
            pieceVals["locked"] = JsonPrimitive(piece.locked)
            pieceVals["type"] = JsonPrimitive(piece.type.toString())
            export[pieceCoordinate.toString()] = pieceVals
        }
        return export
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
            val piece = pieceQueue.removeAt(pieceQueue.size - 1)
            piece.activate()

            if (piece.type == Piece.Type.O) {
                oPiecesActivatedCount += 1
            }

            for (neighbourPiece in piece.getConnectedNeighbours()) {
                if (!piece.sourcePieces.contains(neighbourPiece)) {
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
            val piece = pieceQueue.removeAt(pieceQueue.size - 1)
            piece.deactivate()

            for (linkedPiece in piece.linkedPieces) {
                pieceQueue.add(linkedPiece)
            }

            piece.sourcePieces.clear()
            piece.linkedPieces.clear()
            piece.updateSourceArrows()
        }
    }



    fun shufflePieces() {
        val randomVal = Random.nextFloat()
        for (coordinate in pieces.keys) {
            val piece: Piece = pieces[coordinate]!!
            piece.unlock()
            if (0 <= randomVal && randomVal < 0.25) {

            } else if (0.25 <= randomVal && randomVal < 0.5) {
                piece.rotateByCW90()
            } else if (0.5 <= randomVal && randomVal < 0.75) {
                piece.rotateBy180()
            } else if (0.75 <= randomVal && randomVal < 1f) {
                piece.rotateByCCW90()
            }
        }
    }







    fun checkForBoardComplete(): Boolean {
        for (piece in pieces.values) {
            if (!piece.active) {
                return false
            }
        }
        return true
    }


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