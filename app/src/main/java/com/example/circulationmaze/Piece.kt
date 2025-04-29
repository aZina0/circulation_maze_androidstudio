package com.example.circulationmaze

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.IntOffset


private val UP = IntOffset(0, 1)
private val RIGHT = IntOffset(1, 0)
private val DOWN = IntOffset(0, -1)
private val LEFT = IntOffset(-1, 0)
private val SIDES = arrayOf(UP, RIGHT, DOWN, LEFT)

//private val BACKGROUND_COLOR = Color()

class Piece(coordinate: IntOffset, type: Type) {

    enum class Type {O, I, L, T, NONE}

    companion object {
//        val DEFAULT_COLOR: Color = Color.getColor("#515151")
        const val BASE_SIZE = 64.0F
        var animationSpeed = 0.1F
        var scale = 1f

        var shuffledSides = arrayOf(UP, RIGHT, DOWN, LEFT)
    }

    var locked = false
//        set(value) {
//            setLock(value)
//        }
    var highlighted = false
//        set(value) {
//            setHighlight(value)
//        }
    var active = false
//        set(value) {
//            setActive(value)
//        }
    var flashing = false
    var isRootPiece = false

    val coordinate = coordinate
    var type = type
        set(value) {
            field = value
            triggerRedraw()
        }

    var direction = 0
//    var looped_counter := 0 : set = setLoopedCounter
    var position = Offset(0f, 0f)
    var rotation = 0f
        set(value) {
            field = value
            triggerRedraw()
        }
    var linked_pieces: Array<Piece> = emptyArray()
    var source_pieces: Array<Piece> = emptyArray()

    var redrawTrigger by mutableStateOf(false)

    init {
        position = Offset(
            1f + (scale * BASE_SIZE + 1f) * coordinate.x,
            1f + (scale * BASE_SIZE + 1f) * coordinate.y,
        )
    }

    fun draw(drawScope: DrawScope) {
        Log.d("TEST", "redrawn " + position)
        with(drawScope) {
            redrawTrigger
            drawRect(Color.Gray, topLeft = position, size = Size(80f, 80f))
            rotate(degrees = rotation, pivot = position + Offset(80f, 80f) / 2f) {
                drawRect(Color.Blue, topLeft = position, size = Size(80f, 80f))
            }
        }
    }

    fun triggerRedraw() {
        redrawTrigger = !redrawTrigger
    }


//    fun _to_string(): String {
//        return "pieceAT({0},{1})".format([coordinate.x, coordinate.y])
//    }


    fun changeType(pieceType: Type) {
        type = pieceType
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
//            var relative_coordinate: IntOffset = source_piece.coordinate - coordinate
////            when (relative_coordinate) {
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
//        if (isRootPiece and !value) {
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



//    fun swapType(connectionTypes: MutableMap<IntOffset, String> = mutableMapOf()) {
//        var linkCount: Int = 0
//        var barrierCount: Int = 0
//
//        for (side: IntOffset in SIDES) {
//            var connectionType: String
//            if (side in connectionTypes) {
//                connectionType = connectionTypes[side].toString()
//            } else {
//                connectionType = getNeighboursConnectionType(side)
//                connectionTypes[side] = connectionType
//            }
//
//            if (connectionType == "barrier") {
//                barrierCount += 1
//            } else if (connectionType == "link") {
//                linkCount += 1
//            }
//        }
//
//
//        if (
//            (
//                sameConnectionType(connectionTypes, "adjacent", "barrier") &&
//                barrierCount == 2
//            )
//            ||
//            (
//                sameConnectionType(connectionTypes, "adjacent", "link") &&
//                linkCount == 2
//            )
//        ) {
//            changeType(Type.L)
//
//        } else if (
//            (
//                sameConnectionType(connectionTypes, "across", "barrier") &&
//                barrierCount == 2
//            )
//            ||
//            (
//                sameConnectionType(connectionTypes, "across", "link") &&
//                linkCount == 2
//            )
//        ) {
//            changeType(Type.I)
//
//        } else if (linkCount == 1 || barrierCount == 3) {
//            changeType(Type.O)
//
//        } else if (linkCount == 3 || barrierCount == 1) {
//            changeType(Type.T)
//        }
//    }





//    fun solveAndSpread(
//    connectionTypes: Dictionary[IntOffset, StringName] = {},
//    random_choice:= false
//    ): Boolean:
//
//    var solved:= solve(connectionTypes, random_choice)
//    if solved:
//    locked = true
//    for neighbour in getNeighbours():
//    if not neighbour.locked:
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
//    if connectionType == &"barrier":
//    barrierCount += 1
//    elif connectionType == &"link":
//    linkCount += 1
//
//    link[side] = connectionType == &"link"
//    barrier[side] = connectionType == &"barrier"
//
//
//    match type:
//    Type.O:
//    if linkCount == 1:
//    if link[DOWN]:
//    rotateTo0()
//    return true
//    elif link[LEFT]:
//    rotateTo90()
//    return true
//    elif link[UP]:
//    rotateTo180()
//    return true
//    elif link[RIGHT]:
//    rotateTo270()
//    return true
//
//    elif barrierCount == 3:
//    if barrier[RIGHT] and barrier[UP] and barrier[LEFT]:
//    rotateTo0()
//    return true
//    elif barrier[UP] and barrier[RIGHT] and barrier[DOWN]:
//    rotateTo90()
//    return true
//    elif barrier[DOWN] and barrier[RIGHT] and barrier[LEFT]:
//    rotateTo180()
//    return true
//    elif barrier[UP] and barrier[LEFT] and barrier[DOWN]:
//    rotateTo270()
//    return true
//
//
//    Piece.Type.L:
//    if (
//    (link[RIGHT] and link[LEFT]) or (link[UP] and link[DOWN]) or
//    (barrier[RIGHT] and barrier[LEFT]) or (barrier[UP] and barrier[DOWN])
//    ):
//    pass
//
//    elif (
//    (link[RIGHT] and link[DOWN]) or (barrier[LEFT] and barrier[UP]) or
//    (link[RIGHT] and barrier[UP]) or (link[DOWN] and barrier[LEFT])
//    ):
//    rotateTo0()
//    return true
//
//    elif (
//    (link[DOWN] and link[LEFT]) or (barrier[UP] and barrier[RIGHT]) or
//    (link[DOWN] and barrier[RIGHT]) or (link[LEFT] and barrier[UP])
//    ):
//    rotateTo90()
//    return true
//
//    elif (
//    (link[LEFT] and link[UP]) or (barrier[RIGHT] and barrier[DOWN]) or
//    (link[LEFT] and barrier[DOWN]) or (link[UP] and barrier[RIGHT])
//    ):
//    rotateTo180()
//    return true
//
//    elif (
//    (link[UP] and link[RIGHT]) or (barrier[DOWN] and barrier[LEFT]) or
//    (link[UP] and barrier[LEFT]) or (link[RIGHT] and barrier[DOWN])
//    ):
//    rotateTo270()
//    return true
//
//    elif random_choice:
//    if link[DOWN] or barrier[UP]:
//    if randf() >= 0.5:
//    rotateTo0()
//    return true
//    else:
//    rotateTo90()
//    return true
//    elif link[LEFT] or barrier[RIGHT]:
//    if randf() >= 0.5:
//    rotateTo90()
//    return true
//    else:
//    rotateTo180()
//    return true
//
//    elif link[UP] or barrier[DOWN]:
//    if randf() >= 0.5:
//    rotateTo180()
//    return true
//    else:
//    rotateTo270()
//    return true
//    elif link[RIGHT] or barrier[LEFT]:
//    if randf() >= 0.5:
//    rotateTo270()
//    return true
//    else:
//    rotateTo0()
//    return true
//
//    Piece.Type.I:
//    if barrier[LEFT] or barrier[RIGHT] or link[UP] or link[DOWN]:
//    if link[LEFT] or link[RIGHT] or barrier[UP] or barrier[DOWN]:
//    pass
//    else:
//    rotateTo0()
//    return true
//    elif barrier[UP] or barrier[DOWN] or link[LEFT] or link[RIGHT]:
//    if link[UP] or link[DOWN] or barrier[LEFT] or barrier[RIGHT]:
//    pass
//    else:
//    rotateTo90()
//    return true
//
//    Piece.Type.T:
//    if barrierCount > 1 or linkCount == 4:
//    pass
//    elif barrier[LEFT] or (link[UP] and link[RIGHT] and link[DOWN]):
//    rotateTo0()
//    return true
//    elif barrier[UP] or (link[RIGHT] and link[DOWN] and link[LEFT]):
//    rotateTo90()
//    return true
//    elif barrier[RIGHT] or (link[DOWN] and link[LEFT] and link[UP]):
//    rotateTo180()
//    return true
//    elif barrier[DOWN] or (link[LEFT] and link[UP] and link[RIGHT]):
//    rotateTo270()
//    return true
//    elif random_choice:
//    if link[UP] and link[DOWN]:
//    if randf() > 0.5:
//    rotateTo0()
//    return true
//    else:
//    rotateTo180()
//    return true
//    elif link[LEFT] and link[RIGHT]:
//    if randf() > 0.5:
//    rotateTo90()
//    return true
//    else:
//    rotateTo270()
//    return true
//    elif link[RIGHT] and link[DOWN]:
//    if randf() > 0.5:
//    rotateTo0()
//    return true
//    else:
//    rotateTo90()
//    return true
//    elif link[DOWN] and link[LEFT]:
//    if randf() > 0.5:
//    rotateTo90()
//    return true
//    else:
//    rotateTo180()
//    return true
//    elif link[LEFT] and link[UP]:
//    if randf() > 0.5:
//    rotateTo180()
//    return true
//    else:
//    rotateTo270()
//    return true
//    elif link[UP] and link[RIGHT]:
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
//    if not Game.validCoordinate(checkCoordinate):
//    break
//
//    var checkPiece: Piece = Game.pieces[checkCoordinate]
//    if checkPiece.type == Type.I:
//    iPieceLine.push_front(checkPiece)
//    elif checkPiece.type == Type.O:
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
//    if not Game.validCoordinate(checkCoordinate):
//    break
//
//    var checkPiece: Piece = Game.pieces[checkCoordinate]
//    if checkPiece.type == Type.I:
//    iPieceLine.append(checkPiece)
//    elif checkPiece.type == Type.O:
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
//    elif orientation == [LEFT, RIGHT]:
//    for iPiece in iPieceLine:
//    iPiece.rotateTo0()
//    iPiece.locked = true
//    return true
//
//
//    return false



//    fun getNeighbours(): Array[Piece]:
//    var neighbourPieces: Array[Piece] = []
//    for side in SIDES:
//    if Game.validCoordinate(coordinate + side):
//    neighbourPieces.append(Game.pieces[coordinate + side])
//
//    return neighbourPieces

//    fun getSidesWithNeighbours(): Dictionary[IntOffset, Piece]:
//    var neighbourPieces: Dictionary[IntOffset, Piece] = {}
//    for side in SIDES:
//    if Game.validCoordinate(coordinate + side):
//    neighbourPieces[side] = Game.pieces[coordinate + side]
//
//    return neighbourPieces


//    fun getConnectedNeighbours(): Array[Piece]:
//    var neighbourPieces:= getNeighbours()
//    var pieceIndex:= neighbourPieces.size() - 1
//    while pieceIndex >= 0:
//    if not self.connected(neighbourPieces[pieceIndex]):
//    neighbourPieces.remove_at(pieceIndex)
//    pieceIndex -= 1
//
//    return neighbourPieces

//    fun getSidesWithConnectedNeighbours(): Dictionary[IntOffset, Piece]:
//    var sidesWithNeighbours:= getSidesWithNeighbours()
//
//    for side: IntOffset in sidesWithNeighbours.keys():
//    if not connected(sidesWithNeighbours[side]):
//    sidesWithNeighbours.erase(side)
//
//    return sidesWithNeighbours


//    fun connected(neighbourPiece: Piece): Boolean:
//    var pieceLinks:= getLinks()
//    var neighbourPieceLinks:= neighbourPiece.getLinks()
//
//    var relative_coordinate:= neighbourPiece.coordinate - coordinate
//    match relative_coordinate:
//    UP:
//    if pieceLinks[UP] and neighbourPieceLinks[DOWN]:
//    return true
//    RIGHT:
//    if pieceLinks[RIGHT] and neighbourPieceLinks[LEFT]:
//    return true
//    DOWN:
//    if pieceLinks[DOWN] and neighbourPieceLinks[UP]:
//    return true
//    LEFT:
//    if pieceLinks[LEFT] and neighbourPieceLinks[RIGHT]:
//    return true
//    return false



//    fun getLinks(): Dictionary[IntOffset, Boolean]:
//    var links: Dictionary[IntOffset, Boolean] = {
//        UP : false,
//        RIGHT : false,
//        DOWN : false,
//        LEFT : false,
//    }
//
//    match type:
//    Type.O:
//    match direction:
//    0:
//    links[DOWN] = true
//    90:
//    links[LEFT] = true
//    180:
//    links[UP] = true
//    270:
//    links[RIGHT] = true
//    Type.I:
//    match direction:
//    0, 180:
//    links[DOWN] = true
//    links[UP] = true
//    90,	270:
//    links[RIGHT] = true
//    links[LEFT] = true
//    Type.L:
//    match direction:
//    0:
//    links[RIGHT] = true
//    links[DOWN] = true
//    90:
//    links[DOWN] = true
//    links[LEFT] = true
//    180:
//    links[LEFT] = true
//    links[UP] = true
//    270:
//    links[UP] = true
//    links[RIGHT] = true
//    Type.T:
//    match direction:
//    0:
//    links[UP] = true
//    links[RIGHT] = true
//    links[DOWN] = true
//    90:
//    links[RIGHT] = true
//    links[DOWN] = true
//    links[LEFT] = true
//    180:
//    links[DOWN] = true
//    links[LEFT] = true
//    links[UP] = true
//    270:
//    links[LEFT] = true
//    links[UP] = true
//    links[RIGHT] = true
//
//    return links



//    fun getNeighboursConnectionType(side: IntOffset): StringName:
//    var neighbourCoordinate:= coordinate + side
//    if not Game.validCoordinate(neighbourCoordinate):
//    return &"barrier"
//
//    var neighbourPiece: Piece = Game.pieces[neighbourCoordinate]
//
//    if not neighbourPiece.locked:
//    if self.type == Piece.Type.O and neighbourPiece.type == Piece.Type.O:
//    return &"barrier"
//    else:
//    return &"free"
//
//    var neighbourSideStates:= neighbourPiece.getLinks()
//    if neighbourSideStates[-side]:
//    return &"link"
//    else:
//    return &"barrier"



//    fun rotateByCW90(instant:= true):
//    instant = false
//    direction += 90
//    if direction == 360:
//    direction = 0
//
//    if not instant:
//    if direction == 0:
//    $symbol.rotation_degrees = -90
//    get_tree().create_tween().tween_property(
//    $symbol,
//    "rotation_degrees",
//    direction,
//    ANIMATION_SPEED
//    )
//
//    if Game.playerPlaying:
//    History.add_action(self, History.ActionType.rotateCW90)
//    rotated.emit()
//
//    fun rotateByCCW90(instant:= true):
//    instant = false
//    direction -= 90
//    if direction == -90:
//    direction = 270
//
//    if not instant:
//    if direction == 270:
//    $symbol.rotation_degrees = 360
//    get_tree().create_tween().tween_property(
//    $symbol,
//    "rotation_degrees",
//    direction,
//    ANIMATION_SPEED
//    )
//
//    if Game.playerPlaying:
//    History.add_action(self, History.ActionType.rotateCCW90)
//    rotated.emit()
//
//    fun rotateBy180(instant:= true):
//    instant = false
//    direction += 180
//    if direction == 360:
//    direction = 0
//    elif direction == 450:
//    direction = 90
//
//    if not instant:
//    if direction == 0:
//    $symbol.rotation_degrees = -180
//    elif direction == 90:
//    $symbol.rotation_degrees = -90
//    get_tree().create_tween().tween_property(
//    $symbol,
//    "rotation_degrees",
//    direction,
//    ANIMATION_SPEED
//    )
//
//    if Game.playerPlaying:
//    History.add_action(self, History.ActionType.rotate180)
//    rotated.emit()


//    fun rotateTo0(instant: Boolean = true) {
//        if (direction == 0) {
//            ;
//        } else if (direction == 90) {
//            rotateByCCW90(instant)
//        } else if (direction == 180) {
//            rotateBy180(instant)
//        } else if (direction == 270) {
//            rotateByCW90(instant)
//        }
//    }
//
//    fun rotateTo90(instant: Boolean = true) {
//        if (direction == 0) {
//            rotateByCW90(instant)
//        } else if (direction == 90) {
//            ;
//        } else if (direction == 180) {
//            rotateByCCW90(instant)
//        } else if (direction == 270) {
//            rotateBy180(instant)
//        }
//    }
//
//    fun rotateTo180(instant: Boolean = true) {
//        if (direction == 0) {
//            rotateBy180(instant)
//        } else if (direction == 90) {
//            rotateByCW90(instant)
//        } else if (direction == 180) {
//            ;
//        } else if (direction == 270) {
//            rotateByCCW90(instant)
//        }
//    }
//
//    fun rotateTo270(instant: Boolean = true) {
//        if (direction == 0) {
//            rotateByCCW90(instant)
//        } else if (direction == 90) {
//            rotateBy180(instant)
//        } else if (direction == 180) {
//            rotateByCW90(instant)
//        } else if (direction == 270) {
//            ;
//        }
//    }



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



//    static fun sameConnectionType(
//    connectionTypes : Dictionary,
//    relativePosition : StringName,
//    typeToCheck : StringName
//    ): Boolean:
//
//    if relativePosition == &"adjacent":
//    if (
//    connectionTypes[LEFT] == connectionTypes[UP] and
//    connectionTypes[LEFT] == typeToCheck
//    ):
//    return true
//
//    elif (
//    connectionTypes[UP] == connectionTypes[RIGHT] and
//    connectionTypes[UP] == typeToCheck
//    ):
//    return true
//
//    elif (
//    connectionTypes[RIGHT] == connectionTypes[DOWN] and
//    connectionTypes[RIGHT] == typeToCheck
//    ):
//    return true
//
//    elif (
//    connectionTypes[DOWN] == connectionTypes[LEFT] and
//    connectionTypes[DOWN] == typeToCheck
//    ):
//    return true
//
//    return false
//
//
//    elif relativePosition == &"across":
//    if (
//    connectionTypes[LEFT] == connectionTypes[RIGHT] and
//    connectionTypes[LEFT] == typeToCheck
//    ):
//    return true
//
//
//    return false
//
//    else:
//    assert(false, "Invalid type of relativePosition: '{0}'".format([relativePosition]))
//    return false
//
//
//    static fun getShuffledSides(): Array[IntOffset]:
//    shuffledSides.shuffle()
//    return shuffledSides
//
//
//    static fun resetShuffledSides():
//    shuffledSides = [UP, RIGHT, DOWN, LEFT]

}