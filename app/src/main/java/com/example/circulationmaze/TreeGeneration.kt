package com.example.circulationmaze

import com.example.circulationmaze.Game.pieces


object TreeGeneration {


//    fun generate(): Boolean {
//        val piecesLoopyList = Game.getLoopyPieceList(true)
//        val nonePieces = mutableListOf<Piece>()
//
////        Connect all O pieces to the tree
//        var repeat = true
//        while (repeat) {
//            repeat = false
//            for (piece in piecesLoopyList) {
//                if (piece.type == Piece.Type.O && !piece.active) {
//                    val connected = connectOPieceToTree(piece)
//                    if (connected) {
//                        repeat = true
//                    } else {
//                        piece.rotateTo0()
//                    }
//                    piece.changeType(Piece.Type.NONE)
//                }
//            }
//
//
//            val centerPiece = Game.pieces[Game.gridCenterCoordinate]!!
//            val sidesWithNeighbours = centerPiece.getSidesWithNeighbours()
//            for (side in sidesWithNeighbours.keys) {
//                val neighbour = sidesWithNeighbours[side]!!
//                if (centerPiece.getLinks()[side]!! && neighbour.type == Piece.Type.NONE) {
//                    neighbour.changeType(Piece.Type.O)
//                    neighbour.solve()
//                    neighbour.locked = true
//                }
//            }
//
//            nonePieces.clear()
//            for piece in piecesLoopyList:
//                if piece.type == Piece.Type.none:
//                    nonePieces.append(piece)
//
//            nonePieces.shuffle()
//
//            var neededOPieceCount := Game.targetOPieceCount - Game.countOPieces()
//            for index in range(neededOPieceCount):
//                var piece : Piece = nonePieces.pop_back()
//                piece.changeType(Piece.Type.O)
//
//
//            # Fill all eligible none pieces
//            repeat = true
//            while repeat:
//                repeat = false
//
//                var index := nonePieces.size() - 1
//                while index >= 0:
//                    var piece := nonePieces[index]
//
//                    var filled := await fillEmptyPiece(piece)
//                    if filled:
//                        nonePieces.remove_at(index)
//                        repeat = true
//
//                    index -= 1
//
//
//            # Fill all none piece pairs
//            repeat = true
//            while repeat:
//                repeat = false
//                var piecesNearbyTree : Array[Piece] = []
//                for piece in nonePieces:
//                    for neighbourPiece in piece.getNeighbours():
//                        if neighbourPiece.type != Piece.Type.none:
//                            piecesNearbyTree.append(piece)
//                            break
//
//                var piecePairsNearbyTree : Array[Array] = []
//                while piecesNearbyTree.size() > 0:
//                    var piece : Piece = piecesNearbyTree.pop_back()
//                    for neighbourPiece in piece.getNeighbours():
//                        if neighbourPiece in piecesNearbyTree:
//                            piecePairsNearbyTree.append([piece, neighbourPiece])
//
//
//                for piecePair in piecePairsNearbyTree:
//                    var typedPiecePair : Array[Piece]
//                    typedPiecePair.assign(piecePair)
//                    var filled := fillEmptyPiecePair(typedPiecePair)
//                    if filled:
//                        nonePieces.erase(piecePair[0])
//                        nonePieces.erase(piecePair[1])
//                        repeat = true
//
//
//            # Fill all eligible none pieces
//            repeat = true
//            while repeat:
//                repeat = false
//
//                var index := nonePieces.size() - 1
//                while index >= 0:
//                    var piece := nonePieces[index]
//
//                    var filled := await fillEmptyPiece(piece)
//                    if filled:
//                        nonePieces.remove_at(index)
//                        repeat = true
//
//                    index -= 1
//
//
//            # Attempt to hijack O pieces to fill all none pieces
//            var hijacked := await hijackOPieces(nonePieces.size())
//            if hijacked:
//                while nonePieces.size() > 0:
//                    var nonePiece : Piece = nonePieces.pop_back()
//                    nonePiece.changeType(Piece.Type.O)
//                    await connectOPieceToTree(nonePiece)
//            else:
//                print("Couldn't find enough eligible O pieces to hijack")
//
//
//            if nonePieces.size() == 0:
//                return true
//            else:
//                return false
//        }
//    }

//static func connectOPieceToTree(oPiece : Piece) -> bool:
//	var path : Array[Piece] = [oPiece]
//	var attemptedPieces : Array[Piece] = []
//
//	while true:
//		# await Global.create_timer(0.05)
//
//		var pathHead : Piece
//		if path.size() > 0:
//			pathHead = path[-1]
//		else:
//			return false
//		# pathHead.flash()
//
//		if pathHead.active:
//
//
//			for piece in path:
//				# await Global.create_timer(0.25)
//				piece.active = true
//				piece.locked = true
//
//			pathHead.locked = false
//			if pathHead.coordinate != Game.gridCenterCoordinate:
//				if pathHead.type == Piece.Type.O:
//					pathHead.swapType()
//				else:
//					pathHead.changeType(Piece.Type.T)
//
//
//			pathHead.solve()
//			pathHead.locked = true
//
//			return true
//
//		var validSidesPrio0 : Array[Vector2i] = []
//		var validSidesPrio1 : Array[Vector2i] = []
//		var validSidesPrio2 : Array[Vector2i] = []
//
//		var connectSide : Vector2i
//
//		for side in Piece.getShuffledSides():
//			var neighbourCoordinate := pathHead.coordinate + side
//
//			if !Game.validCoordinate(neighbourCoordinate):
//				continue
//
//			var neighbour := Game.pieces[neighbourCoordinate]
//
//			if neighbour in path || neighbour in attemptedPieces:
//				continue
//
//			elif (
//				neighbour.coordinate == Game.gridCenterCoordinate &&
//				neighbour.getLinks()[-side]
//			):
//				connectSide = side
//				# neighbour.flash(Color.YELLOW)
//				break
//
//			elif (
//				neighbour.type != Piece.Type.O &&
//				neighbour.type != Piece.Type.T &&
//				neighbour.coordinate != Game.gridCenterCoordinate &&
//				neighbour.active
//			):
//				validSidesPrio0.append(side)
//				# connectSide = side
//				# break
//
//			elif (
//				neighbour.active &&
//				neighbour.type == Piece.Type.O
//			):
//				validSidesPrio1.append(side)
//
//			elif neighbour.type == Piece.Type.none:
//				validSidesPrio2.append(side)
//
//
//		if !connectSide:
//			if validSidesPrio0.size() > 0:
//				connectSide = validSidesPrio0[0]
//			elif validSidesPrio1.size() > 0:
//				connectSide = validSidesPrio1[0]
//			elif validSidesPrio2.size() > 0:
//				connectSide = validSidesPrio2[0]
//			else:
//				attemptedPieces.append(pathHead)
//				pathHead.rotateTo0()
//				if pathHead != oPiece:
//					pathHead.changeType(Piece.Type.none)
//				path.remove_at(path.size() - 1)
//				continue
//
//		var previousNeighbourSide : Vector2i
//		if pathHead != oPiece:
//			previousNeighbourSide = path[-2].coordinate - pathHead.coordinate
//
//			pathHead.swapType({
//				connectSide: &"link",
//				previousNeighbourSide: &"link"
//			})
//			pathHead.solve({
//				connectSide: &"link",
//				previousNeighbourSide: &"link"
//			})
//		else:
//			match connectSide:
//				DOWN:
//					oPiece.rotateTo0()
//				LEFT:
//					oPiece.rotateTo90()
//				UP:
//					oPiece.rotateTo180()
//				RIGHT:
//					oPiece.rotateTo270()
//
//		var nextPieceCoordinate := pathHead.coordinate + connectSide
//		var nextPiece := Game.pieces[nextPieceCoordinate]
//		path.append(nextPiece)
//
//	return false
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
//			pieceBecomingO.locked = true
//			pieceBecomingO.active = true
//
//			var sideWithO := pieceBecomingO.coordinate - pieceBecomingConnect.coordinate
//			var sideWithOrigin : Vector2i = \
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
//			emptyPiece.locked = true
//			emptyPiece.active = true
//
//			if firstNeighbour.type == Piece.Type.T:
//				firstNeighbour.solve()
//				firstNeighbour.locked = true
//				cornerNeighbour.swapType()
//				cornerNeighbour.solve()
//				cornerNeighbour.locked = true
//				secondNeighbour.swapType()
//				secondNeighbour.solve()
//				secondNeighbour.locked = true
//
//			elif secondNeighbour.type == Piece.Type.T:
//				secondNeighbour.solve()
//				secondNeighbour.locked = true
//				cornerNeighbour.swapType()
//				cornerNeighbour.solve()
//				cornerNeighbour.locked = true
//				firstNeighbour.swapType()
//				firstNeighbour.solve()
//				firstNeighbour.locked = true
//
//			else:
//				cornerNeighbour.swapType({
//					firstNeighbour.coordinate - cornerNeighbour.coordinate: &"link"
//				})
//				cornerNeighbour.solve({
//					firstNeighbour.coordinate - cornerNeighbour.coordinate: &"link"
//				})
//				cornerNeighbour.locked = true
//				firstNeighbour.changeType(Piece.Type.T)
//				firstNeighbour.solve()
//				firstNeighbour.locked = true
//				secondNeighbour.swapType()
//				secondNeighbour.solve()
//				secondNeighbour.locked = true
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
//		var sidesWithNeighbours : Array[Vector2i]
//
//		if nonePiece1.coordinate - nonePiece2.coordinate in [LEFT, RIGHT]:
//			sidesWithNeighbours = [UP, DOWN]
//		elif nonePiece1.coordinate - nonePiece2.coordinate in [UP, DOWN]:
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
//				nonePiece1.locked = true
//				nonePiece1.active = true
//
//				nonePiece2.changeType(Piece.Type.L)
//				nonePiece2.solve({
//					side: &"link",
//					nonePiece1.coordinate - nonePiece2.coordinate: &"link"
//				})
//				nonePiece2.locked = true
//				nonePiece2.active = true
//
//				if treePiece1.type == Piece.Type.T:
//					treePiece2.locked = false
//					treePiece1.solve()
//					treePiece2.swapType()
//					treePiece2.solve()
//					treePiece2.locked = true
//				elif treePiece2.type == Piece.Type.T:
//					treePiece1.locked = false
//					treePiece2.solve()
//					treePiece1.swapType()
//					treePiece1.solve()
//					treePiece1.locked = true
//				else:
//					treePiece1.locked = false
//					treePiece2.locked = false
//					treePiece1.swapType()
//					treePiece1.solve()
//					treePiece2.swapType()
//					treePiece2.solve()
//					treePiece1.locked = true
//					treePiece2.locked = true
//
//				return true
//
//			elif treePiece1.type == Piece.Type.T && treePiece2.type == Piece.Type.T:
//				treePiece1.locked = false
//				treePiece2.locked = false
//
//				nonePiece1.changeType(Piece.Type.L)
//				nonePiece2.changeType(Piece.Type.L)
//				nonePiece1.solve()
//				nonePiece2.solve()
//				nonePiece1.locked = true
//				nonePiece2.locked = true
//				nonePiece1.active = true
//				nonePiece2.active = true
//
//				treePiece1.solve()
//				treePiece2.solve()
//				treePiece1.locked = true
//				treePiece2.locked = true
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
//				secondNeighbour.locked = true
//				piece.solve()
//				piece.locked = true
//				firstNeighbour.swapType()
//				firstNeighbour.solve()
//				firstNeighbour.locked = true
//
//				piecesHijacked += 1
//				if piecesHijacked == hijackCountNeeded:
//					return true
//				break
//
//			elif firstNeighbour.type == Piece.Type.O && secondNeighbour.type == Piece.Type.T:
//				firstNeighbour.locked = false
//				secondNeighbour.locked = false
//				piece.locked = false
//
//				firstNeighbour.changeType(Piece.Type.L)
//				firstNeighbour.solve()
//				firstNeighbour.locked = true
//				piece.solve()
//				piece.locked = true
//				secondNeighbour.swapType()
//				secondNeighbour.solve()
//				secondNeighbour.locked = true
//
//				piecesHijacked += 1
//				if piecesHijacked == hijackCountNeeded:
//					return true
//				break
//
//
//	return false

}