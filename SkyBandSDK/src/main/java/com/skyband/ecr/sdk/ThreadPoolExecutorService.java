package com.skyband.ecr.sdk;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolExecutorService {

    private static ThreadPoolExecutorService executorService;
    private final int corePoolSize;
    private final int maximumPoolSize;
    private long keepAliveTime = 1;
    private TimeUnit unit = TimeUnit.SECONDS;
    private ThreadPoolExecutor executor;
    private Handler handler;

    public static ThreadPoolExecutorService getInstance() {
        if (executorService == null) {
            executorService = new ThreadPoolExecutorService();
        }
        return executorService;
    }

    private ThreadPoolExecutorService() {

        corePoolSize = Runtime.getRuntime().availableProcessors() * 2 + 1;
        maximumPoolSize = Integer.MAX_VALUE;
        executor = new ThreadPoolExecutor(corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                unit,
                new SynchronousQueue<Runnable>(),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());
        handler = new Handler(Looper.getMainLooper());
    }

    public ThreadPoolExecutor getExecutor() {
        return executor;
    }

    public void execute(Runnable runnable) {
        if (runnable == null) return;
        executor.execute(runnable);
    }

    public void runOnUiThread(Runnable runnable) {
        if (handler == null) {
            handler = new Handler(Looper.getMainLooper());
        }
        handler.post(runnable);
    }

    public Handler getHandler() {
        return handler;
    }

    public void remove(Runnable runnable) {
        if (runnable == null) return;
        executor.remove(runnable);
    }
}
