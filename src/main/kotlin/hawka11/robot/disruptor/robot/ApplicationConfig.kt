package hawka11.robot.disruptor.robot

data class ApplicationConfig(
        var metrics: Metics,
        var shouldPrintPosition: Boolean
)

data class Metics(
        var shouldCaptureThroughput: Boolean,
        var shouldCaptureLatency: Boolean
)