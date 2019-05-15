package hawka11.robot.disruptor.robot.queue

import hawka11.robot.disruptor.robot.ApplicationConfig
import hawka11.robot.disruptor.robot.RobotEventTranslator
import hawka11.robot.disruptor.robot.RobotEventType
import hawka11.robot.disruptor.robot.game.Direction
import java.nio.ByteBuffer

class MockQueueReader(private val config: ApplicationConfig) : QueueReader {

    private var isInit = false
    private var isPlaced = false
    private var direction = Direction.EAST
    private var x = 0
    private var y = 0

    private var count = 0L
    private var printEvery = 1000000L

    override fun nextMessage(bb: ByteBuffer) {
        when {
            !isInit -> simulateInitMsg(bb)
            !isPlaced -> simulatePlaceMsg(bb)
            config.shouldPrintPosition && count % printEvery == 0L -> simulatePrintMsg(bb)
            direction == Direction.EAST -> simulateMoveRightMsg(bb)
            direction == Direction.NORTH -> simulateMoveUpMsg(bb)
            direction == Direction.WEST -> simulateMoveLeftMsg(bb)
            direction == Direction.SOUTH -> simulateMoveDownMsg(bb)
        }

        bb.putLong(RobotEventTranslator.CREATED_AT_IDX, System.nanoTime())

        count++
    }

    private fun simulatePrintMsg(bb: ByteBuffer) {
        bb.putInt(RobotEventTranslator.EVENT_TYPE_IDX, RobotEventType.PRINT.ordinal)
    }

    private fun simulateInitMsg(bb: ByteBuffer) {
        bb.putInt(RobotEventTranslator.EVENT_TYPE_IDX, RobotEventType.INIT.ordinal)
        bb.putInt(RobotEventTranslator.CO_ORD_X_IDX, 4)
        bb.putInt(RobotEventTranslator.CO_ORD_Y_IDX, 4)
        isInit = true
    }

    private fun simulatePlaceMsg(bb: ByteBuffer) {
        bb.putInt(RobotEventTranslator.EVENT_TYPE_IDX, RobotEventType.PLACE.ordinal)
        bb.putInt(RobotEventTranslator.CO_ORD_X_IDX, 0)
        bb.putInt(RobotEventTranslator.CO_ORD_Y_IDX, 0)
        bb.putInt(RobotEventTranslator.DIRECTION_IDX, Direction.EAST.ordinal)
        isPlaced = true
    }

    private fun simulateMoveRightMsg(bb: ByteBuffer) {
        if (x >= 3) {
            bb.putInt(RobotEventTranslator.EVENT_TYPE_IDX, RobotEventType.LEFT.ordinal)
            direction = Direction.NORTH
        } else {
            bb.putInt(RobotEventTranslator.EVENT_TYPE_IDX, RobotEventType.MOVE.ordinal)
            x++
        }
    }

    private fun simulateMoveUpMsg(bb: ByteBuffer) {
        if (y >= 3) {
            bb.putInt(RobotEventTranslator.EVENT_TYPE_IDX, RobotEventType.LEFT.ordinal)
            direction = Direction.WEST
        } else {
            bb.putInt(RobotEventTranslator.EVENT_TYPE_IDX, RobotEventType.MOVE.ordinal)
            y++
        }
    }

    private fun simulateMoveLeftMsg(bb: ByteBuffer) {
        if (x == 0) {
            bb.putInt(RobotEventTranslator.EVENT_TYPE_IDX, RobotEventType.LEFT.ordinal)
            direction = Direction.SOUTH
        } else {
            bb.putInt(RobotEventTranslator.EVENT_TYPE_IDX, RobotEventType.MOVE.ordinal)
            x--
        }
    }

    private fun simulateMoveDownMsg(bb: ByteBuffer) {
        if (y == 0) {
            bb.putInt(RobotEventTranslator.EVENT_TYPE_IDX, RobotEventType.LEFT.ordinal)
            direction = Direction.EAST
        } else {
            bb.putInt(RobotEventTranslator.EVENT_TYPE_IDX, RobotEventType.MOVE.ordinal)
            y--
        }
    }
}