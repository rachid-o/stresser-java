package com.stresser.generator.cpu;

import com.stresser.generator.api.StressGenerator;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static java.lang.String.format;

public class CpuStresser implements StressGenerator {

    static final int timeout = 3;
    static final int delay = 0;


    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public void start() {
        println(getName() + " Start stressing the CPU");
        println(format("Start stressing CPU for %ds after waiting for %ds", timeout, delay));

        try {
            stress();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void stress() throws InterruptedException {
        int procsAvail = Runtime.getRuntime().availableProcessors();

        println(format("Create %d concurrent tasks", procsAvail));
        ExecutorService executor = Executors.newFixedThreadPool(procsAvail);
        IntStream.range(0, procsAvail).forEach( it -> executor.submit(() -> {
            heavyTask();
        }));

        if (!executor.awaitTermination(timeout, TimeUnit.SECONDS)) {
            println("Terminate stressing");
            executor.shutdownNow();
            if (!executor.awaitTermination(2, TimeUnit.SECONDS)) {
                println("Could not terminate gracefully ");
            }
        }
        executor.shutdownNow();
        println("Finished.");
    }

    private void heavyTask() {
        int millisToSleep = 10;
        while (!Thread.currentThread().isInterrupted()) {
            fib(40);
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

    static void println(String message) {
        String timestamp = DateTimeFormatter.ofPattern("HH:mm:ss.SSS").format(ZonedDateTime.now());
        System.out.println(timestamp + " - " + message);
    }

}