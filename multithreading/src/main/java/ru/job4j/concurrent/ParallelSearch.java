package ru.job4j.concurrent;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ParallelSearch {
    private static final Logger LOGGER = Logger.getLogger(ParallelSearch.class.getName());

    public static void main(String[] args) {
        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>();
        final Thread consumer = new Thread(
                () -> {
                    while (!Thread.currentThread().isInterrupted() || !queue.isEmpty()) {
                        try {
                            System.out.println(queue.poll());
                        } catch (InterruptedException e) {
                            LOGGER.log(Level.SEVERE, "Поток остановлен", e);
                            Thread.currentThread().interrupt();
                        }
                    }
                }
        );
        consumer.start();
        new Thread(
                () -> {
                    for (int index = 0; index != 3; index++) {
                        try {
                            queue.offer(index);
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    consumer.interrupt();
                }

        ).start();
    }
}
