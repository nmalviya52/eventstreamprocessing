package com.company;

/**
 * Created by naresh.m on 23/05/17.
 */
import com.google.common.cache.*;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class TaskCache<T,D> {

    Cache<T,D> lastreqtime;
    public TaskCache(long duration)
    {
        lastreqtime=CacheBuilder.newBuilder().expireAfterAccess(duration, TimeUnit.SECONDS).build();
    }

    void insert(T taskId,D curtimesec)
    {
        lastreqtime.put(taskId,curtimesec);
    }

    Boolean containsValue(T taskId)
    {
        if(lastreqtime.getIfPresent(taskId)!=null) {
            return true;
        } else
            return false;
    }

}
