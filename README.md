

##Dump Heap
jmap -dump:format=b,file=<file-path> <pid> 
where
pid: is the Java Process Id, whose heap dump should be captured
file-path: is the file path where heap dump will be written in to.


ps -ef | grep java | grep RobotMain

rm -rf /tmp/disruptor*

jmap -dump:format=b,file=/tmp/disruptor1 6250