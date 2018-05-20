package com.stresser.generator.cpu;

import com.stresser.generator.api.StressGenerator;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import static java.lang.String.format;

public class CpuStresser implements StressGenerator {

    private ExecutorService executor;

    private String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public void start() {
        int procsAvail = Runtime.getRuntime().availableProcessors();
        int half = Math.round(procsAvail / 2);
//        int procsToStress = half;
        int procsToStress = procsAvail;
        executor = Executors.newFixedThreadPool(procsToStress);
        println(format("Create %d concurrent tasks", procsToStress));
        IntStream.range(0, procsToStress).forEach( nr -> executor.submit(this::heavyTask));
    }

    @Override
    public void stop() {
        if(executor != null) {
            executor.shutdownNow();
        }
        println("Stopped " + getName());
    }

    private void heavyTask() {
        int millisToSleep = 1000;
        while (!Thread.currentThread().isInterrupted()) {
            fib(42);
            System.out.print(".");
            try {
//                println(format("Sleep for %d ms", millisToSleep));
                Thread.sleep(millisToSleep);
            } catch (InterruptedException e) {
//                println(format("%s is aborted, so break out of the loop", Thread.currentThread().getName()));
                break;
            }
        }
    }

    /**
     * Calc fibonacci number. Starts to take noticeable time when n > 42
     */
    private long fib(long n) {
        if (n == 1 || n == 2) {
            return 1;
        } else {
            return fib(n - 1) + fib(n - 2);
        }
    }

    private static void println(String message) {
        String timestamp = DateTimeFormatter.ofPattern("HH:mm:ss.SSS").format(ZonedDateTime.now());
        System.out.println(timestamp + " - " + message);
    }

}