package hawka11.robot.disruptor.robot.game

data class Grid(
        var rows: Int = 0,
        var columns: Int = 0
) {
    fun isValidPosition(x: Int, y: Int): Boolean = x < columns && y < rows

    fun canMove(x: Int, y: Int, direction: Direction): Boolean {
        return when (direction) {
            Direction.NORTH -> y < rows - 1
            Direction.EAST -> x < columns - 1
            Direction.SOUTH -> x > 0
            Direction.WEST -> y > 0
            else -> false
        }
    }
}