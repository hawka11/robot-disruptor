
# Overview

Fun POC of controlling the movement of a robot around a grid based game board 
by sending commands/events via the LMAX Disruptor framework (https://github.com/LMAX-Exchange/disruptor) and implemented
in pure kotlin.

This is a low latency / high thoughput example with minimal JVM object allocation (no garbage)

# Implementation Details

Code can be found [here](./src/main/kotlin/hawka11/robot/disruptor/robot)

We read each message from a [QueueReader](./src/main/kotlin/hawka11/robot/disruptor/robot/queue/QueueReader.kt) and populate a raw
ByteBuffer, this is then published into the disruptor framework in which a [Translator](./src/main/kotlin/hawka11/robot/disruptor/robot/RobotEventProducer.kt)
populates the data from the ByteBuffer into a pre allocated [RobotEvent](./src/main/kotlin/hawka11/robot/disruptor/robot/RobotEvent.kt) from the internal ring buffer.
 
There is one [MockQueueReader](./src/main/kotlin/hawka11/robot/disruptor/robot/queue/MockQueueReader.kt) implementation that
simulates messages to move the robot around the outer edge of a 4x4 game board. 
(and randomly simulate a PRINT message to print it's current location)

There is no object serialisation library / real queue, just direct byte buffer allocation / management.

To capture metrics / print position, turn these in [application.yml](./src/main/resources/config/application.yml)

# Performance

On my 2017 MBP (2.9 GHz Intel Core i7, 16GB RAM) it can process around 10million msg p/s with a latency of around 100 microseconds 

## Dump Heap
jmap -dump:format=b,file=<file-path> <pid> 
where
pid: is the Java Process Id, whose heap dump should be captured
file-path: is the file path where heap dump will be written in to.


ps -ef | grep java | grep RobotMain

rm -rf /tmp/disruptor*

jmap -dump:format=b,file=/tmp/disruptor1 6250