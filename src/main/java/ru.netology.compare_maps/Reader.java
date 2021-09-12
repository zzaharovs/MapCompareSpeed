package ru.netology.compare_maps;

import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class Reader implements Runnable {

    private int minIndex;
    private int maxIndex;
    private Map <Integer, String> testMap;
    private CountDownLatch latch;
    private int counter;

    public Reader(Map<Integer, String> testMap, int minIndex, int maxIndex, CountDownLatch latch) {
        this.minIndex = minIndex;
        this.maxIndex = maxIndex;
        this.testMap = testMap;
        this.latch = latch;
    }

    @Override
    public void run() {

        for (int i = minIndex; i < maxIndex; i++) {

            String value = testMap.get(i);
            if (value != null) counter++;

        }
        latch.countDown();
        System.out.printf("В мапу в диапазон от %d до %d записалось %d значений\n", minIndex, maxIndex, counter);

    }

}
