package hawka11.robot.disruptor.example

import com.lmax.disruptor.EventFactory
import com.lmax.disruptor.EventHandler
import com.lmax.disruptor.EventTranslatorOneArg
import com.lmax.disruptor.RingBuffer
import hawka11.robot.disruptor.robot.game.Metrics
import java.nio.ByteBuffer

data class LongEvent(var value: Long = 0)

class LongEventFactory : EventFactory<LongEvent> {
    override fun newInstance(): LongEvent = LongEvent()
}

class LongEventHandler : EventHandler<LongEvent> {

    private val throughput = Metrics.REGISTRY.meter("throughput")

    override fun onEvent(event: LongEvent, sequence: Long, endOfBatch: Boolean) {
        throughput.mark()
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