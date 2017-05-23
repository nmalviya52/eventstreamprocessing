package com.company;

/**
 * Created by naresh.m on 22/05/17.
 */
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Producer {

    private final ExecutorService executors;
    private final int delay;
    public static Consumer c;

    public Producer(int parallelFactor, int delay) {
        this.executors = Executors.newFixedThreadPool(parallelFactor);
        this.delay = delay;
    }

    private void run(Consumer consumer, long num) throws InterruptedException {
        for (int i = 0; i < num; i++) {
            executors.submit(() -> {
                consumer.consume(new Event());
            });
            Thread.sleep(delay);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int parallelFactor = 10;
        int delay = 100;
        long num = 1000;
        Consumer consumer = getConsumer();
        new Producer(parallelFactor, delay)
                .run(consumer, num);
    }

    private static Consumer getConsumer() {
        c=new myConsumer();
        return c;
//        return Consumer.ExecutableTask::execute;
    }


    private class Event implements Consumer.ExecutableTask {
        private final UUID taskId = UUID.randomUUID();
        private final UUID clientId = UUID.randomUUID();

        @Override
        public UUID getTaskID() {
            return taskId;
        }

        @Override
        public UUID getClientID() {
            return clientId;
        }

        @Override
        public String execute() {
            System.out.println(String.format("Event{client:%s, task:%s}", clientId, taskId));
            return "done";
        }
    }
}
