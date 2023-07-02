import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ArraySumTest {
    @Test
    public void testArraySum() {
        int arraySize = 200_000_000;
        int[] randomArray = new int[arraySize];
        Random random = new Random();

        // Generating the random array
        for (int i = 0; i < arraySize; i++) {
            randomArray[i] = random.nextInt(10) + 1;
        }

        // Parallel array sum
        int numThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);
        ParallelArraySum[] partialSums = new ParallelArraySum[numThreads];
        int chunkSize = arraySize / numThreads;
        long parallelSum = 0;

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < numThreads; i++) {
            int start = i * chunkSize;
            int end = (i + 1) * chunkSize;

            if (i == numThreads - 1) {
                end = arraySize;
            }

            partialSums[i] = new ParallelArraySum(start, end, randomArray);
            executorService.execute(partialSums[i]);
        }

        executorService.shutdown();

        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (ParallelArraySum partialSum : partialSums) {
            parallelSum += partialSum.getPartialSum();
        }

        long parallelTime = System.currentTimeMillis() - startTime;

        // Single thread array sum
        startTime = System.currentTimeMillis();

        long singleThreadSum = 0;
        for (int num : randomArray) {
            singleThreadSum += num;
        }

        long singleThreadTime = System.currentTimeMillis() - startTime;

        // Verify that the sums are equal
        Assertions.assertEquals(singleThreadSum, parallelSum);
    }

}
