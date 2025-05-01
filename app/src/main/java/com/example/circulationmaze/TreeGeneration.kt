package com.example.circulationmaze

import android.util.Log
import androidx.compose.ui.unit.IntOffset


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
                    }
                    piece.changeType(Piece.Type.NONE)
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
            Log.d("customTag", "Couldn't find enough eligible O pieces to hijack")
        }


        if (nonePieces.size == 0) {
            return true
        } else {
            return false
        }
    }


    fun connectOPieceToTree(oPiece: Piece): Boolean {
        val path = mutableListOf(oPiece)
        val attemptedPieces = mutableListOf<Piece>()

        while (true) {
//            # await Global . create_timer (0.05)

            var pathHead: Piece
            if (path.size > 0) {
                pathHead = path[-1]
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
                pathHead.lock()
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
                else if (neighbour.type == Piece.Type.NONE ) {
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
                previousNeighbourSide = path[-2].coordinate - pathHead.coordinate

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
//
//
//
//static func fillEmptyPiece(emptyPiece : Piece) -> bool:
//	# emptyPiece.flash(Color.YELLOW)
//	# await Global.create_timer(0.25)
//	var neighbours := emptyPiece.getSidesWithNeighbours()
//
//	for sideWithNeighbour in neighbours:
//		var pieceBecomingConnect := neighbours[sideWithNeighbour]
//
//		if pieceBecomingConnect.type == Piece.Type.O && pieceBecomingConnect.active:
//			var pieceBecomingO := emptyPiece
//
//			var sideWithConnect := pieceBecomingConnect.coordinate - pieceBecomingO.coordinate
//			pieceBecomingO.changeType(Piece.Type.O)
//			pieceBecomingO.solve({sideWithConnect: &"link"})
//			pieceBecomingO.lock()
//			pieceBecomingO.active = true
//
//			var sideWithO := pieceBecomingO.coordinate - pieceBecomingConnect.coordinate
//			var sideWithOrigin : IntOffset = \
//				pieceBecomingConnect.getSidesWithConnectedNeighbours().keys()[0]
//			pieceBecomingConnect.swapType({
//				sideWithO: &"link",
//				sideWithOrigin: &"link"
//			})
//			pieceBecomingConnect.solve({
//				sideWithO: &"link",
//				sideWithOrigin: &"link"
//			})
//			return true
//
//
//	var noneNeigbhourPieceCount := 0
//	for sideWithNeighbour in neighbours:
//		if neighbours[sideWithNeighbour].type == Piece.Type.none:
//			noneNeigbhourPieceCount += 1
//
//
//	if noneNeigbhourPieceCount == 0:
//		# emptyPiece.flash(Color.PURPLE)
//		var sideIndices := range(4)
//		sideIndices.shuffle()
//
//		for index in range(4):
//			var firstSide := SIDES[sideIndices[index]]
//			var secondSide := SIDES[(sideIndices[index] + 1) % 4]
//
//			if !(firstSide in neighbours && secondSide in neighbours):
//				continue
//
//			var firstNeighbour := neighbours[firstSide]
//			var secondNeighbour := neighbours[secondSide]
//			var cornerNeighbour := Game.pieces[emptyPiece.coordinate + firstSide + secondSide]
//
//			if !(
//				cornerNeighbour.type == Piece.Type.T &&
//				firstNeighbour.connected(cornerNeighbour) &&
//				secondNeighbour.connected(cornerNeighbour) &&
//				(firstNeighbour.type != Piece.Type.T || firstNeighbour.type != Piece.Type.T)
//			):
//				continue
//
//			# firstNeighbour.flash(Color.BLUE)
//			# secondNeighbour.flash(Color.BLUE)
//			# cornerNeighbour.flash(Color.BLUE)
//
//			firstNeighbour.locked = false
//			secondNeighbour.locked = false
//			cornerNeighbour.locked = false
//
//			emptyPiece.changeType(Piece.Type.L)
//			emptyPiece.solve({
//				firstSide: &"link",
//				secondSide: &"link"
//			})
//			emptyPiece.lock()
//			emptyPiece.active = true
//
//			if firstNeighbour.type == Piece.Type.T:
//				firstNeighbour.solve()
//				firstNeighbour.lock()
//				cornerNeighbour.swapType()
//				cornerNeighbour.solve()
//				cornerNeighbour.lock()
//				secondNeighbour.swapType()
//				secondNeighbour.solve()
//				secondNeighbour.lock()
//
//			else if secondNeighbour.type == Piece.Type.T:
//				secondNeighbour.solve()
//				secondNeighbour.lock()
//				cornerNeighbour.swapType()
//				cornerNeighbour.solve()
//				cornerNeighbour.lock()
//				firstNeighbour.swapType()
//				firstNeighbour.solve()
//				firstNeighbour.lock()
//
//			else:
//				cornerNeighbour.swapType({
//					firstNeighbour.coordinate - cornerNeighbour.coordinate: &"link"
//				})
//				cornerNeighbour.solve({
//					firstNeighbour.coordinate - cornerNeighbour.coordinate: &"link"
//				})
//				cornerNeighbour.lock()
//				firstNeighbour.changeType(Piece.Type.T)
//				firstNeighbour.solve()
//				firstNeighbour.lock()
//				secondNeighbour.swapType()
//				secondNeighbour.solve()
//				secondNeighbour.lock()
//
//			return true
//
//	return false
//
//
//
//static func fillEmptyPiecePair(piecePair : Array[Piece]) -> bool:
//	var nonePiece1 := piecePair[0]
//	var nonePiece2 := piecePair[1]
//
//	if nonePiece1.active || nonePiece2.active:
//		return false
//
//	var piece1Neighbours := nonePiece1.getSidesWithNeighbours()
//	var piece2Neighbours := nonePiece2.getSidesWithNeighbours()
//
//	for _orientation in range(2):
//		var sidesWithNeighbours : Array[IntOffset]
//
//		if nonePiece1.coordinate - nonePiece2.coordinate in [LEFT, RIGHT]:
//			sidesWithNeighbours = [UP, DOWN]
//		else if nonePiece1.coordinate - nonePiece2.coordinate in [UP, DOWN]:
//			sidesWithNeighbours = [LEFT, RIGHT]
//
//		for side in sidesWithNeighbours:
//			if !(piece1Neighbours.has(side) && piece2Neighbours.has(side)):
//				continue
//
//			var treePiece1 := piece1Neighbours[side]
//			var treePiece2 := piece2Neighbours[side]
//
//			if !treePiece1.connected(treePiece2):
//				continue
//
//			if treePiece1.type != Piece.Type.T || treePiece2.type != Piece.Type.T:
//
//				nonePiece1.changeType(Piece.Type.L)
//				nonePiece1.solve({
//					side: &"link",
//					nonePiece2.coordinate - nonePiece1.coordinate: &"link"
//				})
//				nonePiece1.lock()
//				nonePiece1.active = true
//
//				nonePiece2.changeType(Piece.Type.L)
//				nonePiece2.solve({
//					side: &"link",
//					nonePiece1.coordinate - nonePiece2.coordinate: &"link"
//				})
//				nonePiece2.lock()
//				nonePiece2.active = true
//
//				if treePiece1.type == Piece.Type.T:
//					treePiece2.locked = false
//					treePiece1.solve()
//					treePiece2.swapType()
//					treePiece2.solve()
//					treePiece2.lock()
//				else if treePiece2.type == Piece.Type.T:
//					treePiece1.locked = false
//					treePiece2.solve()
//					treePiece1.swapType()
//					treePiece1.solve()
//					treePiece1.lock()
//				else:
//					treePiece1.locked = false
//					treePiece2.locked = false
//					treePiece1.swapType()
//					treePiece1.solve()
//					treePiece2.swapType()
//					treePiece2.solve()
//					treePiece1.lock()
//					treePiece2.lock()
//
//				return true
//
//			else if treePiece1.type == Piece.Type.T && treePiece2.type == Piece.Type.T:
//				treePiece1.locked = false
//				treePiece2.locked = false
//
//				nonePiece1.changeType(Piece.Type.L)
//				nonePiece2.changeType(Piece.Type.L)
//				nonePiece1.solve()
//				nonePiece2.solve()
//				nonePiece1.lock()
//				nonePiece2.lock()
//				nonePiece1.active = true
//				nonePiece2.active = true
//
//				treePiece1.solve()
//				treePiece2.solve()
//				treePiece1.lock()
//				treePiece2.lock()
//				treePiece1.active = true
//				treePiece2.active = true
//
//				return true
//
//	return false
//
//
//
//static func hijackOPieces(hijackCountNeeded : int) -> bool:
//	if hijackCountNeeded == 0:
//		return true
//
//	var piecesHijacked := 0
//
//	var pieces : Array[Piece] = Game.pieces.values()
//	pieces.shuffle()
//
//	for piece in pieces:
//		if !(piece.type == Piece.Type.O && piece.locked):
//			continue
//
//		var neighbours := piece.getSidesWithNeighbours()
//
//		var randomIndexShift := randi_range(0, 3)
//		for index in range(4):
//			var firstSide := SIDES[(index + randomIndexShift) % 4]
//			var secondSide := SIDES[(index + randomIndexShift + 1) % 4]
//
//			if !(firstSide in neighbours && secondSide in neighbours):
//				continue
//
//			var firstNeighbour := neighbours[firstSide]
//			var secondNeighbour := neighbours[secondSide]
//			var cornerNeighbour := Game.pieces[piece.coordinate + firstSide + secondSide]
//
//			if !(
//				firstNeighbour.connected(cornerNeighbour) &&
//				secondNeighbour.connected(cornerNeighbour) &&
//				(piece.connected(firstNeighbour) || piece.connected(secondNeighbour))
//			):
//				continue
//
//			if firstNeighbour.type == Piece.Type.T && secondNeighbour.type == Piece.Type.O:
//				firstNeighbour.locked = false
//				secondNeighbour.locked = false
//				piece.locked = false
//
//				secondNeighbour.changeType(Piece.Type.L)
//				secondNeighbour.solve()
//				secondNeighbour.lock()
//				piece.solve()
//				piece.lock()
//				firstNeighbour.swapType()
//				firstNeighbour.solve()
//				firstNeighbour.lock()
//
//				piecesHijacked += 1
//				if piecesHijacked == hijackCountNeeded:
//					return true
//				break
//
//			else if firstNeighbour.type == Piece.Type.O && secondNeighbour.type == Piece.Type.T:
//				firstNeighbour.locked = false
//				secondNeighbour.locked = false
//				piece.locked = false
//
//				firstNeighbour.changeType(Piece.Type.L)
//				firstNeighbour.solve()
//				firstNeighbour.lock()
//				piece.solve()
//				piece.lock()
//				secondNeighbour.swapType()
//				secondNeighbour.solve()
//				secondNeighbour.lock()
//
//				piecesHijacked += 1
//				if piecesHijacked == hijackCountNeeded:
//					return true
//				break
//
//
//	return false

}