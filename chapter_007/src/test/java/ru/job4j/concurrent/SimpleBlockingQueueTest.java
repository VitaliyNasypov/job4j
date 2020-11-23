package ru.job4j.concurrent;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Logger;

public class SimpleBlockingQueueTest {
    private static final Logger LOGGER = Logger.getLogger(SimpleBlockingQueueTest.class.getName());

    @Test
    public void whenFetchAllThenGetIt() throws InterruptedException {
        final SimpleBlockingQueue<Integer> pendingTasks = new SimpleBlockingQueue<>(5);
        Queue<Integer> newTasks = new LinkedList<>();
        for (int i = 0; i < 7; i++) {
            newTasks.offer(i);
        }
        Queue<Integer> completedTasks = new LinkedList<>();
        Thread producer = new Thread(
                () -> {
                    try {
                        while (!(newTasks.size() == 0)) {
                            pendingTasks.offer(newTasks.poll());
                        }
                    } catch (InterruptedException e) {
                        LOGGER.severe(e.getMessage());
                    }
                }
        );
        producer.start();
        Thread consumer = new Thread(
                () -> {
                    try {
                        while (!Thread.currentThread().isInterrupted()) {
                            completedTasks.add(pendingTasks.poll());
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        LOGGER.severe(e.getMessage());
                    }
                }
        );
        consumer.start();
        producer.join();
        consumer.interrupt();
        Assertions.assertEquals(0, newTasks.size());
        Assertions.assertEquals(0, pendingTasks.getSize());
        Assertions.assertEquals(7, completedTasks.size());
    }
}
