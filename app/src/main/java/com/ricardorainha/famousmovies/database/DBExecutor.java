package com.ricardorainha.famousmovies.database;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DBExecutor {

    private static final Object LOCK = new Object();
    private static DBExecutor instance;
    private final Executor executor;

    private DBExecutor(Executor executor) {
        this.executor = executor;
    }

    public static DBExecutor getInstance() {
        if (instance == null) {
            synchronized (LOCK) {
                instance = new DBExecutor(Executors.newSingleThreadExecutor());
            }
        }
        return instance;
    }

    public Executor getExecutor() {
        return executor;
    }
}
