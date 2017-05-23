package com.company;

/**
 * Created by naresh.m on 22/05/17.
 */

import java.util.*;

import javafx.concurrent.Task;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.util.Pair;
/**
 * Created by naresh.m on 22/05/17.
 */

public class myConsumer2 implements Consumer {
    private final int parallelfactor=10;
    HashMap<UUID,Long> lastreqtime=new HashMap<>();
    ExecutorService[] executors=new ExecutorService[10];
    Queue<Pair<UUID,Long> > q=new ArrayDeque<>();

    myConsumer2() {
        for (int i = 0; i < parallelfactor; i++) {
            executors[i] = Executors.newSingleThreadExecutor();
        }
    }
    class mythread implements Runnable{
        Thread runner;
        UUID taskId;
        UUID clientId;
        public mythread(String threadName,ExecutableTask e) {
            this.taskId= e.getTaskID();
            this.clientId=e.getClientID();
            runner = new Thread(this, threadName);
            runner.start();
        }
        @Override
        public void run() {
            System.out.println(String.format("Executed   event{client:%s, task:%s}", clientId, taskId));
        }
    }

    public void consume(ExecutableTask e)
    {
        long curtimesec=System.currentTimeMillis();
        Boolean allowed=false;
        synchronized(myConsumer2.class) {
//            System.out.println(q.size());
            while (q.size() > 300 && curtimesec - q.peek().getValue() >= 10000) {
                if (lastreqtime.get(q.peek().getKey()) == q.peek().getValue()) {
                    lastreqtime.remove(q.peek().getKey());
                }
                q.poll();
            }
            q.add(new Pair<>(e.getTaskID(), curtimesec));
            if (!lastreqtime.containsKey(e.getTaskID())) {
                allowed = true;
                lastreqtime.put(e.getTaskID(), curtimesec);
            } else if (curtimesec - lastreqtime.get(e.getTaskID()) >= 10000) {
                allowed = true;
                lastreqtime.replace(e.getTaskID(), lastreqtime.get(e.getTaskID()), curtimesec);
            } else {
                lastreqtime.replace(e.getTaskID(), lastreqtime.get(e.getTaskID()), curtimesec);
            }
        }
        if(allowed)
        {
            int hashval=e.getClientID().hashCode()%10;
            // same ClientID task are submitted to same ExecutorService
            executors[hashval].submit(new mythread("hello",e));
        }
    }

}
