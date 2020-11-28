package ru.job4j.concurrent;

import java.util.concurrent.RecursiveTask;
import java.util.stream.IntStream;

public class ForkJoinLinearSearch<T> extends RecursiveTask<Integer> {
    private final T[] array;
    private final T elementSearch;
    private final int from;
    private final int to;

    public ForkJoinLinearSearch(T[] array, T elementSearch, int from, int to) {
        this.array = array;
        this.elementSearch = elementSearch;
        this.from = from;
        this.to = to;
    }

    @Override
    protected Integer compute() {
        if (to - from <= 10) {
            return linearSearchGeneric(array, elementSearch, from, to);
        }
        int middle = (from + to) / 2;
        var leftSearch = new ForkJoinLinearSearch<>(array, elementSearch, from, middle);
        var rightSearch = new ForkJoinLinearSearch<>(array, elementSearch, middle + 1, to);
        leftSearch.fork();
        rightSearch.fork();
        int left = leftSearch.join();
        int right = rightSearch.join();
        if (left > -1) {
            return left;
        }
        return Math.max(right, -1);
    }

    private int linearSearchGeneric(T[] array, T elementSearch, int from, int to) {
        return IntStream.range(from, to + 1)
                .filter(i -> elementSearch.equals(array[i]))
                .findFirst()
                .orElse(-1);
    }
}