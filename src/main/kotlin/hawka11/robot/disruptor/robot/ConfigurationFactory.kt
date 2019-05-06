package hawka11.robot.disruptor.robot

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.dropwizard.configuration.DefaultConfigurationFactoryFactory
import io.dropwizard.configuration.ResourceConfigurationSourceProvider
import javax.validation.Validation

fun loadConfiguration(filename: String): ApplicationConfig {
    val objectMapper = ObjectMapper().registerModule(KotlinModule())
    val validator = Validation.buildDefaultValidatorFactory().validator

    return DefaultConfigurationFactoryFactory<ApplicationConfig>()
            .create(ApplicationConfig::class.java, validator, objectMapper, "robot")
            .build(ResourceConfigurationSourceProvider(), filename)
}