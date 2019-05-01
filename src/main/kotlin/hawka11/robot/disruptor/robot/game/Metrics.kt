package hawka11.robot.disruptor.robot.game

import com.codahale.metrics.ConsoleReporter
import com.codahale.metrics.MetricRegistry
import java.util.concurrent.TimeUnit

object Metrics {

    val REGISTRY = MetricRegistry()

    init {
        ConsoleReporter.forRegistry(REGISTRY)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build()
                .start(10, TimeUnit.SECONDS)
    }


}