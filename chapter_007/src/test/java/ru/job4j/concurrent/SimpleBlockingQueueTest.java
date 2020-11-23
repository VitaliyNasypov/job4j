package ru.job4j.concurrent;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class SimpleBlockingQueueTest {
    private static final Logger LOGGER = Logger.getLogger(SimpleBlockingQueueTest.class.getName());

    private static Stream<Integer> countTask() {
        return Stream.of(1, 15, 30, 23, 56, 4, 78, 156, 234, 3, 2);
    }

    @ParameterizedTest
    @MethodSource("countTask")
    public void whenFetchAllThenGetIt(Integer count) throws InterruptedException {
        final SimpleBlockingQueue<Integer> pendingTasks = new SimpleBlockingQueue<>(5);
        Queue<Integer> newTasks = new LinkedList<>();
        for (int i = 0; i < count; i++) {
            newTasks.offer(i);
        }
        Queue<Integer> completedTasks = new LinkedList<>();
        Thread producer = new Thread(
                () -> {
                    try {
                        while (!newTasks.isEmpty()) {
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
                        while (!pendingTasks.isEmpty() || !Thread.currentThread().isInterrupted()) {
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
        consumer.join();
        Assertions.assertTrue(newTasks.isEmpty());
        Assertions.assertTrue(pendingTasks.isEmpty());
        Assertions.assertEquals(count, completedTasks.size());
    }
}
