package ru.job4j.concurrent;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.util.concurrent.atomic.AtomicReference;

public class CacheBaseTest {
    @Test
    public void whenThrowException() throws InterruptedException {
        AtomicReference<Exception> ex = new AtomicReference<>();
        Thread thread = new Thread(
                () -> {
                    try {
                        CacheBase cacheBase = new CacheBase();
                        cacheBase.add(new Base(1, 10, "One"));
                        cacheBase.update(new Base(1, 2, "One"));
                    } catch (Exception e) {
                        ex.set(e);
                    }
                }
        );
        thread.start();
        thread.join();
        Assertions.assertEquals("Wrong version", ex.get().getMessage());
    }
}
