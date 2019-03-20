package me.sieric;

import org.jetbrains.annotations.NotNull;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Class that sorts given Array of elements according to there naturalOrder.
 * Uses quickSort as algorithm for sorting
 * Provides methods for execution in one thread and in several threads
 */
public class QSort {

    private static ExecutorService pool;
    private static final Random random = new Random();
    private static final int MAX_POOL_SIZE = Runtime.getRuntime().availableProcessors();
    private static final AtomicInteger poolSize = new AtomicInteger();


    /**
     * Sorts given array (using quicksort) in natural order in a single thread
     * @param array - an array to sort
     * @param <T> - the type of array elements, must extend Comparable
     */
    public static <T extends Comparable<? super T>> void singleThreadSort(@NotNull T[] array) {
        pool = Executors.newCachedThreadPool();
        sortInSingleThread(array, 0, array.length);
    }

    /**
     * Sorts given array (using quicksort) in natural order in several threads
     * @param array - an array to sort
     * @param <T> - the type of array elements, must extend Comparable
     * @throws InterruptedException if one of working threads was occasionally interrupted
     */
    public static <T extends Comparable<? super T>> void paralleledSort(@NotNull T[] array) throws InterruptedException {
        pool = Executors.newFixedThreadPool(MAX_POOL_SIZE);
        poolSize.set(1);
        pool.execute(() -> sortParalleled(array, 0, array.length));
        synchronized (poolSize) {
            poolSize.wait();
        }
        pool.shutdown();
    }

    private static <T extends Comparable<? super T>> void sortInSingleThread(@NotNull T[] array, int left, int right) {
        if (right - left <= 1) {
            return;
        }
        int partitioningIndex = partition(array, left, right);
        sortInSingleThread(array, left, partitioningIndex);
        sortInSingleThread(array, partitioningIndex, right);
    }

    private static <T extends Comparable<? super T>> void sortParalleled(@NotNull T[] array, int left, int right) {
        if (right - left > 1) {
            int partitioningIndex = partition(array, left, right);

            if (poolSize.get() >= 2 * MAX_POOL_SIZE) {
                sortInSingleThread(array, left, partitioningIndex);
                sortInSingleThread(array, partitioningIndex, right);
            } else {
                poolSize.getAndAdd(2);
                pool.execute(() -> sortParalleled(array, left, partitioningIndex));
                pool.execute(() -> sortParalleled(array, partitioningIndex, right));
            }
        }
        synchronized (poolSize) {
            if (poolSize.decrementAndGet() == 0) {
                poolSize.notify();
            }
        }
    }

    private static <T extends Comparable<? super T> > int partition(@NotNull T[] array, int left, int right) {
        int pivot = random.nextInt(right - left) + left;
        T pivotElement = array[pivot];
        right--;
        while (left <= right) {
            while (array[left].compareTo(pivotElement) < 0) {
                left++;
            }
            while (array[right].compareTo(pivotElement) > 0) {
                right--;
            }
            if (left <= right) {
                T tmp = array[left];
                array[left] = array[right];
                array[right] = tmp;
                left++;
                right--;
            }
        }
        return left;
    }

}
