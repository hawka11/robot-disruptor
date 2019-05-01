package hawka11.robot.disruptor.example

import com.lmax.disruptor.dsl.Disruptor
import com.lmax.disruptor.util.DaemonThreadFactory
import java.nio.ByteBuffer

fun main(args: Array<String>) {
    // The factory for the event
    val factory = LongEventFactory()

    // Specify the size of the ring buffer, must be power of 2.
    val bufferSize = 1024

    // Construct the Disruptor
    val disruptor = Disruptor(factory, bufferSize, DaemonThreadFactory.INSTANCE)

    // Connect the handler
    disruptor.handleEventsWith(LongEventHandler())

    // Start the Disruptor, starts all threads running
    disruptor.start()

    // Get the ring buffer from the Disruptor to be used for publishing.
    val ringBuffer = disruptor.ringBuffer

    val producer = LongEventProducerWithTranslator(ringBuffer)

    val bb = ByteBuffer.allocate(8)
    var l: Long = 0
    while (true) {
        bb.putLong(0, System.nanoTime())
        producer.onData(bb)
        Thread.sleep(10)
        l++
    }
}