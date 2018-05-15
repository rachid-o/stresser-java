package com.monitor.monitor;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class JvmMonitor {

    private ScheduledExecutorService scheduler;

    public void start() {
        println("started");
        var url = "localhost";
        var port = 31337;
        JmxReader reader = new JmxReader(url, port);

        scheduler = Executors.newSingleThreadScheduledExecutor();

        scheduler.scheduleAtFixedRate(() -> {
            println(reader.read());
        }, 0, 1, TimeUnit.SECONDS);
    }

    public void stop() {
        if(scheduler == null) {
            throw new IllegalStateException("scheduler was not started");
        }
        scheduler.shutdownNow();
        scheduler = null;
        println("stopped");
    }

    void println(Object message) {
        String timestamp = DateTimeFormatter.ofPattern("HH:mm:ss.SSS").format(ZonedDateTime.now());
        System.out.println(timestamp + " " + getClass().getSimpleName() +" - " + message);
    }
}
