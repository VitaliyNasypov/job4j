package ru.job4j.concurrent;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class SimpleBlockingQueueTest {
    private static final Logger LOGGER = Logger.getLogger(SimpleBlockingQueueTest.class.getName());

    @Test
    public void whenFetchAllThenGetIt() throws InterruptedException {
        final SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(5);
        final List<Integer> delivery = new ArrayList<>();
        Thread producer = new Thread(
                () -> {
                    for (int i = 0; i < 7; i++) {
                        queue.offer(i);
                    }
                }
        );
        producer.start();
        Thread consumer = new Thread(
                () -> {
                    while (delivery.size() != 7) {
                        try {
                            delivery.add(queue.poll());
                        } catch (InterruptedException e) {
                            LOGGER.severe(e.getMessage());
                        }
                    }
                }
        );
        consumer.start();
        consumer.join();
        Assertions.assertEquals(7,delivery.size());
    }
}
