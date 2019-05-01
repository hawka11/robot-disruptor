package hawka11.robot.disruptor.example

import com.lmax.disruptor.EventFactory
import com.lmax.disruptor.EventHandler
import com.lmax.disruptor.EventTranslatorOneArg
import com.lmax.disruptor.RingBuffer
import java.nio.ByteBuffer

data class LongEvent(var value: Long = 0)

class LongEventFactory : EventFactory<LongEvent> {
    override fun newInstance(): LongEvent = LongEvent()
}

class LongEventHandler : EventHandler<LongEvent> {

    private var count: Long = 0
    private var total: Long = 0

    override fun onEvent(event: LongEvent, sequence: Long, endOfBatch: Boolean) {
        val latency = System.nanoTime() - event.value
        total += latency
        count += 1

        if (sequence % 20 == 0L) {
            println("${Thread.currentThread()}: Event $event: $latency : ${total / count}")
        }
    }
}

class LongEventProducerWithTranslator(private val ringBuffer: RingBuffer<LongEvent>) {

    fun onData(bb: ByteBuffer) {
        ringBuffer.publishEvent(TRANSLATOR, bb)
    }

    companion object {
        val TRANSLATOR = EventTranslatorOneArg<LongEvent, ByteBuffer> { event, sequence, bb: ByteBuffer ->
            event.value = bb.getLong(0)
        }
    }
}