package ru.job4j.concurrent;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.util.Random;
import java.util.concurrent.ForkJoinPool;

public class ForkJoinLinearSearchTest {
    @Test
    public void shouldReturnIndexElement() {
        Integer[] array = new Random()
                .ints(15, 275, 834)
                .boxed()
                .toArray(Integer[]::new);
        ForkJoinLinearSearch<Integer> forkJoinLinearSearch =
                new ForkJoinLinearSearch<>(array, array[10], 0, array.length - 1);
        ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
        Integer index = forkJoinPool.invoke(forkJoinLinearSearch);
        Assertions.assertEquals(10, index);
    }

    @Test
    public void shouldReturnIndexNegative() {
        Integer[] array = new Random()
                .ints(15, 275, 834)
                .boxed()
                .toArray(Integer[]::new);
        ForkJoinLinearSearch<Integer> forkJoinLinearSearch =
                new ForkJoinLinearSearch<>(array, 100, 0, array.length - 1);
        ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
        Integer index = forkJoinPool.invoke(forkJoinLinearSearch);
        Assertions.assertEquals(-1, index);
    }
}
