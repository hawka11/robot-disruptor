package hawka11.robot.disruptor.robot

import com.lmax.disruptor.BusySpinWaitStrategy
import com.lmax.disruptor.dsl.Disruptor
import com.lmax.disruptor.dsl.ProducerType
import com.lmax.disruptor.util.DaemonThreadFactory
import hawka11.robot.disruptor.robot.handlers.EventJournalerHandler
import hawka11.robot.disruptor.robot.handlers.RobotClearEventHandler
import hawka11.robot.disruptor.robot.handlers.RobotEventHandler
import hawka11.robot.disruptor.robot.queue.MockQueueReader
import hawka11.robot.disruptor.robot.queue.QueueReader

fun main(args: Array<String>) {

    val config = loadConfiguration("config/application.yml")

    val disruptor = constructDisruptor()

    // Connect the handler(s)
    disruptor
            .handleEventsWith(RobotEventHandler(config.metrics), EventJournalerHandler())
            .then(RobotClearEventHandler())

    // Start the Disruptor, starts all threads running
    disruptor.start()

    // Get the ring buffer from the Disruptor to be used for publishing.
    val ringBuffer = disruptor.ringBuffer

    val producer = RobotEventProducer(ringBuffer)

    val queue: QueueReader = MockQueueReader(config)

    val bb = RobotEventTranslator.allocate()

    while (true) {

        RobotEventTranslator.initialize(bb)

        //load next message from 'queue'
        queue.nextMessage(bb)

        //publish message into disruptor (ring buffer)
        producer.onData(bb)
    }
}

private fun constructDisruptor(): Disruptor<RobotEvent> {
    // The factory for the event
    val factory = RobotEventFactory()

    // Specify the size of the ring buffer, must be power of 2.
    val bufferSize = 1024 * 64

    // Construct the Disruptor
    return Disruptor<RobotEvent>(factory,
            bufferSize,
            DaemonThreadFactory.INSTANCE,
            ProducerType.SINGLE,
            BusySpinWaitStrategy()
    )
}