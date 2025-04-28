package com.example.circulationmaze


class Vector2i(var x: Int, var y: Int) {

    companion object {
        val UP: Vector2i = Vector2i(0, 1)
        val RIGHT: Vector2i = Vector2i(1, 0)
        val DOWN: Vector2i = Vector2i(0, -1)
        val LEFT: Vector2i = Vector2i(-1, 0)
        val ONE: Vector2i = Vector2i(1, 1)
    }

    override fun equals(other: Any?): Boolean {
        if (other is Vector2i) {
            if (x == other.x && y == other.y) {
                return true
            }
        }
        return false
    }

    override fun hashCode(): Int {
        return 31 * x + y
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
