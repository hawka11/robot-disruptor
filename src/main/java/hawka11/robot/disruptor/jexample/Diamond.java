package hawka11.robot.disruptor.jexample;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.*;
import java.util.concurrent.*;

public class Diamond {

    public static void main(String[] args) {
        ExecutorService executor = Executors.newCachedThreadPool();
        Disruptor<LongEvent> disruptor = new Disruptor<>(LongEvent::new, 1024, executor, ProducerType.SINGLE, new SleepingWaitStrategy());

        //register five consumers and a final conclude
        disruptor.handleEventsWith(new Consumer(1), new Consumer(2), new Consumer(3), new Consumer(4), new Consumer(5)).then(new Conclude());

        disruptor.start();

        for (int i = 0; i < 3; i++) {
            EventTranslatorOneArg<LongEvent, Integer> cb = (event, sequence, newValue) -> {
                event.original = newValue;
                event.value = newValue;
            };
            disruptor.publishEvent(cb, i);
        }

        disruptor.shutdown();
        executor.shutdown();
    }

    public static class Consumer implements EventHandler<LongEvent> {
        private int i;
        public Consumer(int i) { this.i = i; }

        @Override
        public void onEvent(LongEvent event, long sequence, boolean endOfBatch) throws Exception {
            long value = event.value;
            System.out.println(Thread.currentThread() + ": Consumer: " + i + " processing current: " + value + " original : " + event.original);
            Thread.sleep(value * event.original * 10);
            event.value = value + 1;
        }
    }

    public static class Conclude implements EventHandler<LongEvent> {
        @Override
        public void onEvent(LongEvent event, long sequence, boolean endOfBatch) throws Exception {
            System.out.println("Conclude: " + event.value + " original: " + event.original);
        }
    }

    public static class LongEvent
    {
        public long value;
        public long original;
    }
}
