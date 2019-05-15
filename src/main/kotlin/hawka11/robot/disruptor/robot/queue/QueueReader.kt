package hawka11.robot.disruptor.robot.queue

import java.nio.ByteBuffer

interface QueueReader {

    fun nextMessage(bb: ByteBuffer)
}
