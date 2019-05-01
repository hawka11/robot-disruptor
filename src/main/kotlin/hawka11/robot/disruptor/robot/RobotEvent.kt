package hawka11.robot.disruptor.robot

import hawka11.robot.disruptor.robot.game.Direction
import com.lmax.disruptor.EventFactory

class RobotEventFactory : EventFactory<RobotEvent> {
    override fun newInstance(): RobotEvent = RobotEvent()
}


data class RobotEvent(
        var type: RobotEventType = RobotEventType.UNKNOWN,
        var coordinateX: Int = -1,
        var coordinateY: Int = -1,
        var direction: Direction = Direction.UNKNOWN
) {

    fun clear() {
        type = RobotEventType.UNKNOWN
        coordinateX = -1
        coordinateY = -1
        direction = Direction.UNKNOWN
    }
}


enum class RobotEventType {

    UNKNOWN, INIT, PLACE, RIGHT, LEFT, MOVE, PRINT;

    companion object {
        private val values = RobotEventType.values()

        fun fromIndex(idx: Int): RobotEventType {
            return values[idx]
        }
    }
}
