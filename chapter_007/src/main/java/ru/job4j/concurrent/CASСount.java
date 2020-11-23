package ru.job4j.concurrent;

import java.util.concurrent.atomic.AtomicInteger;

public class CASСount {
    private final AtomicInteger count = new AtomicInteger();

    public CASСount(Integer value) {
        count.set(value);
    }

    public CASСount() {
    }

    public void increment() {
        count.incrementAndGet();
    }

    public int get() {
        return count.get();
    }
}