package ru.job4j.concurrent;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.logging.Logger;
import java.util.stream.Stream;

public class CasTest {
    private static final Logger LOGGER = Logger.getLogger(SimpleBlockingQueueTest.class.getName());

    private static Stream<Integer> number() {
        return Stream.of(2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096);
    }

    @ParameterizedTest
    @MethodSource("number")
    public void shouldMakeTwoThreadIncrement(Integer count) {
        Cas countTest = new Cas(0);
        Thread first = new Thread(
                () -> {
                    for (int i = 0; i < count; i++) {
                        countTest.increment();
                    }
                }
        );
        Thread second = new Thread(
                () -> {
                    for (int i = 0; i < count; i++) {
                        countTest.increment();
                    }
                }
        );
        first.start();
        second.start();
        try {
            first.join();
            second.join();
        } catch (InterruptedException e) {
            LOGGER.severe(e.getMessage());
        }
        Assertions.assertEquals(count * 2, countTest.get());
    }
}
