package com.company;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by naresh.m on 23/05/17.
 */
public class myConsumer implements Consumer {
    private final int parallelfactor=10;
    ExecutorService[] executors=new ExecutorService[10];
    TaskCache<UUID,Long> cache;

    myConsumer() {
        cache=new TaskCache<>(10);
        for (int i = 0; i < parallelfactor; i++) {
            executors[i] = Executors.newSingleThreadExecutor();
        }
    }
    class mythread implements Runnable{
        UUID taskId;
        UUID clientId;
        public mythread(ExecutableTask e) {
            this.taskId= e.getTaskID();
            this.clientId=e.getClientID();
        }
        @Override
        public void run() {
            System.out.println(String.format("Executed   event{client:%s, task:%s}", clientId, taskId));
        }
    }

    public void consume(ExecutableTask e)
    {
        long curtimesec=System.currentTimeMillis();
        if(cache.containsValue(e.getTaskID()))
        {
            cache.insert(e.getTaskID(),curtimesec);
        }
        else
        {
            cache.insert(e.getTaskID(),curtimesec);
            int hashval=e.getClientID().hashCode()%10;
            // same ClientID task are submitted to same ExecutorService
            executors[hashval].submit(new Thread(new mythread(e)));
        }
    }
}
