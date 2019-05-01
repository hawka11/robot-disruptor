package hawka11.robot.disruptor.robot.handlers

import hawka11.robot.disruptor.robot.RobotEvent
import com.lmax.disruptor.EventHandler

class EventJournalerHandler : EventHandler<RobotEvent> {

    private var count = 0L

    override fun onEvent(event: RobotEvent, sequence: Long, endOfBatch: Boolean) {
        count++
    }
}
