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
        event.type = RobotEventType.fromIndex(bb.getInt(EVENT_TYPE_IDX))
        event.coordinateX = bb.getInt(CO_ORD_X_IDX)
        event.coordinateY = bb.getInt(CO_ORD_Y_IDX)
        event.direction = Direction.fromIndex(bb.getInt(DIRECTION_IDX))
        event.createdAt = bb.getLong(CREATED_AT_IDX)
    }

    companion object {

        const val EVENT_TYPE_IDX = 0
        const val CO_ORD_X_IDX = 4
        const val CO_ORD_Y_IDX = 8
        const val DIRECTION_IDX = 12
        const val CREATED_AT_IDX = 16

        fun allocate() = ByteBuffer.allocate(4 + 4 + 4 + 4 + 8)

        fun initialize(bb: ByteBuffer) {
            bb.putInt(EVENT_TYPE_IDX, RobotEventType.UNKNOWN.ordinal)
            bb.putInt(CO_ORD_X_IDX, -1)
            bb.putInt(CO_ORD_Y_IDX, -1)
            bb.putInt(DIRECTION_IDX, Direction.UNKNOWN.ordinal)
            bb.putLong(CREATED_AT_IDX, 0L)
        }
    }
}