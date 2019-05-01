package hawka11.robot.disruptor.robot.game

data class Robot(
        var coordinateX: Int = 0,
        var coordinateY: Int = 0,
        var direction: Direction = Direction.NORTH
) {
    fun rotateRight(): Robot {
        direction = direction.rotateRight()
        return this
    }

    fun rotateLeft(): Robot {
        direction = direction.rotateLeft()
        return this
    }

    fun moveOne(): Robot {
        when (direction) {
            Direction.NORTH -> this.coordinateY = this.coordinateY + 1
            Direction.EAST -> this.coordinateX = this.coordinateX + 1
            Direction.SOUTH -> this.coordinateY = this.coordinateY - 1
            Direction.WEST -> this.coordinateX = this.coordinateX - 1
        }
        return this
    }
}

enum class Direction {
    UNKNOWN, NORTH, EAST, SOUTH, WEST;

    fun rotateRight() = when (this) {
        NORTH -> EAST
        EAST -> SOUTH
        SOUTH -> WEST
        WEST -> NORTH
        UNKNOWN -> UNKNOWN
    }

    fun rotateLeft() = when (this) {
        NORTH -> WEST
        EAST -> NORTH
        SOUTH -> EAST
        WEST -> SOUTH
        UNKNOWN -> UNKNOWN
    }

    companion object {
        private val values = Direction.values()

        fun fromIndex(idx: Int): Direction {
            return values[idx]
        }
    }
}