package hawka11.robot.disruptor.robot

data class ApplicationConfig(var metrics: Metics)

data class Metics(
        var shouldCaptureThroughput: Boolean,
        var shouldCaptureLatency: Boolean
)