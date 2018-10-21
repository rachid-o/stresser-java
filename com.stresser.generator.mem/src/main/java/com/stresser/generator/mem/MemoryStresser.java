package com.stresser.generator.mem;

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
    private static final int MB = 1024 * KB;
    private static final int GB = 1024 * MB;

    private volatile List<Long> memory = new LinkedList<>();
    private ExecutorService executor;
    private long maxMem;

    @Override
    public void start() {
        memory = new LinkedList<>();
        maxMem = Runtime.getRuntime().maxMemory();

        if (executor != null) {
            throw new IllegalStateException("Already started! Stop first");
        }
        executor = Executors.newFixedThreadPool(1);
        executor.submit(this::allocateMemory);
    }

    @Override
    public void stop() {
        memory.clear();
        memory = new LinkedList<>();
        if (executor != null) {
            printMemory();
            executor.shutdownNow();
            executor = null;
        }
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

        Random r = new Random();
//        byte[] b = new byte[KB];
        int millisToSleep = 1000;
        for (long i = 1; i < amount; i++) {
            if(getMemoryUsage() > (maxMem - 200*MB) ) {
                println("Finsihed generating memory");
                break;
            }
            memory.add(r.nextLong());

            try {
//                if (i % (1_000) == 0) {
//                    Thread.sleep(1);
//                }
//                if (i % (100) == 0) {
//                    System.out.print(".");
//                }
                if (i % (10_000_000) == 0) {
//                    System.out.println("\n");
                    printMemory();
                    Thread.sleep(millisToSleep);
                }
            } catch (InterruptedException e) {
                println(format("%s is aborted, so break out of the loop", Thread.currentThread().getName()));
                break;
            }
        }
        printMemory();
    }

    private void printMemory() {
        println(String.format("Memory usage: %d / %d", toM(getMemoryUsage()), toM(maxMem)));
    }

    private long getMemoryUsage() {
        long free = Runtime.getRuntime().freeMemory();
        long total = Runtime.getRuntime().totalMemory();
        return total - free;
    }

    static long toM(long free) {
        return free / MB;
    }

    static void println(Object message) {
        String timestamp = DateTimeFormatter.ofPattern("HH:mm:ss.SSS").format(ZonedDateTime.now());
        System.out.println(timestamp + " - " + message);
    }

}