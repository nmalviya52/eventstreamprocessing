package com.company;

/**
 * Created by naresh.m on 23/05/17.
 */
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class TaskCache<T,D> {

    LoadingCache<T,D> lastreqtime;

    public TaskCache(long duration)
    {
        lastreqtime=CacheBuilder.newBuilder().expireAfterWrite(duration, TimeUnit.SECONDS).build(new CacheLoader<T, D>() {
            @Override
            public D load(T t) throws Exception {
                throw new Exception();
            }
        });
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

    D getValue(T taskId)
    {
        try {
            return (D)lastreqtime.get(taskId);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
}
