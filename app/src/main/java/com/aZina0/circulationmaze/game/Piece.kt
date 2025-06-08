package com.aZina0.circulationmaze.game

import android.animation.ValueAnimator
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.aZina0.circulationmaze.Global
import com.aZina0.circulationmaze.R
import kotlinx.coroutines.delay


val UP = IntOffset(0, -1)
val RIGHT = IntOffset(1, 0)
val DOWN = IntOffset(0, 1)
val LEFT = IntOffset(-1, 0)
val SIDES = arrayOf(UP, RIGHT, DOWN, LEFT)

private val FREE = Piece.ConnectionType.FREE
private val BARRIER = Piece.ConnectionType.BARRIER
private val LINK = Piece.ConnectionType.LINK

private val DEFAULT_BACKGROUND_COLOR = Color(0xFF000000)
private val LOCKED_BACKGROUND_COLOR = Color(0xFF252525)
private val DEFAULT_PIECE_COLOR = Color(0xFF515151)
private val ACTIVE_PIECE_COLOR = Color(0xFF008700)
private val ROOT_PIECE_COLOR = Color(0xFFFFD700)

private const val ROTATION_DURATION = 200
private const val INSTANT_ROTATION = false


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PieceComposable(modifier: Modifier, piece: Piece) {
    var backgroundColor = DEFAULT_BACKGROUND_COLOR
    if (piece.locked) {
        backgroundColor = LOCKED_BACKGROUND_COLOR
    }

    var pieceColor = DEFAULT_PIECE_COLOR
    if (piece.active) {
        pieceColor = ACTIVE_PIECE_COLOR
    }
    if (piece == Game.rootPiece) {
        pieceColor = ROOT_PIECE_COLOR
    }

    if (piece.golden) {
        pieceColor = ROOT_PIECE_COLOR
    }

    Global.redrawAmount++
//    Global.print("%s redrawn. (%s total)".format(piece, Global.redrawAmount))
    piece.triggerRedraw

    Box (
        modifier = modifier
            .size((Piece.BASE_SIZE * Piece.scale).dp)
            .background(backgroundColor)
            .combinedClickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = { piece.onClicked() },
                onLongClick = { piece.onLongClicked() },
            )
    ) {
        if (piece.highlighted) {
            Image(
                painter = painterResource(id = R.drawable.highlight),
                contentDescription = "img",
                modifier = Modifier
                    .size((Piece.BASE_SIZE * Piece.scale).dp)
            )
        }
        Image(
            painter = Piece.images[piece.type]!!,
            contentDescription = "img",
            colorFilter = ColorFilter.tint(pieceColor),
            modifier = Modifier
                .size((Piece.BASE_SIZE * Piece.scale).dp)
                .rotate(piece.rotation.toFloat())
        )
        if (piece.upArrow) {
            Image(
                painter = painterResource(id = R.drawable.go),
                contentDescription = "img",
                modifier = Modifier
                    .size((Piece.BASE_SIZE * Piece.scale).dp * 0.4f)
                    .offset(x = 10.dp, y = 0.dp)
                    .rotate(90f)
            )
        }
        if (piece.rightArrow) {
            Image(
                painter = painterResource(id = R.drawable.go),
                contentDescription = "img",
                modifier = Modifier
                    .size((Piece.BASE_SIZE * Piece.scale).dp * 0.4f)
                    .offset(x = 20.dp, y = 10.dp)
                    .rotate(180f)
            )
        }
        if (piece.downArrow) {
            Image(
                painter = painterResource(id = R.drawable.go),
                contentDescription = "img",
                modifier = Modifier
                    .size((Piece.BASE_SIZE * Piece.scale).dp * 0.4f)
                    .offset(x = 10.dp, y = 20.dp)
                    .rotate(270f)
            )
        }
        if (piece.leftArrow) {
            Image(
                painter = painterResource(id = R.drawable.go),
                contentDescription = "img",
                modifier = Modifier
                    .size((Piece.BASE_SIZE * Piece.scale).dp * 0.4f)
                    .offset(x = 0.dp, y = 10.dp)
                    .rotate(0f)
            )
        }
    }
}


class Piece(val coordinate: IntOffset, val position: Offset, type: Type) {

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
        }
    var linkedPieces = mutableListOf<Piece>()
    var sourcePieces = mutableListOf<Piece>()

    var triggerRedraw by mutableStateOf(false)

    var upArrow by mutableStateOf(false)
    var rightArrow by mutableStateOf(false)
    var downArrow by mutableStateOf(false)
    var leftArrow by mutableStateOf(false)

    var golden by mutableStateOf(false)

    companion object {
//        val DEFAULT_COLOR: Color = Color.getColor("#515151")
        const val BASE_SIZE = 64.0F
        var animationSpeed = 0.1F
        var scale = 1f

        @JvmField
        var shuffledSides = arrayOf(UP, RIGHT, DOWN, LEFT)

        var images = emptyMap<Type, Painter>()

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
            shuffledSides.shuffle(Game.deterministicRandom)
            return shuffledSides.toList()
        }


        fun resetShuffledSides() {
            shuffledSides = arrayOf(UP, RIGHT, DOWN, LEFT)
        }
    }

    private fun triggerRedraw() {
        triggerRedraw = !triggerRedraw
    }


    override fun toString(): String {
        return "pieceAT(%d,%d)".format(coordinate.x, coordinate.y)
    }


    fun changeType(pieceType: Type) {
        type = pieceType
        triggerRedraw()
    }

    fun makeGolden() {
        golden = true
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


    fun updateSourceArrows() {
//        upArrow = false
//        rightArrow = false
//        downArrow = false
//        leftArrow = false
//
//        for (sourcePiece in sourcePieces) {
//            var relativeCoordinate = sourcePiece.coordinate - coordinate
//            when (relativeCoordinate) {
//                UP -> upArrow = true
//                RIGHT -> rightArrow = true
//                DOWN -> downArrow = true
//                LEFT -> leftArrow = true
//            }
//        }

//        Global.print((sourcePieces.size != sourcePieces.toSet().size).toString())
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
    }


    fun highlight() {
        highlighted = true
        triggerRedraw()
    }

    fun unhighlight() {
        highlighted = false
        triggerRedraw()
    }
//    fun setHighlight(value: Boolean) {
//        highlighted = value
////        $highlight.visible = value
////        if value:
////            $symbol.modulate = Color.GREEN
////        else:
////            if $symbol.modulate != Color.RED:
////                $symbol.modulate = DEFAULT_COLOR
//    }

    fun lock() {
        locked = true
        triggerRedraw()
    }

    fun unlock() {
        locked = false
        triggerRedraw()
    }

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

    fun activate() {
        active = true
        triggerRedraw()
    }

    fun deactivate() {
        if (this == Game.rootPiece) return
        active = false
        triggerRedraw()
    }

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


    suspend fun flash(customColor: Color = Color.Magenta, count: Int = 1) {
        if (flashing) return
        flashing = true

        for (i in 0 until count) {
            highlight()
            delay(250)
            unhighlight()
            delay(250)
        }

        flashing = false
    }



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

            if (connectionType == BARRIER) {
                barrierCount += 1
            } else if (connectionType == LINK) {
                linkCount += 1
            }
        }


        if (
            (
                sameConnectionType(connections, "adjacent", BARRIER) &&
                barrierCount == 2
            )
            ||
            (
                sameConnectionType(connections, "adjacent", LINK) &&
                linkCount == 2
            )
        ) {
            changeType(Type.L)

        } else if (
            (
                sameConnectionType(connections, "across", BARRIER) &&
                barrierCount == 2
            )
            ||
            (
                sameConnectionType(connections, "across", LINK) &&
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





    fun solveAndSpread(
        connectionTypes: Map<IntOffset, ConnectionType> = mapOf(),
        randomChoice: Boolean = false
    ): Boolean {
        val solved = solve(connectionTypes, randomChoice)
        if (solved) {
            lock()
            for (neighbour in getNeighbours()) {
                if (!neighbour.locked) {
                    neighbour.solveAndSpread()
                }
            }
        }
        return solved
    }


    fun solve(
        connectionTypes: Map<IntOffset, ConnectionType> = emptyMap(),
        randomChoice: Boolean = false
    ): Boolean {
        var linkCount = 0
        var barrierCount = 0
        val link = mutableMapOf<IntOffset, Boolean>()
        val barrier = mutableMapOf<IntOffset, Boolean>()

        for (side in SIDES) {
            var connectionType: ConnectionType
            if (connectionTypes.contains(side)) {
                connectionType = connectionTypes[side]!!
            } else {
                connectionType = getNeighboursConnectionType(side)
            }

            if (connectionType == BARRIER) {
                barrierCount += 1
            } else if (connectionType == LINK) {
                linkCount += 1
            }

            link[side] = connectionType == LINK
            barrier[side] = connectionType == BARRIER
        }


        when (type) {
            Type.O -> {
                if (linkCount == 1) {
                    if (link[DOWN]!!) {
                        rotateTo0()
                        return true
                    } else if (link[LEFT]!!) {
                        rotateTo90()
                        return true
                    } else if (link[UP]!!) {
                        rotateTo180()
                        return true
                    } else if (link[RIGHT]!!) {
                        rotateTo270()
                        return true
                    }
                } else if (barrierCount == 3) {
                    if (barrier[RIGHT]!! && barrier[UP]!! && barrier[LEFT]!!) {
                        rotateTo0()
                        return true
                    } else if (barrier[UP]!! && barrier[RIGHT]!! && barrier[DOWN]!!) {
                        rotateTo90()
                        return true
                    } else if (barrier[DOWN]!! && barrier[RIGHT]!! && barrier[LEFT]!!) {
                        rotateTo180()
                        return true
                    } else if (barrier[UP]!! && barrier[LEFT]!! && barrier[DOWN]!!) {
                        rotateTo270()
                        return true
                    }
                }
            }

            Type.L -> {
                if (
                    (link[RIGHT]!! && link[LEFT]!!) || (link[UP]!! && link[DOWN]!!) ||
                    (barrier[RIGHT]!! && barrier[LEFT]!!) || (barrier[UP]!! && barrier[DOWN]!!)
                ) {

                } else if (
                    (link[RIGHT]!! && link[DOWN]!!) || (barrier[LEFT]!! && barrier[UP]!!) ||
                    (link[RIGHT]!! && barrier[UP]!!) || (link[DOWN]!! && barrier[LEFT]!!)
                ) {
                    rotateTo0()
                    return true

                } else if (
                    (link[DOWN]!! && link[LEFT]!!) || (barrier[UP]!! && barrier[RIGHT]!!) ||
                    (link[DOWN]!! && barrier[RIGHT]!!) || (link[LEFT]!! && barrier[UP]!!)
                ) {
                    rotateTo90()
                    return true

                } else if (
                    (link[LEFT]!! && link[UP]!!) || (barrier[RIGHT]!! && barrier[DOWN]!!) ||
                    (link[LEFT]!! && barrier[DOWN]!!) || (link[UP]!! && barrier[RIGHT]!!)
                ) {
                    rotateTo180()
                    return true

                } else if (
                    (link[UP]!! && link[RIGHT]!!) || (barrier[DOWN]!! && barrier[LEFT]!!) ||
                    (link[UP]!! && barrier[LEFT]!!) || (link[RIGHT]!! && barrier[DOWN]!!)
                ) {
                    rotateTo270()
                    return true

                } else if (randomChoice) {
                    if (link[DOWN]!! || barrier[UP]!!) {
                        if (Game.deterministicRandom.nextFloat() >= 0.5) {
                            rotateTo0()
                            return true
                        } else {
                            rotateTo90()
                            return true
                        }
                    } else if (link[LEFT]!! || barrier[RIGHT]!!) {
                        if (Game.deterministicRandom.nextFloat() >= 0.5) {
                            rotateTo90()
                            return true
                        } else {
                            rotateTo180()
                            return true
                        }
                    } else if (link[UP]!! || barrier[DOWN]!!) {
                        if (Game.deterministicRandom.nextFloat() >= 0.5) {
                            rotateTo180()
                            return true
                        } else {
                            rotateTo270()
                            return true
                        }
                    }
                    else if (link[RIGHT]!! || barrier[LEFT]!!) {
                        if (Game.deterministicRandom.nextFloat() >= 0.5) {
                            rotateTo270()
                            return true
                        } else {
                            rotateTo0()
                            return true
                        }
                    }
                }
            }

            Type.I -> {
                if (barrier[LEFT]!! || barrier[RIGHT]!! || link[UP]!! || link[DOWN]!!) {
                    if (link[LEFT]!! || link[RIGHT]!! || barrier[UP]!! || barrier[DOWN]!!) {

                    } else {
                        rotateTo0()
                        return true
                    }
                } else if (barrier[UP]!! || barrier[DOWN]!! || link[LEFT]!! || link[RIGHT]!!) {
                    if (link[UP]!! || link[DOWN]!! || barrier[LEFT]!! || barrier[RIGHT]!!) {

                    } else {
                        rotateTo90()
                        return true
                    }
                }
            }

            Type.T -> {
                if (barrierCount > 1 || linkCount == 4) {

                } else if (barrier[LEFT]!! || (link[UP]!! && link[RIGHT]!! && link[DOWN]!!)) {
                    rotateTo0()
                    return true
                } else if (barrier[UP]!! || (link[RIGHT]!! && link[DOWN]!! && link[LEFT]!!)) {
                    rotateTo90()
                    return true
                } else if (barrier[RIGHT]!! || (link[DOWN]!! && link[LEFT]!! && link[UP]!!)) {
                    rotateTo180()
                    return true
                } else if (barrier[DOWN]!! || (link[LEFT]!! && link[UP]!! && link[RIGHT]!!)) {
                    rotateTo270()
                    return true
                } else if (randomChoice) {
                    if (link[UP]!! && link[DOWN]!!) {
                        if (Game.deterministicRandom.nextFloat() > 0.5) {
                            rotateTo0()
                            return true
                        } else {
                            rotateTo180()
                            return true
                        }
                    } else if (link[LEFT]!! && link[RIGHT]!!) {
                        if (Game.deterministicRandom.nextFloat() > 0.5) {
                            rotateTo90()
                            return true
                        } else {
                            rotateTo270()
                            return true
                        }
                    } else if (link[RIGHT]!! && link[DOWN]!!) {
                        if (Game.deterministicRandom.nextFloat() > 0.5) {
                            rotateTo0()
                            return true
                        } else {
                            rotateTo90()
                            return true
                        }
                    } else if (link[DOWN]!! && link[LEFT]!!) {
                        if (Game.deterministicRandom.nextFloat() > 0.5) {
                            rotateTo90()
                            return true
                        } else {
                            rotateTo180()
                            return true
                        }
                    } else if (link[LEFT]!! && link[UP]!!) {
                        if (Game.deterministicRandom.nextFloat() > 0.5) {
                            rotateTo180()
                            return true
                        } else {
                            rotateTo270()
                            return true
                        }
                    } else if (link[UP]!! && link[RIGHT]!!) {
                        if (Game.deterministicRandom.nextFloat() > 0.5) {
                            rotateTo270()
                            return true
                        } else {
                            rotateTo0()
                            return true
                        }
                    }
                }
            }

            Type.NONE -> {}
        }


        if (type == Type.I) {
            for (orientation in listOf(Pair(UP, DOWN), Pair(LEFT, RIGHT))) {
                val iPieceLine = mutableListOf(this)

                val firstDirection = orientation.first
                val secondDirection = orientation.second
                var sideChecks = 0

                while (true) {
                    val edgePiece = iPieceLine[0]
                    val checkCoordinate = edgePiece.coordinate + firstDirection

                    if (!Game.validCoordinate(checkCoordinate)) {
                        break
                    }

                    val checkPiece = Game.pieces[checkCoordinate]!!
                    if (checkPiece.type == Type.I) {
                        iPieceLine.add(0, checkPiece)
                    } else if (checkPiece.type == Type.O) {
                        sideChecks += 1
                        break
                    } else {
                        break
                    }
                }

                if (sideChecks == 0) {
                    continue
                }

                while (true) {
                    val edgePiece = iPieceLine[iPieceLine.size - 1]
                    val checkCoordinate = edgePiece.coordinate + secondDirection

                    if (!Game.validCoordinate(checkCoordinate)) {
                        break
                    }

                    val checkPiece = Game.pieces[checkCoordinate]!!
                    if (checkPiece.type == Type.I) {
                        iPieceLine.add(checkPiece)
                    } else if (checkPiece.type == Type.O) {
                        sideChecks += 1
                        break
                    } else {
                        break
                    }
                }

                if (sideChecks == 2) {
                    if (orientation == Pair(UP, DOWN)) {
                        for (iPiece in iPieceLine) {
                            iPiece.rotateTo90()
                            iPiece.lock()
                        }
                    } else if (orientation == Pair(LEFT, RIGHT)) {
                        for (iPiece in iPieceLine) {
                            iPiece.rotateTo0()
                            iPiece.lock()
                        }
                    }
                    return true
                }
            }
        }

        return false
    }


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

        for (side in sidesWithNeighbours.keys.toSet()) {
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
            return BARRIER
        }

        val neighbourPiece = Game.pieces[neighbourCoordinate]!!

        if (!neighbourPiece.locked) {
            if (type == Type.O && neighbourPiece.type == Type.O) {
                return BARRIER
            } else {
                return FREE
            }
        }

        val neighbourSideStates = neighbourPiece.getLinks()
        if (neighbourSideStates[-side]!!) {
            return LINK
        } else {
            return BARRIER
        }
    }



    fun rotateByCW90(instant: Boolean = INSTANT_ROTATION) {
        if (locked) return

        if (!instant) {
//            if (direction == 0) {
//                rotation = -90
//            }
            val animator = ValueAnimator.ofInt(direction, direction + 90)
            animator.duration = ROTATION_DURATION.toLong()
            animator.addUpdateListener { animation ->
                triggerRedraw()
                rotation = animation.animatedValue as Int
            }
            animator.start()
        } else {
            rotation += 90
        }

        direction += 90
        if (direction == 360) {
            direction = 0
        }

        if (Game.playerPlaying) {
//            History.add_action(self, History.ActionType.rotateCW90)
        }
//        rotated.emit()
        onRotated()
        triggerRedraw()
    }


    fun rotateByCCW90(instant: Boolean = INSTANT_ROTATION) {
        if (locked) return

        if (!instant) {
//            if (direction == 270) {
//                rotation = 360
//            }
            val animator = ValueAnimator.ofInt(direction, direction - 90)
            animator.duration = ROTATION_DURATION.toLong()
            animator.addUpdateListener { animation ->
                triggerRedraw()
                rotation = animation.animatedValue as Int
            }
            animator.start()
        } else {
            rotation -= 90
        }

        direction -= 90
        if (direction == -90) {
            direction = 270
        }

        if (Game.playerPlaying) {
//            History.add_action(self, History.ActionType.rotateCCW90)
        }
//        rotated.emit()
        onRotated()
        triggerRedraw()
    }

    fun rotateBy180(instant: Boolean = INSTANT_ROTATION) {
        if (locked) return

        if (!instant) {
//            if (direction == 0) {
//                rotation = -180
//            } else if (direction == 90) {
//                rotation = -90
//            }
            val animator = ValueAnimator.ofInt(direction, direction + 180)
            animator.duration = ROTATION_DURATION.toLong()
            animator.addUpdateListener { animation ->
                triggerRedraw()
                rotation = animation.animatedValue as Int
            }
            animator.start()
        } else {
            rotation += 180
        }

        direction += 180
        if (direction == 360) {
            direction = 0
        } else if (direction == 450) {
            direction = 90
        }

        if (Game.playerPlaying) {
//            History.add_action(self, History.ActionType.rotate180)
        }
//        rotated.emit()
        onRotated()
        triggerRedraw()
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


    fun onClicked() {
        rotateByCW90()
    }

    fun onLongClicked() {
        if (!locked) {
            lock()
        } else {
            unlock()
        }
    }

    fun onRotated() {
        if (!Game.playerPlaying) {
            return
        }

//        connectSubgraph(piece)

        val connectedNeighbourPieces = getConnectedNeighbours()
        if (!active) {
            for (neighbourPiece in connectedNeighbourPieces) {
                if (neighbourPiece.active) {
                    sourcePieces.add(neighbourPiece)
                    neighbourPiece.linkedPieces.add(this)
                }
            }
            if (sourcePieces.isNotEmpty()) {
                activate()
                Game.connectSubgraph(this)
            }
        }
        else {
            var sourcePieceIndex = 0
            while (sourcePieceIndex < sourcePieces.size) {
                val sourcePiece = sourcePieces[sourcePieceIndex]
                if (!connected(sourcePiece)) {
                    sourcePiece.linkedPieces.remove(this)
                    sourcePieces.removeAt(sourcePieceIndex)
                } else {
                    sourcePieceIndex += 1
                }
            }

            if (sourcePieces.isEmpty() && this != Game.rootPiece) {
                Game.disconnectSubgraph(this)
            } else {
                for (neighbourPiece in connectedNeighbourPieces) {
                    if (!linkedPieces.contains(neighbourPiece) && !sourcePieces.contains(neighbourPiece)) {
                        linkedPieces.add(neighbourPiece)
                        neighbourPiece.sourcePieces.add(this)
                        Game.connectSubgraph(neighbourPiece)
                    }
                }

                var linkedPieceIndex = 0
                while (linkedPieceIndex < linkedPieces.size) {
                    val linkedPiece = linkedPieces[linkedPieceIndex]
                    if (!connectedNeighbourPieces.contains(linkedPiece)) {
                        linkedPieces.removeAt(linkedPieceIndex)
                        linkedPiece.sourcePieces.remove(this)
                        Game.disconnectSubgraph(linkedPiece)
                    } else {
                        linkedPieceIndex += 1
                    }
                }
            }
        }

        updateSourceArrows()

//        if loopPathingEnabled:
//        	await Loops.traverse(piece)

        Game.checkForBoardSolve()
    }

    fun printInfo() {
        Global.print("========")
        Global.print("%s".format(this))
        Global.print("TYPE: %s".format(type))
        Global.print("DIRECTION: %s".format(direction))
        Global.print("ACTIVE: %s".format(active))
        Global.print("SOURCE pieces:")
        for (piece in sourcePieces) {
            Global.print("---%s".format(piece.coordinate))
        }
        Global.print("LINKED pieces:")
            for (piece in linkedPieces) {
            Global.print("---%s".format(piece.coordinate))
        }
    }


}