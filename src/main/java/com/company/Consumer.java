package com.company;

/**
 * Created by naresh.m on 22/05/17.
 */
import java.util.UUID;
public interface Consumer {
    /*
        Executes the task, unless a duplicate TaskId was seen in the last 10 seconds
        Ensures that tasks with same clientID are processed in the order in which they
        are given to this method.

        Note: this method may be called from more than one thread.

     */
    void consume(ExecutableTask task);

    interface ExecutableTask {
        UUID getTaskID();

        UUID getClientID();

        String execute();
    }
}
