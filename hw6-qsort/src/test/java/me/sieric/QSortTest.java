package me.sieric;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class QSortTest {

    private static final Random random = new Random();

    private Integer[] getArray(int size) {
        var array = new Integer[size];
        for (int i = 0; i < array.length; i++) {
            array[i] = random.nextInt();
        }
        return array;
    }

    private void testSingleThreadSort(int n) {
        var array = getArray(n);
        var copiedArray = Arrays.copyOf(array, array.length);
        QSort.singleThreadSort(array);
        Arrays.sort(copiedArray);
        assertArrayEquals(copiedArray, array);
    }

    private void testParalleledThreadSort(int n) {
        var array = getArray(n);
        var copiedArray = Arrays.copyOf(array, array.length);
        assertAll(() -> QSort.paralleledSort(array));
        Arrays.sort(copiedArray);
        assertArrayEquals(copiedArray, array);
    }

    @Test
    void testSingleThreadSort() {
        testSingleThreadSort(0);
        testSingleThreadSort(1);
        testSingleThreadSort(2);
        testSingleThreadSort(4);
        testSingleThreadSort(8);
        testSingleThreadSort(128);
        testSingleThreadSort(1000);
        testSingleThreadSort(10000);
        testSingleThreadSort(100000);
    }

    @Test
    void testParalleledSort() {
        testParalleledThreadSort(0);
        testParalleledThreadSort(1);
        testParalleledThreadSort(2);
        testParalleledThreadSort(4);
        testParalleledThreadSort(8);
        testParalleledThreadSort(128);
        testParalleledThreadSort(1000);
        testParalleledThreadSort(10000);
        testParalleledThreadSort(100000);
    }

    @Test
    void compareSorts() {
        final int MAX_SIZE_TESTED = 1 << 20;
        int left = 1;
        int right = MAX_SIZE_TESTED;
        while (right - left > 1) {
            int middle = left + (right - left) / 2;
            var array = getArray(middle);
            var copiedArray = Arrays.copyOf(array, array.length);

            long startTime = System.currentTimeMillis();
            QSort.singleThreadSort(array);
            long endTime = System.currentTimeMillis();
            long timeOnSingle = endTime - startTime;

            startTime = System.currentTimeMillis();
            QSort.singleThreadSort(copiedArray);
            endTime = System.currentTimeMillis();
            long timeOnParalleled = endTime - startTime;

            if (timeOnSingle < timeOnParalleled) {
                left = middle;
            } else {
                right = middle;
            }
        }

        if (right < MAX_SIZE_TESTED) {
            System.out.println("Paralleled qsort is faster on array size from " + right);
        } else {
            System.out.println("Paralleled qsort is too slow");
        }
    }
}