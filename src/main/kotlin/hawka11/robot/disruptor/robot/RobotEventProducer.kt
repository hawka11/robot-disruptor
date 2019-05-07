package hawka11.robot.disruptor.robot

import com.lmax.disruptor.EventTranslatorOneArg
import com.lmax.disruptor.RingBuffer
import hawka11.robot.disruptor.robot.game.Direction
import java.nio.ByteBuffer

class RobotEventProducer(private val ringBuffer: RingBuffer<RobotEvent>) {

    fun onData(bb: ByteBuffer) {
        ringBuffer.publishEvent(TRANSLATOR, bb)
    }

    companion object {
        val TRANSLATOR = RobotEventTranslator()
    }
}

class RobotEventTranslator : EventTranslatorOneArg<RobotEvent, ByteBuffer> {

    override fun translateTo(event: RobotEvent, sequence: Long, bb: ByteBuffer) {
        event.type = RobotEventType.fromIndex(bb.getInt(0))
        event.coordinateX = bb.getInt(4)
        event.coordinateY = bb.getInt(8)
        event.direction = Direction.fromIndex(bb.getInt(12))
        event.createdAt = bb.getLong(16)
    }

    companion object {

        fun allocate() = ByteBuffer.allocate(4 + 4 + 4 + 4 + 8)

        fun initialize(bb: ByteBuffer) {
            bb.putInt(0, RobotEventType.UNKNOWN.ordinal)
            bb.putInt(4, -1)
            bb.putInt(8, -1)
            bb.putInt(12, Direction.UNKNOWN.ordinal)
            bb.putLong(16, 0L)
        }
    }
}