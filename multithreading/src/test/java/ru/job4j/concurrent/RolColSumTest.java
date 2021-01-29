package ru.job4j.concurrent;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.util.Arrays;

public class RolColSumTest {
    private final int[][] array = new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
    private final int[][] correctResult = new int[][]{{6, 15, 24}, {12, 15, 18}};

    @Test
    public void shouldReturnCorrectSum() {
        RolColSum.Sums[] sum = RolColSum.sum(array);
        int[] resultRow = Arrays.stream(sum).mapToInt(RolColSum.Sums::getRowSum).toArray();
        int[] resultCol = Arrays.stream(sum).mapToInt(RolColSum.Sums::getColSum).toArray();
        Assertions.assertArrayEquals(correctResult[0], resultRow);
        Assertions.assertArrayEquals(correctResult[1], resultCol);
    }

    @Test
    public void shouldReturnCorrectAsyncSum() {
        RolColSum.Sums[] asyncSum = RolColSum.asyncSum(array);
        int[] resultRow = Arrays.stream(asyncSum).mapToInt(RolColSum.Sums::getRowSum).toArray();
        int[] resultCol = Arrays.stream(asyncSum).mapToInt(RolColSum.Sums::getColSum).toArray();
        Assertions.assertArrayEquals(correctResult[0], resultRow);
        Assertions.assertArrayEquals(correctResult[1], resultCol);
    }
}
