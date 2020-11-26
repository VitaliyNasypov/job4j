package ru.job4j.concurrent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

public class ThreadPool {
    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadPool.class);
    private final List<Thread> threads;
    private final SimpleBlockingQueue<Runnable> tasks;
    private volatile boolean isRunnable;

    public ThreadPool() {
        threads = new LinkedList<>();
        tasks = new SimpleBlockingQueue<>();
        int size = Runtime.getRuntime().availableProcessors();
        isRunnable = true;
        for (int i = 0; i < size; i++) {
            Thread thread = new Thread(
                    () -> {
                        while (isRunnable || !tasks.isEmpty()) {
                            try {
                                tasks.poll().run();
                            } catch (InterruptedException e) {
                                LOGGER.warn(e.getMessage(), e);
                            }
                        }
                    }
            );
            thread.start();
            threads.add(thread);
        }
    }

    public void work(Runnable job) {
        try {
            tasks.offer(job);
        } catch (InterruptedException e) {
            LOGGER.warn(e.getMessage(), e);
        }

    }

    public void shutdown() {
        isRunnable = false;
    }
}