package ru.job4j.concurrent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class RolColSum {
    private static final Logger LOGGER = LoggerFactory.getLogger(RolColSum.class.getName());

    public static class Sums {
        private final int rowSum;
        private final int colSum;

        public Sums(int rowSum, int colSum) {
            this.rowSum = rowSum;
            this.colSum = colSum;
        }

        public int getRowSum() {
            return rowSum;
        }

        public int getColSum() {
            return colSum;
        }
    }

    public static Sums[] sum(int[][] matrix) {
        Sums[] sumsArray = new Sums[matrix.length];
        for (int i = 0; i < sumsArray.length; i++) {
            int rowSum = rowSum(matrix, i);
            int colSum = colSum(matrix, i);
            sumsArray[i] = new Sums(rowSum, colSum);
        }
        return sumsArray;
    }

    public static Sums[] asyncSum(int[][] matrix) {
        Sums[] sumsArray = new Sums[matrix.length];
        CompletableFuture<Integer> rowSum;
        CompletableFuture<Integer> colSum;
        for (int i = 0; i < sumsArray.length; i++) {
            int index = i;
            rowSum = CompletableFuture.supplyAsync(() -> rowSum(matrix, index));
            colSum = CompletableFuture.supplyAsync(() -> colSum(matrix, index));
            try {
                sumsArray[i] = rowSum.thenCombine(colSum, Sums::new).get();
            } catch (InterruptedException | ExecutionException e) {
                LOGGER.warn(e.getMessage(), e);
            }
        }
        return sumsArray;
    }

    private static int rowSum(int[][] matrix, int numberRow) {
        return Arrays.stream(matrix[numberRow]).sum();
    }

    private static int colSum(int[][] matrix, int numberColumn) {
        return Arrays.stream(matrix).mapToInt(row -> row[numberColumn]).sum();
    }
}