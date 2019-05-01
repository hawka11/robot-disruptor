package hawka11.robot.disruptor.robot.handlers

import hawka11.robot.disruptor.robot.RobotEvent
import com.lmax.disruptor.EventHandler

class RobotClearEventHandler : EventHandler<RobotEvent> {

    override fun onEvent(event: RobotEvent, sequence: Long, endOfBatch: Boolean) {
        event.clear()
    }
}
