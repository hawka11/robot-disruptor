package hawka11.robot.disruptor.robot.handlers

import hawka11.robot.disruptor.robot.RobotEvent
import hawka11.robot.disruptor.robot.RobotEventType
import hawka11.robot.disruptor.robot.game.*
import com.lmax.disruptor.EventHandler

private val throughput = Metrics.REGISTRY.meter("throughput")
private val latency = Metrics.REGISTRY.histogram("latency")

class RobotEventHandler : EventHandler<RobotEvent> {

    private val robot = Robot()
    private val grid = Grid()

    override fun onEvent(event: RobotEvent, sequence: Long, endOfBatch: Boolean) {

        when (event.type) {
            RobotEventType.INIT -> handleInitType(event, grid)
            RobotEventType.PLACE -> handlePlaceType(event, grid, robot)
            RobotEventType.RIGHT -> handleRotateRightCmd(robot)
            RobotEventType.LEFT -> handleRotateLeftCmd(robot)
            RobotEventType.MOVE -> handleMoveOneCmd(grid, robot)
            RobotEventType.PRINT -> handlePrintCmd(robot)
            RobotEventType.UNKNOWN -> handlePrintCmd(robot)
        }

        throughput.mark()
        //latency.update(System.nanoTime() - event.createdAt)
    }
}
