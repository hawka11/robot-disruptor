package hawka11.robot.disruptor.robot

import hawka11.robot.disruptor.robot.handlers.EventJournalerHandler
import hawka11.robot.disruptor.robot.handlers.RobotClearEventHandler
import hawka11.robot.disruptor.robot.handlers.RobotEventHandler
import hawka11.robot.disruptor.robot.queue.MockQueueReader
import hawka11.robot.disruptor.robot.queue.QueueReader
import com.lmax.disruptor.BusySpinWaitStrategy
import com.lmax.disruptor.dsl.Disruptor
import com.lmax.disruptor.dsl.ProducerType
import com.lmax.disruptor.util.DaemonThreadFactory
import java.nio.ByteBuffer

fun main(args: Array<String>) {
    // The factory for the event
    val factory = RobotEventFactory()

    // Specify the size of the ring buffer, must be power of 2.
    val bufferSize = 1024

    // Construct the Disruptor
    val disruptor = Disruptor(factory,
            bufferSize,
            DaemonThreadFactory.INSTANCE,
            ProducerType.SINGLE,
            BusySpinWaitStrategy()
    )

    // Connect the handler(s)
    disruptor
            .handleEventsWith(RobotEventHandler(), EventJournalerHandler())
            .then(RobotClearEventHandler())

    // Start the Disruptor, starts all threads running
    disruptor.start()

    // Get the ring buffer from the Disruptor to be used for publishing.
    val ringBuffer = disruptor.ringBuffer


    val producer = RobotEventProducerWithTranslator(ringBuffer)

    val queue: QueueReader = MockQueueReader()

    val bb = ByteBuffer.allocate(4 + 4 + 4 + 4)

    while (true) {

        RobotEventProducerWithTranslator.initialize(bb)

        queue.nextMessage(bb)

        producer.onData(bb)
    }
}