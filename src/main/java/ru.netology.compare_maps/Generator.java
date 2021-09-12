package ru.netology.compare_maps;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class Generator implements Runnable {

    private int minIndex;
    private int maxIndex;
    private Map<Integer, String> testMap;
    private CountDownLatch latch;
    int keyCounter;

    public Generator(Map<Integer, String> testMap, int minIndex, int maxIndex, CountDownLatch latch) {
        this.testMap = testMap;
        this.latch = latch;
        this.minIndex = minIndex;
        this.maxIndex = maxIndex;
    }

    @Override
    public void run() {

        System.out.println("Начал работу");

        for (int i = minIndex; i < maxIndex; i++) {
            {
                writeRandomValueToMap(i);
            }

        }
        latch.countDown();
        System.out.println("Закончил работу");

    }

    public void writeRandomValueToMap(int key) {

        testMap.put(key, "Значение " + keyCounter);

    }
}
