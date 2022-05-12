package com.example.myapplication.db;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppExecutor {

    private static AppExecutor instance;
    private final Executor mainThread;
    private final Executor subThread;

    public AppExecutor(Executor mainThread, Executor subThread) {
        this.mainThread = mainThread;
        this.subThread = subThread;
    }

    public static AppExecutor getInstance() {

        if (instance == null)
            instance = new AppExecutor( new MainThreadHandler(), Executors.newSingleThreadExecutor());
        return instance;
    }

    public static class MainThreadHandler implements Executor {
        private Handler mainHandler = new Handler(Looper.getMainLooper());// хэндлер будет запускаться в основном потоке

        @Override
        public void execute(Runnable runnable) {
            mainHandler.post(runnable);
        }
    }

    public Executor getMainThread() {
        return mainThread;
    }

    public Executor getSubThread() {
        return subThread;
    }
}
