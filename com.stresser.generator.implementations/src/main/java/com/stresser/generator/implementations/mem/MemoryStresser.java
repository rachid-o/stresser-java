package com.stresser.generator.implementations.mem;

import com.stresser.generator.api.StressGenerator;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.String.format;

public class MemoryStresser implements StressGenerator {

    private static final int KB = 1024;
    private static final int MB = 1024*KB;
    private static final int GB = 1024*MB;

    private List<Long> memory;
    private ExecutorService executor;

    @Override
    public void start() {
        executor = Executors.newFixedThreadPool(1);
        executor.submit(() -> {
            allocateMemory();
        });
    }

    @Override
    public void stop() {
        printMemory();
        executor.shutdownNow();
        println("Stopped " + getClass().getSimpleName());
    }

    /**
     * 1 long: ~16 bytes
     * 1 KB = 1000 / 16 = 62.5
     * 1 MB = 62_500
     * 1 GB = 62_500_000
     */
    private void allocateMemory() {
        printMemory();
//        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1 * GB);

        long amount = Integer.MAX_VALUE; // 2_147_483_647
        println(String.format("Creating %d random Longs", amount));
        printMemory();
//
        memory = new LinkedList<>();
        Random r = new Random();
//        byte[] b = new byte[KB];
        int millisToSleep = 100;
        for(long i=1; i<amount; i++) {
            memory.add(r.nextLong());

            if(i % (10_000_000) == 0) {
//                println(i);
                printMemory();
                try {
                    Thread.sleep(millisToSleep);
                } catch (InterruptedException e) {
                    println(format("%s is aborted, so break out of the loop", Thread.currentThread().getName()));
                    break;
                }
            }
        }
    }

    private void printMemory() {
        long free = Runtime.getRuntime().freeMemory();
        long max = Runtime.getRuntime().maxMemory();
        long total = Runtime.getRuntime().totalMemory();
        long usage = total - free;
        println(String.format("Memory usage: %d / %d", toM(usage), toM(max) ));
    }

    static long toM(long free) {
        return free / MB;
    }

    static void println(Object message) {
        String timestamp = DateTimeFormatter.ofPattern("HH:mm:ss.SSS").format(ZonedDateTime.now());
        System.out.println(timestamp + " - " + message);
    }

}