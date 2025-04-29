package com.example.circulationmaze

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.IntOffset


val UP = IntOffset(0, 1)
val RIGHT = IntOffset(1, 0)
val DOWN = IntOffset(0, -1)
val LEFT = IntOffset(-1, 0)
val SIDES = arrayOf(UP, RIGHT, DOWN, LEFT)

private const val INSTANT_ROTATION = true

//private val BACKGROUND_COLOR = Color()

class Piece(val coordinate: IntOffset, private val position: Offset, type: Type) {

    enum class Type {O, I, L, T, NONE}
    enum class ConnectionType {FREE, BARRIER, LINK}

    var isRootPiece = false
    var locked = false
        private set
    var highlighted = false
        private set
    var active = false
        private set
    var flashing = false
        private set
    var type = type
        private set
    var direction = 0
        private set
    //    var looped_counter := 0 : set = setLoopedCounter
    var rotation = 0
        set(value) {
            field = value
            triggerRedraw()
        }
    var linked_pieces = mutableListOf<Piece>()
    var source_pieces = mutableListOf<Piece>()

    private var redrawTrigger by mutableStateOf(false)


    companion object {
//        val DEFAULT_COLOR: Color = Color.getColor("#515151")
        const val BASE_SIZE = 64.0F
        var animationSpeed = 0.1F
        var scale = 1f

        @JvmField
        var shuffledSides = arrayOf(UP, RIGHT, DOWN, LEFT)

        lateinit var oPieceImage: ImageBitmap
        lateinit var iPieceImage: ImageBitmap
        lateinit var lPieceImage: ImageBitmap
        lateinit var tPieceImage: ImageBitmap

        fun sameConnectionType(
            connectionTypes: Map<IntOffset, ConnectionType>,
            relativePosition: String,
            typeToCheck: ConnectionType
        ): Boolean {

            when (relativePosition) {
                "adjacent" -> {
                    if (
                        connectionTypes[LEFT] == connectionTypes[UP] &&
                        connectionTypes[LEFT] == typeToCheck
                    ) {
                        return true

                    } else if (
                        connectionTypes[UP] == connectionTypes[RIGHT] &&
                        connectionTypes[UP] == typeToCheck
                    ) {
                        return true

                    } else if (
                        connectionTypes[RIGHT] == connectionTypes[DOWN] &&
                        connectionTypes[RIGHT] == typeToCheck
                    ) {
                        return true

                    } else if (
                        connectionTypes[DOWN] == connectionTypes[LEFT] &&
                        connectionTypes[DOWN] == typeToCheck
                    ) {
                        return true
                    }
                }


                "across" -> {
                    if (
                        connectionTypes[LEFT] == connectionTypes[RIGHT] &&
                        connectionTypes[LEFT] == typeToCheck
                    ) {
                        return true
                    }
                    else if (
                        connectionTypes[UP] == connectionTypes[DOWN] &&
                        connectionTypes[UP] == typeToCheck
                    ) {
                        return true
                    }
                }
            }

            return false
        }


        fun getShuffledSides(): List<IntOffset> {
            shuffledSides.shuffle()
            return shuffledSides.toList()
        }


        fun resetShuffledSides() {
            shuffledSides = arrayOf(UP, RIGHT, DOWN, LEFT)
        }
    }


    fun draw(drawScope: DrawScope) {
        Log.d("TEST", "redrawn $position")
        with(drawScope) {
            redrawTrigger
            rotate(
                degrees = rotation.toFloat(),
                pivot = position + Offset(BASE_SIZE * scale, BASE_SIZE * scale) / 2f
            ) {
                drawRect(
                    Color.Blue,
                    topLeft = position,
                    size = Size(BASE_SIZE * scale, BASE_SIZE * scale)
                )
                when (type) {
                    Type.O -> {
                        drawImage(image = oPieceImage, topLeft = position)
                    }
                    Type.I -> {
                        drawImage(image = iPieceImage, topLeft = position)
                    }
                    Type.L -> {
                        drawImage(image = lPieceImage, topLeft = position)
                    }
                    Type.T -> {
                        drawImage(image = tPieceImage, topLeft = position)
                    }
                    Type.NONE -> {}
                }
            }
        }
    }

    private fun triggerRedraw() {
        redrawTrigger = !redrawTrigger
    }


//    fun _to_string(): String {
//        return "pieceAT({0},{1})".format([coordinate.x, coordinate.y])
//    }


    fun changeType(pieceType: Type) {
        type = pieceType
        triggerRedraw()
    }


//    fun refresh() {
//        if (direction == 90) {
//            rotateByCCW90()
//        } else if (direction == 180) {
//            rotateBy180()
//        } else if (direction == 270) {
//            rotateByCW90()
//        }
////        $symbol.texture = textures[type]
////        $symbol.modulate = DEFAULT_COLOR
//
//        setActive(active)
//    }


//    fun update_source_arrows() {
//        for node : Polygon2D in [$up, $right, $down, $left]:
//        node.visible = false
//
//        for (source_piece: Piece in source_pieces) {
//            var relativeCoordinate: IntOffset = source_piece.coordinate - coordinate
////            when (relativeCoordinate) {
////                UP -> $up.visible = true
////                RIGHT -> $right.visible = true
////                DOWN -> $down.visible = true
////                LEFT -> $left.visible = true
////            }
//        }
//
//    }



//    fun setHighlight(value: Boolean) {
//        highlighted = value
////        $highlight.visible = value
////        if value:
////            $symbol.modulate = Color.GREEN
////        else:
////            if $symbol.modulate != Color.RED:
////                $symbol.modulate = DEFAULT_COLOR
//    }

//    fun setLock(value: Boolean) {
//        locked = value
//        if value:
//        self_modulate = Color("252525")
//        if Game.playerPlaying:
//        History.add_action(self, History.ActionType.lock)
//        else:
//        self_modulate = Color.BLACK
//        if Game.playerPlaying:
//        History.add_action(self, History.ActionType.unlock)
//    }

//    fun setActive(value: Boolean) {
//        if (isRootPiece && !value) {
//            return
//        }
//
//        active = value
//
//        if (value) {
//            activated.emit()
//            if (isRootPiece) {
//                $symbol.modulate = Color.GOLD
//            } else {
//                $symbol.modulate = Color("008700")
//            }
//        } else {
//            deactivated.emit()
//        }
//        $symbol.modulate = DEFAULT_COLOR
//    }

//    fun setLoopedCounter(value: int) {
//        looped_counter = value
//        $Label.text = str(looped_counter)
//        if looped_counter > 0:
//        $symbol.modulate = Color.BLUE
//        else:
//        $symbol.modulate = Color("515151")
//    }


//    fun err(color: Color = colors) {
//        $symbol.modulate = color
//    }


//    fun flash(custom_color:= Color.HOT_PINK, count:= 1):
//    if flashing: return
//    flashing = true
//    var highlight_visible:= highlighted
//    var highlight_color: Color = $highlight.color
//
//    for i in range(count):
//    $highlight.visible = true
//    $highlight.color = custom_color
//    await Global.create_timer(0.25)
//    $highlight.visible = highlight_visible
//    $highlight.color = highlight_color
//    await Global.create_timer(0.25)
//
//    flashing = false



    fun swapType(connections: MutableMap<IntOffset, ConnectionType> = mutableMapOf()) {
        var linkCount = 0
        var barrierCount = 0

        for (side in SIDES) {
            var connectionType: ConnectionType
            if (side in connections) {
                connectionType = connections[side]!!
            } else {
                connectionType = getNeighboursConnectionType(side)
                connections[side] = connectionType
            }

            if (connectionType == ConnectionType.BARRIER) {
                barrierCount += 1
            } else if (connectionType == ConnectionType.LINK) {
                linkCount += 1
            }
        }


        if (
            (
                sameConnectionType(connections, "adjacent", ConnectionType.BARRIER) &&
                barrierCount == 2
            )
            ||
            (
                sameConnectionType(connections, "adjacent", ConnectionType.LINK) &&
                linkCount == 2
            )
        ) {
            changeType(Type.L)

        } else if (
            (
                sameConnectionType(connections, "across", ConnectionType.BARRIER) &&
                barrierCount == 2
            )
            ||
            (
                sameConnectionType(connections, "across", ConnectionType.LINK) &&
                linkCount == 2
            )
        ) {
            changeType(Type.I)

        } else if (linkCount == 1 || barrierCount == 3) {
            changeType(Type.O)

        } else if (linkCount == 3 || barrierCount == 1) {
            changeType(Type.T)
        }
    }





//    fun solveAndSpread(
//    connectionTypes: Dictionary[IntOffset, StringName] = {},
//    random_choice:= false
//    ): Boolean:
//
//    var solved:= solve(connectionTypes, random_choice)
//    if solved:
//    locked = true
//    for neighbour in getNeighbours():
//    if !neighbour.locked:
//    neighbour.solveAndSpread()
//
//    return solved


//    fun solve(connectionTypes:= {}, random_choice:= false): Boolean:
//    var linkCount:= 0
//    var barrierCount:= 0
//    var link:= {}
//    var barrier:= {}
//
//    for side in SIDES:
//    var connectionType: StringName
//    if side in connectionTypes:
//    connectionType = connectionTypes[side]
//    else:
//    connectionType = getNeighboursConnectionType(side)
//
//    if connectionType == &BARRIER:
//    barrierCount += 1
//    else if connectionType == &LINK:
//    linkCount += 1
//
//    link[side] = connectionType == &LINK
//    barrier[side] = connectionType == &BARRIER
//
//
//    match type:
//    Type.O:
//    if linkCount == 1:
//    if link[DOWN]:
//    rotateTo0()
//    return true
//    else if link[LEFT]:
//    rotateTo90()
//    return true
//    else if link[UP]:
//    rotateTo180()
//    return true
//    else if link[RIGHT]:
//    rotateTo270()
//    return true
//
//    else if barrierCount == 3:
//    if barrier[RIGHT] && barrier[UP] && barrier[LEFT]:
//    rotateTo0()
//    return true
//    else if barrier[UP] && barrier[RIGHT] && barrier[DOWN]:
//    rotateTo90()
//    return true
//    else if barrier[DOWN] && barrier[RIGHT] && barrier[LEFT]:
//    rotateTo180()
//    return true
//    else if barrier[UP] && barrier[LEFT] && barrier[DOWN]:
//    rotateTo270()
//    return true
//
//
//    Piece.Type.L:
//    if (
//    (link[RIGHT] && link[LEFT]) || (link[UP] && link[DOWN]) ||
//    (barrier[RIGHT] && barrier[LEFT]) || (barrier[UP] && barrier[DOWN])
//    ):
//    pass
//
//    else if (
//    (link[RIGHT] && link[DOWN]) || (barrier[LEFT] && barrier[UP]) ||
//    (link[RIGHT] && barrier[UP]) || (link[DOWN] && barrier[LEFT])
//    ):
//    rotateTo0()
//    return true
//
//    else if (
//    (link[DOWN] && link[LEFT]) || (barrier[UP] && barrier[RIGHT]) ||
//    (link[DOWN] && barrier[RIGHT]) || (link[LEFT] && barrier[UP])
//    ):
//    rotateTo90()
//    return true
//
//    else if (
//    (link[LEFT] && link[UP]) || (barrier[RIGHT] && barrier[DOWN]) ||
//    (link[LEFT] && barrier[DOWN]) || (link[UP] && barrier[RIGHT])
//    ):
//    rotateTo180()
//    return true
//
//    else if (
//    (link[UP] && link[RIGHT]) || (barrier[DOWN] && barrier[LEFT]) ||
//    (link[UP] && barrier[LEFT]) || (link[RIGHT] && barrier[DOWN])
//    ):
//    rotateTo270()
//    return true
//
//    else if random_choice:
//    if link[DOWN] || barrier[UP]:
//    if randf() >= 0.5:
//    rotateTo0()
//    return true
//    else:
//    rotateTo90()
//    return true
//    else if link[LEFT] || barrier[RIGHT]:
//    if randf() >= 0.5:
//    rotateTo90()
//    return true
//    else:
//    rotateTo180()
//    return true
//
//    else if link[UP] || barrier[DOWN]:
//    if randf() >= 0.5:
//    rotateTo180()
//    return true
//    else:
//    rotateTo270()
//    return true
//    else if link[RIGHT] || barrier[LEFT]:
//    if randf() >= 0.5:
//    rotateTo270()
//    return true
//    else:
//    rotateTo0()
//    return true
//
//    Piece.Type.I:
//    if barrier[LEFT] || barrier[RIGHT] || link[UP] || link[DOWN]:
//    if link[LEFT] || link[RIGHT] || barrier[UP] || barrier[DOWN]:
//    pass
//    else:
//    rotateTo0()
//    return true
//    else if barrier[UP] || barrier[DOWN] || link[LEFT] || link[RIGHT]:
//    if link[UP] || link[DOWN] || barrier[LEFT] || barrier[RIGHT]:
//    pass
//    else:
//    rotateTo90()
//    return true
//
//    Piece.Type.T:
//    if barrierCount > 1 || linkCount == 4:
//    pass
//    else if barrier[LEFT] || (link[UP] && link[RIGHT] && link[DOWN]):
//    rotateTo0()
//    return true
//    else if barrier[UP] || (link[RIGHT] && link[DOWN] && link[LEFT]):
//    rotateTo90()
//    return true
//    else if barrier[RIGHT] || (link[DOWN] && link[LEFT] && link[UP]):
//    rotateTo180()
//    return true
//    else if barrier[DOWN] || (link[LEFT] && link[UP] && link[RIGHT]):
//    rotateTo270()
//    return true
//    else if random_choice:
//    if link[UP] && link[DOWN]:
//    if randf() > 0.5:
//    rotateTo0()
//    return true
//    else:
//    rotateTo180()
//    return true
//    else if link[LEFT] && link[RIGHT]:
//    if randf() > 0.5:
//    rotateTo90()
//    return true
//    else:
//    rotateTo270()
//    return true
//    else if link[RIGHT] && link[DOWN]:
//    if randf() > 0.5:
//    rotateTo0()
//    return true
//    else:
//    rotateTo90()
//    return true
//    else if link[DOWN] && link[LEFT]:
//    if randf() > 0.5:
//    rotateTo90()
//    return true
//    else:
//    rotateTo180()
//    return true
//    else if link[LEFT] && link[UP]:
//    if randf() > 0.5:
//    rotateTo180()
//    return true
//    else:
//    rotateTo270()
//    return true
//    else if link[UP] && link[RIGHT]:
//    if randf() > 0.5:
//    rotateTo270()
//    return true
//    else:
//    rotateTo0()
//    return true
//
//
//    if type == Type.I:
//    for orientation: Array in [[UP, DOWN], [LEFT, RIGHT]]:
//    var iPieceLine: Array[Piece] = [self]
//
//    var firstDirection: IntOffset = orientation[0]
//    var secondDirection: IntOffset = orientation[1]
//    var sideChecks:= 0
//
//    while true:
//    var edgePiece:= iPieceLine[0]
//    var checkCoordinate:= edgePiece.coordinate + firstDirection
//
//    if !Game.validCoordinate(checkCoordinate):
//    break
//
//    var checkPiece: Piece = Game.pieces[checkCoordinate]
//    if checkPiece.type == Type.I:
//    iPieceLine.push_front(checkPiece)
//    else if checkPiece.type == Type.O:
//    sideChecks += 1
//    break
//    else:
//    break
//
//    if sideChecks == 0:
//    continue
//
//    while true:
//    var edgePiece:= iPieceLine[iPieceLine.size() - 1]
//    var checkCoordinate:= edgePiece.coordinate + secondDirection
//
//    if !Game.validCoordinate(checkCoordinate):
//    break
//
//    var checkPiece: Piece = Game.pieces[checkCoordinate]
//    if checkPiece.type == Type.I:
//    iPieceLine.append(checkPiece)
//    else if checkPiece.type == Type.O:
//    sideChecks += 1
//    break
//    else:
//    break
//
//    if sideChecks == 2:
//    if orientation == [UP, DOWN]:
//    for iPiece in iPieceLine:
//    iPiece.rotateTo90()
//    iPiece.locked = true
//    else if orientation == [LEFT, RIGHT]:
//    for iPiece in iPieceLine:
//    iPiece.rotateTo0()
//    iPiece.locked = true
//    return true
//
//
//    return false



    fun getNeighbours(): List<Piece> {
        val neighbourPieces = mutableListOf<Piece>()
        for (side in SIDES) {
            if (Game.validCoordinate(coordinate + side)) {
                neighbourPieces.add(Game.pieces[coordinate + side]!!)
            }
        }

        return neighbourPieces
    }

    fun getSidesWithNeighbours(): Map<IntOffset, Piece> {
        val neighbourPieces = mutableMapOf<IntOffset, Piece>()
        for (side in SIDES) {
            if (Game.validCoordinate(coordinate + side)) {
                neighbourPieces[side] = Game.pieces[coordinate + side]!!
            }
        }

        return neighbourPieces
    }

    fun getConnectedNeighbours(): List<Piece> {
        val neighbourPieces = getNeighbours().toMutableList()
        var pieceIndex = neighbourPieces.size - 1
        while (pieceIndex >= 0) {
            if (!connected(neighbourPieces[pieceIndex])) {
                neighbourPieces.removeAt(pieceIndex)
            }
            pieceIndex -= 1
        }

        return neighbourPieces
    }

    fun getSidesWithConnectedNeighbours(): Map<IntOffset, Piece> {
        val sidesWithNeighbours = getSidesWithNeighbours().toMutableMap()

        for (side in sidesWithNeighbours.keys) {
            if (!connected(sidesWithNeighbours[side]!!)) {
                sidesWithNeighbours.remove(side)
            }
        }

        return sidesWithNeighbours
    }

    fun connected(neighbourPiece: Piece): Boolean {
        val pieceLinks = getLinks()
        val neighbourPieceLinks = neighbourPiece.getLinks()

        val relativeCoordinate = neighbourPiece.coordinate - coordinate
        when (relativeCoordinate) {
            UP -> {
                if (pieceLinks[UP]!! && neighbourPieceLinks[DOWN]!!) {
                    return true
                }
            }
            RIGHT -> {
                if (pieceLinks[RIGHT]!! && neighbourPieceLinks[LEFT]!!) {
                    return true
                }
            }
            DOWN -> {
                if (pieceLinks[DOWN]!! && neighbourPieceLinks[UP]!!) {
                    return true
                }
            }
            LEFT -> {
                if (pieceLinks[LEFT]!! && neighbourPieceLinks[RIGHT]!!) {
                    return true
                }
            }
        }
        return false
    }



    fun getLinks(): Map<IntOffset, Boolean> {
        val links: MutableMap<IntOffset, Boolean> = mutableMapOf(
            UP to false,
            RIGHT to false,
            DOWN to false,
            LEFT to false,
        )

        when (type) {
            Type.O -> {
                when (direction) {
                    0 -> {
                        links[DOWN] = true
                    }
                    90 -> {
                        links[LEFT] = true
                    }
                    180 -> {
                        links[UP] = true
                    }
                    270 -> {
                        links[RIGHT] = true
                    }
                }
            }

            Type.I -> {
                when (direction) {
                    0, 180 -> {
                        links[DOWN] = true
                        links[UP] = true
                    }
                    90, 270 -> {
                        links[RIGHT] = true
                        links[LEFT] = true
                    }
                }
            }
            Type.L -> {
                when (direction) {
                    0 -> {
                        links[RIGHT] = true
                        links[DOWN] = true
                    }
                    90 -> {
                        links[DOWN] = true
                        links[LEFT] = true
                    }
                    180 -> {
                        links[LEFT] = true
                        links[UP] = true
                    }
                    270 -> {
                        links[UP] = true
                        links[RIGHT] = true
                    }
                }
            }
            Type.T -> {
                when (direction) {
                    0 -> {
                        links[UP] = true
                        links[RIGHT] = true
                        links[DOWN] = true
                    }
                    90 -> {
                        links[RIGHT] = true
                        links[DOWN] = true
                        links[LEFT] = true
                    }
                    180 -> {
                        links[DOWN] = true
                        links[LEFT] = true
                        links[UP] = true
                    }
                    270 -> {
                        links[LEFT] = true
                        links[UP] = true
                        links[RIGHT] = true
                    }
                }
            }
            Type.NONE -> {}
        }
        return links
    }



    fun getNeighboursConnectionType(side: IntOffset): ConnectionType {
        val neighbourCoordinate = coordinate + side
        if (!Game.validCoordinate(neighbourCoordinate)) {
            return ConnectionType.BARRIER
        }

        val neighbourPiece = Game.pieces[neighbourCoordinate]!!

        if (!neighbourPiece.locked) {
            if (type == Type.O && neighbourPiece.type == Type.O) {
                return ConnectionType.BARRIER
            } else {
                return ConnectionType.FREE
            }
        }

        val neighbourSideStates = neighbourPiece.getLinks()
        if (neighbourSideStates[-side]!!) {
            return ConnectionType.LINK
        } else {
            return ConnectionType.BARRIER
        }
    }



    fun rotateByCW90(instant: Boolean = INSTANT_ROTATION) {
        direction += 90
        if (direction == 360) {
            direction = 0
        }

        if (!instant) {
            if (direction == 0) {
                rotation = -90
            }
//            get_tree().create_tween().tween_property(
//                $symbol,
//                "rotation_degrees",
//                direction,
//                ANIMATION_SPEED
//            )
        }

//        if Game.playerPlaying:
//        History.add_action(self, History.ActionType.rotateCW90)
//        rotated.emit()
    }

    fun rotateByCCW90(instant: Boolean = INSTANT_ROTATION) {
        direction -= 90
        if (direction == -90) {
            direction = 270
        }

        if (!instant) {
            if (direction == 270) {
                rotation = 360
            }
//            get_tree().create_tween().tween_property(
//                $symbol,
//                "rotation_degrees",
//                direction,
//                ANIMATION_SPEED
//            )
        }

//        if Game.playerPlaying:
//        History.add_action(self, History.ActionType.rotateCCW90)
//        rotated.emit()
    }

    fun rotateBy180(instant: Boolean = INSTANT_ROTATION) {
        direction += 180
        if (direction == 360) {
            direction = 0
        } else if (direction == 450) {
            direction = 90
        }

        if (!instant) {
            if (direction == 0) {
                rotation = -180
            } else if (direction == 90) {
                rotation = -90
            }
//            get_tree().create_tween().tween_property(
//                $symbol,
//                "rotation_degrees",
//                direction,
//                ANIMATION_SPEED
//            )
        }

//        if Game.playerPlaying:
//        History.add_action(self, History.ActionType.rotate180)
//        rotated.emit()
    }


    fun rotateTo0(instant: Boolean = INSTANT_ROTATION) {
        when (direction) {
            0 -> {

            }
            90 -> {
                rotateByCCW90(instant)
            }
            180 -> {
                rotateBy180(instant)
            }
            270 -> {
                rotateByCW90(instant)
            }
        }
    }

    fun rotateTo90(instant: Boolean = INSTANT_ROTATION) {
        when (direction) {
            0 -> {
                rotateByCW90(instant)
            }
            90 -> {

            }
            180 -> {
                rotateByCCW90(instant)
            }
            270 -> {
                rotateBy180(instant)
            }
        }
    }

    fun rotateTo180(instant: Boolean = INSTANT_ROTATION) {
        when (direction) {
            0 -> {
                rotateBy180(instant)
            }
            90 -> {
                rotateByCW90(instant)
            }
            180 -> {

            }
            270 -> {
                rotateByCCW90(instant)
            }
        }
    }

    fun rotateTo270(instant: Boolean = INSTANT_ROTATION) {
        when (direction) {
            0 -> {
                rotateByCCW90(instant)
            }
            90 -> {
                rotateBy180(instant)
            }
            180 -> {
                rotateByCW90(instant)
            }
            270 -> {

            }
        }
    }



//    fun printInfo():
//    print("TYPE: ", Type.find_key(type))
//    print("COORDINATE: ", coordinate)
//    print("DIRECTION: ", direction)
//    print("ACTIVE: ", active)
//    print("SOURCE pieces:")
//    for piece in source_pieces:
//    print("--- ", piece.coordinate)
//    print("LINKED pieces:")
//    for piece in linked_pieces:
//    print("--- ", piece.coordinate)


}