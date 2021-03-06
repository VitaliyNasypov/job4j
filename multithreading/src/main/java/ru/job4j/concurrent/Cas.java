package ru.job4j.concurrent;

import java.util.concurrent.atomic.AtomicInteger;

public class Cas {
    private final AtomicInteger count = new AtomicInteger();

    public Cas(Integer value) {
        count.set(value);
    }

    public void increment() {
        int current;
        int next;
        do {
            current = count.get();
            next = current + 1;
        } while (!count.compareAndSet(current, next));
    }

    public int get() {
        return count.get();
    }
}
