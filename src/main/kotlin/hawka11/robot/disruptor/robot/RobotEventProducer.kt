package hawka11.robot.disruptor.robot

import hawka11.robot.disruptor.robot.game.Direction
import com.lmax.disruptor.EventTranslatorOneArg
import com.lmax.disruptor.RingBuffer
import java.nio.ByteBuffer

class RobotEventProducerWithTranslator(private val ringBuffer: RingBuffer<RobotEvent>) {

    fun onData(bb: ByteBuffer) {
        ringBuffer.publishEvent(TRANSLATOR, bb)
    }

    companion object {
        val TRANSLATOR = EventTranslatorOneArg<RobotEvent, ByteBuffer> { event, _, bb ->
            event.type = RobotEventType.fromIndex(bb.getInt(0))
            event.coordinateX = bb.getInt(4)
            event.coordinateY = bb.getInt(8)
            event.direction = Direction.fromIndex(bb.getInt(12))
        }

        fun initialize(bb: ByteBuffer) {
            bb.putInt(0, RobotEventType.UNKNOWN.ordinal)
            bb.putInt(4, -1)
            bb.putInt(8, -1)
            bb.putInt(12, Direction.UNKNOWN.ordinal)
        }
    }
}