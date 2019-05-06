package hawka11.robot.disruptor.robot.handlers

import com.lmax.disruptor.EventHandler
import hawka11.robot.disruptor.robot.Metics
import hawka11.robot.disruptor.robot.RobotEvent
import hawka11.robot.disruptor.robot.RobotEventType
import hawka11.robot.disruptor.robot.game.*

private val throughput = Metrics.REGISTRY.meter("throughput")
private val latency = Metrics.REGISTRY.histogram("latency")

class RobotEventHandler(private val metrics: Metics) : EventHandler<RobotEvent> {

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

        if (metrics.shouldCaptureThroughput) throughput.mark()

        if (metrics.shouldCaptureLatency) latency.update(System.nanoTime() - event.createdAt)
    }
}
