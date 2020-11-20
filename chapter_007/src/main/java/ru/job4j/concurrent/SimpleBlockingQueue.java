package ru.job4j.concurrent;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Logger;

@ThreadSafe
public class SimpleBlockingQueue<T> {
    private static final Logger LOGGER = Logger.getLogger(Wget.class.getName());
    @GuardedBy("this")
    private final Queue<T> queue = new LinkedList<>();
    private final int limitQueue;

    public SimpleBlockingQueue() {
        limitQueue = 5;
    }

    public SimpleBlockingQueue(int limitQueue) {
        this.limitQueue = limitQueue;
    }

    public void offer(T value) {
        while (true) {
            synchronized (this) {
                while (queue.size() == limitQueue) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        LOGGER.severe(e.getMessage());
                    }
                }
                queue.offer(value);
                this.notify();
            }
        }
    }

    public T poll() throws InterruptedException {
        while (true) {
            synchronized (this) {
                while (queue.size() == 0) {
                    this.wait();
                }
                this.notify();
                return queue.poll();
            }
        }
    }

    public synchronized int getSize() {
        return queue.size();
    }

    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }
}