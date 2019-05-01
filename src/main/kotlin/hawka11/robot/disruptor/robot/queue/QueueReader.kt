package hawka11.robot.disruptor.robot.queue

import hawka11.robot.disruptor.robot.RobotEventType
import hawka11.robot.disruptor.robot.game.Direction
import java.nio.ByteBuffer

interface QueueReader {

    fun nextMessage(bb: ByteBuffer)
}


class MockQueueReader: QueueReader {

    private var isInit = false
    private var isPlaced = false
    private var direction = Direction.EAST
    private var x = 0
    private var y = 0

    private var count = 0L
    private var printEvery = 100L

    override fun nextMessage(bb: ByteBuffer) {
        when {
            !isInit -> simulateInitMsg(bb)
            !isPlaced -> simulatePlaceMsg(bb)
            //count % printEvery  == 0L -> simulatePrintMsg(bb)
            direction == Direction.EAST -> simulateMoveRightMsg(bb)
            direction == Direction.NORTH -> simulateMoveUpMsg(bb)
            direction == Direction.WEST -> simulateMoveLeftMsg(bb)
            direction == Direction.SOUTH -> simulateMoveDownMsg(bb)
        }

        bb.putLong(16, System.nanoTime())

        count++
    }

    private fun simulatePrintMsg(bb: ByteBuffer) {
        bb.putInt(0, RobotEventType.PRINT.ordinal)
    }

    private fun simulateInitMsg(bb: ByteBuffer) {
        bb.putInt(0, RobotEventType.INIT.ordinal)
        bb.putInt(4, 4)
        bb.putInt(8, 4)
        isInit = true
    }

    private fun simulatePlaceMsg(bb: ByteBuffer) {
        bb.putInt(0, RobotEventType.PLACE.ordinal)
        bb.putInt(4, 0)
        bb.putInt(8, 0)
        bb.putInt(12, Direction.EAST.ordinal)
        isPlaced = true
    }

    private fun simulateMoveRightMsg(bb: ByteBuffer) {
        if(x >= 3) {
            bb.putInt(0, RobotEventType.LEFT.ordinal)
            direction = Direction.NORTH
        } else {
            bb.putInt(0, RobotEventType.MOVE.ordinal)
            x++
        }
    }

    private fun simulateMoveUpMsg(bb: ByteBuffer) {
        if(y >= 3) {
            bb.putInt(0, RobotEventType.LEFT.ordinal)
            direction = Direction.WEST
        } else {
            bb.putInt(0, RobotEventType.MOVE.ordinal)
            y++
        }
    }

    private fun simulateMoveLeftMsg(bb: ByteBuffer) {
        if(x == 0) {
            bb.putInt(0, RobotEventType.LEFT.ordinal)
            direction = Direction.SOUTH
        } else {
            bb.putInt(0, RobotEventType.MOVE.ordinal)
            x--
        }
    }

    private fun simulateMoveDownMsg(bb: ByteBuffer) {
        if(y == 0) {
            bb.putInt(0, RobotEventType.LEFT.ordinal)
            direction = Direction.EAST
        } else {
            bb.putInt(0, RobotEventType.MOVE.ordinal)
            y--
        }
    }
}