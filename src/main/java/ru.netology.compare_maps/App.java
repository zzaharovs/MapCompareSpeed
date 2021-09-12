package ru.netology.compare_maps;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/*

Тест 1
1000 операций записи в мапу, по 100 операций на каждый поток + столько же операций чтения
ConcurrentHashMap
Время записи 0.019 с
Время чтения 0.006 с
Общее время 0.025 c

SynchronizedMap
Время записи - 0.024 с
Время чтения 0.002 с
Общее время 0.26

Тест 2
10000 операций записи в мапу, по 1000 операций на каждый поток + столько же операций чтения
ConcurrentHashMap
Время записи 0.043 с
Время чтения 0.001 с
Общее время 0.044 c

SynchronizedMap
Время записи - 0.047 с
Время чтения 0.012 с
Общее время 0.059

Тест 3
1 000 000 операций записи в мапу, по 100 000 операций на каждый поток + столько же операций чтения
ConcurrentHashMap
Время записи 1.133 с
Время чтения 0.215 с
Общее время 1.348 c

SynchronizedMap
Время записи - 1.719 с
Время чтения 0.376 с
Общее время 2.095 c

Тест 4
10 000 000 операций записи в мапу, по 1 000 000 операций на каждый поток + столько же операций чтения
ConcurrentHashMap
Время записи 12.505 с
Время чтения 1.864 с
Общее время 14.369 c

SynchronizedMap
Время записи - 15.8 с
Время чтения 4.648 с
Общее время 20.448 c

 */



public class App {

    public static final int COUNTER_NUMBER = 10;
    public static final int MAX_COUNTER = 10_000_000;
    public static final int SEARCH_AREA = MAX_COUNTER / COUNTER_NUMBER;

    public static void main(String[] args) throws InterruptedException {

        Map<Integer, String> concurrentHM = new ConcurrentHashMap<>();
        Map<Integer, String> oldConcurrentHM = Collections.synchronizedMap(new HashMap<>());

        CountDownLatch cdlWrite = new CountDownLatch(COUNTER_NUMBER);
        CountDownLatch cdlRead = new CountDownLatch(COUNTER_NUMBER);

        ExecutorService threadPool = Executors.newFixedThreadPool(COUNTER_NUMBER);

        System.out.println("Стартуем");
        Instant start = Instant.now();

        writeMap(concurrentHM, threadPool, cdlWrite);
        //writeMap(oldConcurrentHM, threadPool, cdlWrite);

        Instant mid = Instant.now();
        System.out.println("Запись закончилась, время записи " + (Duration.between(start, mid)));
        System.out.println("Стартуем чтение");

        readMap(concurrentHM, threadPool, cdlRead);
        //readMap(oldConcurrentHM, threadPool, cdlRead);


        Instant end = Instant.now();
        System.out.println("Чтение закончилось, время чтения = "+ Duration.between(mid, end));
        System.out.println("Время работы программы = "+ Duration.between(start, end));
        threadPool.shutdown();

    }

    public static void writeMap (Map <Integer, String> testMap, ExecutorService threadPool, CountDownLatch cdlWrite) {

        int minIndex = 0;
        int maxIndex = SEARCH_AREA;

        for (int i = 0; i < COUNTER_NUMBER; i++) {
            threadPool.execute(new Generator(testMap, minIndex, maxIndex, cdlWrite));
            minIndex+= SEARCH_AREA;
            maxIndex+= SEARCH_AREA;

        }

        try {
            cdlWrite.await();
        } catch (InterruptedException ex) {
            System.out.println("Ошибка прерывания");
        }

    }

    public static void readMap(Map <Integer, String> testMap, ExecutorService threadPool, CountDownLatch cdlRead) {

        int minIndex = 0;
        int maxIndex = SEARCH_AREA;

        for (int i = 0; i < COUNTER_NUMBER; i++) {
            threadPool.execute(new Reader(testMap, minIndex, maxIndex, cdlRead));
            minIndex+= SEARCH_AREA;
            maxIndex+= SEARCH_AREA;
        }

        try {
            cdlRead.await();
        } catch (InterruptedException ex) {
            System.out.println("Ошибка прерывания");
        }

    }



}

