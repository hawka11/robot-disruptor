package hawka11.robot.disruptor.robot.game

import hawka11.robot.disruptor.robot.RobotEvent

fun handleInitType(event: RobotEvent, grid: Grid) {
    grid.columns = event.coordinateX
    grid.rows = event.coordinateY
}

fun handlePlaceType(event: RobotEvent, grid: Grid, robot: Robot) {
    if(grid.isValidPosition(event.coordinateX, event.coordinateY)) {
        robot.coordinateX = event.coordinateX
        robot.coordinateY = event.coordinateY
        robot.direction = event.direction
    } else {
        //TODO: Add failure handling
        //println("cannot place $grid : $robot")
    }
}

fun handleRotateRightCmd(robot: Robot) = robot.rotateRight()

fun handleRotateLeftCmd(robot: Robot) = robot.rotateLeft()

fun handleMoveOneCmd(grid: Grid, robot: Robot) {
    if (grid.canMove(robot.coordinateX, robot.coordinateY, robot.direction)) {
        robot.moveOne()
    } else {
        //TODO: Add failure handling
        //println("cannot move $grid : $robot")
    }
}

fun handlePrintCmd(robot: Robot) {
    println("robot located at ${robot.coordinateX},${robot.coordinateY} facing ${robot.direction}")
}
