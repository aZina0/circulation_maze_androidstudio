package com.example.circulationmaze


class Vector2i(x: Int, y: Int) {
    var x: Int = x
    var y: Int = y

    companion object {
        val UP: Vector2i = Vector2i(0, 1)
        val RIGHT: Vector2i = Vector2i(1, 0)
        val DOWN: Vector2i = Vector2i(0, -1)
        val LEFT: Vector2i = Vector2i(-1, 0)
        val ONE: Vector2i = Vector2i(1, 1)
    }

    operator fun plus(otherVector: Vector2i): Vector2i {
        return Vector2i(this.x + otherVector.x, this.y + otherVector.y)
    }

    operator fun minus(otherVector: Vector2i): Vector2i {
        return Vector2i(this.x + otherVector.x, this.y + otherVector.y)
    }

    operator fun div(otherInteger: Int): Vector2i {
        return Vector2i(x / otherInteger, y / otherInteger)
    }
}
