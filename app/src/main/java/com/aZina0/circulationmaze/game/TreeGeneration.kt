package com.aZina0.circulationmaze.game

import androidx.compose.ui.unit.IntOffset
import com.aZina0.circulationmaze.Global


object TreeGeneration {


    fun generate(): Boolean {
        val piecesLoopyList = Game.getLoopyPieceList(true)
        val nonePieces = mutableListOf<Piece>()

//        Connect all O pieces to the tree
        var repeat = true
        while (repeat) {
            repeat = false
            for (piece in piecesLoopyList) {
                if (piece.type == Piece.Type.O && !piece.active) {
                    val connected = connectOPieceToTree(piece)
                    if (connected) {
                        repeat = true
                    } else {
                        piece.rotateTo0()
                        piece.changeType(Piece.Type.NONE)
                    }
                }
            }


            val centerPiece = Game.pieces[Game.gridCenterCoordinate]!!
            val sidesWithNeighbours = centerPiece.getSidesWithNeighbours()
            for (side in sidesWithNeighbours.keys) {
                val neighbour = sidesWithNeighbours[side]!!
                if (centerPiece.getLinks()[side]!! && neighbour.type == Piece.Type.NONE) {
                    neighbour.changeType(Piece.Type.O)
                    neighbour.solve()
                    neighbour.lock()
                }
            }

            nonePieces.clear()
            for (piece in piecesLoopyList) {
                if (piece.type == Piece.Type.NONE) {
                    nonePieces.add(piece)
                }
            }

            nonePieces.shuffle()

            val neededOPieceCount = Game.targetOPieceCount - Game.countOPieces()
            for (index in 0 until neededOPieceCount) {
                val lastIndex = nonePieces.size - 1
                val piece = nonePieces.removeAt(lastIndex)
                piece.changeType(Piece.Type.O)
            }
        }


//        Fill all eligible none pieces
        repeat = true
        while (repeat) {
            repeat = false

            var index = nonePieces.size - 1
            while (index >= 0) {
                val piece = nonePieces[index]

                val filled = fillEmptyPiece(piece)
                if (filled) {
                    nonePieces.removeAt(index)
                    repeat = true
                }

                index -= 1
            }
        }


//        Fill all none piece pairs
        repeat = true
        while (repeat) {
            repeat = false
            val piecesNearbyTree = mutableListOf<Piece>()
            for (piece in nonePieces) {
                for (neighbourPiece in piece.getNeighbours()) {
                    if (neighbourPiece.type != Piece.Type.NONE) {
                        piecesNearbyTree.add(piece)
                        break
                    }
                }
            }

            val piecePairsNearbyTree: MutableList<Pair<Piece, Piece>> = mutableListOf()
            while (piecesNearbyTree.size > 0) {
                val lastIndex = piecesNearbyTree.size - 1
                val piece: Piece = piecesNearbyTree.removeAt(lastIndex)
                for (neighbourPiece in piece.getNeighbours()) {
                    if (piecesNearbyTree.contains(neighbourPiece)) {
                        piecePairsNearbyTree.add(Pair(piece, neighbourPiece))
                    }
                }
            }

            for (piecePair in piecePairsNearbyTree) {
                val filled = fillEmptyPiecePair(piecePair)
                if (filled) {
                    nonePieces.remove(piecePair.first)
                    nonePieces.remove(piecePair.second)
                    repeat = true
                }
            }
        }


//        Fill all eligible none pieces
        repeat = true
        while (repeat) {
            repeat = false

            var index = nonePieces.size - 1
            while (index >= 0) {
                val piece = nonePieces[index]

                val filled = fillEmptyPiece(piece)
                if (filled) {
                    nonePieces.removeAt(index)
                    repeat = true
                }

                index -= 1
            }
        }


//        Attempt to hijack O pieces to fill all none pieces
        val hijacked = hijackOPieces(nonePieces.size)
        if (hijacked) {
            while (nonePieces.size > 0) {
                val lastIndex = nonePieces.size - 1
                val nonePiece: Piece = nonePieces.removeAt(lastIndex)
                nonePiece.changeType(Piece.Type.O)
                connectOPieceToTree(nonePiece)
            }
        } else {
            Global.print("Couldn't find enough eligible O pieces to hijack")
        }


        if (nonePieces.size == 0) {
            return true
        } else {
            return false
        }
    }


    private fun connectOPieceToTree(oPiece: Piece): Boolean {
        val path = mutableListOf(oPiece)
        val attemptedPieces = mutableListOf<Piece>()

        while (true) {
//            # await Global . create_timer (0.05)

            var pathHead: Piece
            if (path.size > 0) {
                pathHead = path.last()
            } else {
                return false
            }
//            pathHead.flash()

            if (pathHead.active) {
                for (piece in path) {
//                    await Global.create_timer (0.25)
                    piece.activate()
                    piece.lock()
                }
                pathHead.unlock()
                if (pathHead.coordinate != Game.gridCenterCoordinate) {
                    if (pathHead.type == Piece.Type.O) {
                        pathHead.swapType()
                    } else {
                        pathHead.changeType(Piece.Type.T)
                    }
                }
                pathHead.solve()
                pathHead.lock()
                return true
            }

            val validSidesPrio0 = mutableListOf<IntOffset>()
            val validSidesPrio1 = mutableListOf<IntOffset>()
            val validSidesPrio2 = mutableListOf<IntOffset>()
            var connectSide: IntOffset? = null

            for (side in Piece.getShuffledSides()) {
                val neighbourCoordinate = pathHead.coordinate + side

                if (!Game.validCoordinate(neighbourCoordinate)) {
                    continue
                }

                val neighbour = Game.pieces[neighbourCoordinate]!!

                if (path.contains(neighbour) || attemptedPieces.contains(neighbour)) {
                    continue
                }
                else if (
                    neighbour.coordinate == Game.gridCenterCoordinate &&
                    neighbour.getLinks()[-side]!!
                ) {
                    connectSide = side
                    break
                }
                else if (
                    neighbour.type != Piece.Type.O &&
                    neighbour.type != Piece.Type.T &&
                    neighbour.coordinate != Game.gridCenterCoordinate &&
                    neighbour.active
                ) {
                    validSidesPrio0.add(side)
                }
                else if (
                    neighbour.active &&
                    neighbour.type == Piece.Type.O
                ) {
                    validSidesPrio1.add(side)
                }
                else if (neighbour.type == Piece.Type.NONE) {
                    validSidesPrio2.add(side)
                }
            }


            if (connectSide == null) {
                if (validSidesPrio0.size > 0) {
                    connectSide = validSidesPrio0[0]
                } else if (validSidesPrio1.size > 0) {
                    connectSide = validSidesPrio1[0]
                } else if (validSidesPrio2.size > 0) {
                    connectSide = validSidesPrio2[0]
                } else {
                    attemptedPieces.add(pathHead)
                    pathHead.rotateTo0()
                    if (pathHead != oPiece) {
                        pathHead.changeType(Piece.Type.NONE)
                    }
                    path.removeAt(path.size - 1)
                    continue
                }
            }

            var previousNeighbourSide: IntOffset
            if (pathHead != oPiece) {
                previousNeighbourSide = path[path.size - 2].coordinate - pathHead.coordinate

                pathHead.swapType(
                    mutableMapOf(
                        connectSide to Piece.ConnectionType.LINK,
                        previousNeighbourSide to Piece.ConnectionType.LINK,
                    )
                )
                pathHead.solve(
                    mutableMapOf(
                        connectSide to Piece.ConnectionType.LINK,
                        previousNeighbourSide to Piece.ConnectionType.LINK,
                    )
                )
            } else {
                when (connectSide) {
                    DOWN -> {
                        oPiece.rotateTo0()
                    }
                    LEFT -> {
                        oPiece.rotateTo90()
                    }
                    UP -> {
                        oPiece.rotateTo180()
                    }
                    RIGHT -> {
                        oPiece.rotateTo270()
                    }
                }
            }

            val nextPieceCoordinate = pathHead.coordinate + connectSide
            val nextPiece = Game.pieces[nextPieceCoordinate]!!
            path.add(nextPiece)
        }
    }



    private fun fillEmptyPiece(emptyPiece: Piece): Boolean {
        val neighbours = emptyPiece.getSidesWithNeighbours()

        for (sideWithNeighbour in neighbours.keys) {
            val pieceBecomingConnect = neighbours[sideWithNeighbour]!!

            if (pieceBecomingConnect.type == Piece.Type.O && pieceBecomingConnect.active) {
                val pieceBecomingO = emptyPiece

                val sideWithConnect = pieceBecomingConnect.coordinate - pieceBecomingO.coordinate
                pieceBecomingO.changeType(Piece.Type.O)
                pieceBecomingO.solve(
                    mutableMapOf(sideWithConnect to Piece.ConnectionType.LINK)
                )
                pieceBecomingO.lock()
                pieceBecomingO.activate()

                val sideWithO = pieceBecomingO.coordinate - pieceBecomingConnect.coordinate
                val sideWithOrigin = pieceBecomingConnect.getSidesWithConnectedNeighbours().keys.first()
                pieceBecomingConnect.unlock()
                pieceBecomingConnect.swapType(
                    mutableMapOf(
                        sideWithO to Piece.ConnectionType.LINK,
                        sideWithOrigin to Piece.ConnectionType.LINK,
                    )
                )
                pieceBecomingConnect.solve(
                    mutableMapOf(
                        sideWithO to Piece.ConnectionType.LINK,
                        sideWithOrigin to Piece.ConnectionType.LINK,
                    )
                )
                pieceBecomingConnect.lock()
                return true
            }
        }


        var noneNeighbourPieceCount = 0
        for (sideWithNeighbour in neighbours.keys) {
            if (neighbours[sideWithNeighbour]!!.type == Piece.Type.NONE) {
                noneNeighbourPieceCount += 1
            }
        }


        if (noneNeighbourPieceCount == 0) {
//            emptyPiece.flash(Color.PURPLE)
            val sideIndices = arrayOf(0, 1, 2, 3)
            sideIndices.shuffle()

            for (index in 0 until 4) {
                val firstSide = SIDES[sideIndices[index]]
                val secondSide = SIDES[(sideIndices[index] + 1) % 4]

                if (!(firstSide in neighbours && secondSide in neighbours)) {
                    continue
                }

                val firstNeighbour = neighbours[firstSide]!!
                val secondNeighbour = neighbours[secondSide]!!
                val cornerNeighbour = Game.pieces[emptyPiece.coordinate + firstSide + secondSide]!!

                if (!(
                    cornerNeighbour.type == Piece.Type.T &&
                    firstNeighbour.connected(cornerNeighbour) &&
                    secondNeighbour.connected(cornerNeighbour) &&
                    (firstNeighbour.type != Piece.Type.T || firstNeighbour.type != Piece.Type.T)
                )) {
                    continue
                }

//                firstNeighbour.flash(Color.BLUE)
//                secondNeighbour.flash(Color.BLUE)
//                cornerNeighbour.flash(Color.BLUE)

                firstNeighbour.unlock()
                secondNeighbour.unlock()
                cornerNeighbour.unlock()

                emptyPiece.changeType(Piece.Type.L)
                emptyPiece.solve(
                    mutableMapOf(
                        firstSide to Piece.ConnectionType.LINK,
                        secondSide to Piece.ConnectionType.LINK,
                    )
                )
                emptyPiece.lock()
                emptyPiece.activate()

                if (firstNeighbour.type == Piece.Type.T) {
                    firstNeighbour.solve()
                    firstNeighbour.lock()
                    cornerNeighbour.swapType()
                    cornerNeighbour.solve()
                    cornerNeighbour.lock()
                    secondNeighbour.swapType()
                    secondNeighbour.solve()
                    secondNeighbour.lock()
                } else if (secondNeighbour.type == Piece.Type.T) {
                    secondNeighbour.solve()
                    secondNeighbour.lock()
                    cornerNeighbour.swapType()
                    cornerNeighbour.solve()
                    cornerNeighbour.lock()
                    firstNeighbour.swapType()
                    firstNeighbour.solve()
                    firstNeighbour.lock()
                } else {
                    cornerNeighbour.swapType(
                        mutableMapOf(
                            firstNeighbour.coordinate - cornerNeighbour.coordinate to
                                    Piece.ConnectionType.LINK,
                        )
                    )
                    cornerNeighbour.solve(
                        mutableMapOf(
                            firstNeighbour.coordinate - cornerNeighbour.coordinate to
                                    Piece.ConnectionType.LINK,
                        )
                    )

                    cornerNeighbour.lock()
                    firstNeighbour.changeType(Piece.Type.T)
                    firstNeighbour.solve()
                    firstNeighbour.lock()
                    secondNeighbour.swapType()
                    secondNeighbour.solve()
                    secondNeighbour.lock()
                }

                return true
            }
        }

        return false
    }


    fun fillEmptyPiecePair(piecePair: Pair<Piece, Piece>): Boolean {
        val nonePiece1 = piecePair.first
        val nonePiece2 = piecePair.second

        if (nonePiece1.active || nonePiece2.active) {
            return false
        }

        val piece1Neighbours = nonePiece1.getSidesWithNeighbours()
        val piece2Neighbours = nonePiece2.getSidesWithNeighbours()

        for (orientation in 0 until 2) {
            var sidesWithNeighbours: List<IntOffset>
            val relativeCoordinate = nonePiece1.coordinate - nonePiece2.coordinate

            if (listOf(LEFT, RIGHT).contains(relativeCoordinate)) {
                sidesWithNeighbours = listOf(UP, DOWN)
            } else {
                sidesWithNeighbours = listOf(LEFT, RIGHT)
            }

            for (side in sidesWithNeighbours) {
                if (!(piece1Neighbours.containsKey(side) && piece2Neighbours.containsKey(side))) {
                    continue
                }

                val treePiece1 = piece1Neighbours[side]!!
                val treePiece2 = piece2Neighbours[side]!!

                if (!treePiece1.connected(treePiece2)) {
                    continue
                }

                if (treePiece1.type != Piece.Type.T || treePiece2.type != Piece.Type.T) {
                    nonePiece1.changeType(Piece.Type.L)
                    nonePiece1.solve(mutableMapOf(
                        side to Piece.ConnectionType.LINK,
                        (nonePiece2.coordinate - nonePiece1.coordinate) to Piece.ConnectionType.LINK,
                    ))
                    nonePiece1.lock()
                    nonePiece1.activate()

                    nonePiece2.changeType(Piece.Type.L)
                    nonePiece2.solve(mutableMapOf(
                        side to Piece.ConnectionType.LINK,
                        (nonePiece1.coordinate - nonePiece2.coordinate) to Piece.ConnectionType.LINK,
                    ))
                    nonePiece2.lock()
                    nonePiece2.activate()

                    if (treePiece1.type == Piece.Type.T) {
                        treePiece2.unlock()
                        treePiece1.solve()
                        treePiece2.swapType()
                        treePiece2.solve()
                        treePiece2.lock()
                    } else if (treePiece2.type == Piece.Type.T) {
                        treePiece1.unlock()
                        treePiece2.solve()
                        treePiece1.swapType()
                        treePiece1.solve()
                        treePiece1.lock()
                    } else {
                        treePiece1.unlock()
                        treePiece2.unlock()
                        treePiece1.swapType()
                        treePiece1.solve()
                        treePiece2.swapType()
                        treePiece2.solve()
                        treePiece1.lock()
                        treePiece2.lock()
                    }

                    return true
                }

                else if (treePiece1.type == Piece.Type.T && treePiece2.type == Piece.Type.T) {
                    treePiece1.unlock()
                    treePiece2.unlock()

                    nonePiece1.changeType(Piece.Type.L)
                    nonePiece2.changeType(Piece.Type.L)
                    nonePiece1.solve()
                    nonePiece2.solve()
                    nonePiece1.lock()
                    nonePiece2.lock()
                    nonePiece1.activate()
                    nonePiece2.activate()

                    treePiece1.solve()
                    treePiece2.solve()
                    treePiece1.lock()
                    treePiece2.lock()
                    treePiece1.activate()
                    treePiece2.activate()

                    return true
                }
            }
        }

        return false
    }



    fun hijackOPieces(hijackCountNeeded: Int): Boolean {
        if (hijackCountNeeded == 0) {
            return true
        }

        var piecesHijacked = 0
        val pieces = Game.pieces.values.shuffled()
        for (piece in pieces) {
            if (!(piece.type == Piece.Type.O && piece.locked)) {
                continue
            }

            val neighbours = piece.getSidesWithNeighbours()

            val randomIndexShift = Game.deterministicRandom.nextInt(0, 4)
            for (index in 0 until 4) {
                val firstSide = SIDES[(index + randomIndexShift) % 4]
                val secondSide = SIDES[(index + randomIndexShift + 1) % 4]

                if (!(firstSide in neighbours && secondSide in neighbours)) {
                    continue
                }

                val firstNeighbour = neighbours[firstSide]!!
                val secondNeighbour = neighbours[secondSide]!!
                val cornerNeighbour = Game.pieces[piece.coordinate + firstSide + secondSide]!!

                if (!(
                    firstNeighbour.connected(cornerNeighbour) &&
                    secondNeighbour.connected(cornerNeighbour) &&
                    (piece.connected(firstNeighbour) || piece.connected(secondNeighbour))
                )) {
                    continue
                }

                if (firstNeighbour.type == Piece.Type.T && secondNeighbour.type == Piece.Type.O) {
                    firstNeighbour.unlock()
                    secondNeighbour.unlock()
                    piece.unlock()

                    secondNeighbour.changeType(Piece.Type.L)
                    secondNeighbour.solve()
                    secondNeighbour.lock()
                    piece.solve()
                    piece.lock()
                    firstNeighbour.swapType()
                    firstNeighbour.solve()
                    firstNeighbour.lock()

                    piecesHijacked += 1
                    if (piecesHijacked == hijackCountNeeded) {
                        return true
                    }
                    break
                }

                else if (firstNeighbour.type == Piece.Type.O && secondNeighbour.type == Piece.Type.T) {
                    firstNeighbour.unlock()
                    secondNeighbour.unlock()
                    piece.unlock()

                    firstNeighbour.changeType(Piece.Type.L)
                    firstNeighbour.solve()
                    firstNeighbour.lock()
                    piece.solve()
                    piece.lock()
                    secondNeighbour.swapType()
                    secondNeighbour.solve()
                    secondNeighbour.lock()

                    piecesHijacked += 1
                    if (piecesHijacked == hijackCountNeeded) {
                        return true
                    }
                    break
                }
            }
        }


        return false
    }

}