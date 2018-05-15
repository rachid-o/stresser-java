package com.monitor.cli;

import com.monitor.monitor.JvmMonitor;

import java.util.concurrent.TimeUnit;

public class CliMonitor {

    static final int TIME_TO_RUN = 1000;

    public static void main(String[] args) throws InterruptedException {

        System.out.println(String.format("Running monitor for " + TIME_TO_RUN + " secs"));

        var monitor = new JvmMonitor();

        monitor.start();

        TimeUnit.SECONDS.sleep(TIME_TO_RUN);
        monitor.stop();
    }

}