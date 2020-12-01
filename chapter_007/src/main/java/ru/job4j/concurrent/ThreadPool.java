package ru.job4j.concurrent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

public class ThreadPool extends Thread {
    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadPool.class);
    private final List<Thread> threads;
    private final SimpleBlockingQueue<Runnable> tasks;
    private volatile boolean isRunnable;

    public ThreadPool() {
        threads = new LinkedList<>();
        tasks = new SimpleBlockingQueue<>();
        isRunnable = true;
        int size = Runtime.getRuntime().availableProcessors();
        for (int i = 0; i < size; i++) {
            Thread thread = new Thread(
                    () -> {
                        while (!Thread.currentThread().isInterrupted() || !tasks.isEmpty()) {
                            try {
                                tasks.poll().run();
                            } catch (InterruptedException e) {
                                LOGGER.warn(e.getMessage(), e);
                                Thread.currentThread().interrupt();
                            }
                        }
                    }
            );
            thread.start();
            threads.add(thread);
        }
        start();
    }

    @Override
    public void run() {
        while (threads.stream().anyMatch(Thread::isAlive)) {
        }
    }

    public void work(Runnable job) {
        if (isRunnable) {
            try {
                tasks.offer(job);
            } catch (InterruptedException e) {
                LOGGER.warn(e.getMessage(), e);
            }
        } else {
            throw new IllegalStateException("ThreadPool is closed");
        }
    }

    public void shutdown() {
        isRunnable = false;
        threads.forEach(Thread::interrupt);
    }
}