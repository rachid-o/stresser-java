package com.stresser.cli;

import com.stresser.generator.api.StressGenerator;

import java.util.ServiceLoader;
import java.util.concurrent.TimeUnit;

public class CliStresser {

    static final int TIME_TO_RUN = 3;

    public static void main(String[] args) throws InterruptedException {

        System.out.println(String.format("Running stresser for %d secs", TIME_TO_RUN));


        ServiceLoader<StressGenerator> loader = ServiceLoader.load(StressGenerator.class);
        if (!loader.iterator().hasNext()) {
            System.out.println("No implementations of StressGenerator found!");
        }
        for (StressGenerator stresser : loader) {
            System.out.println("\tStarting " + stresser.getClass().getSimpleName());
            stresser.start();
        }
        TimeUnit.SECONDS.sleep(TIME_TO_RUN);
        for (StressGenerator stresser : loader) {
            System.out.println("\tStopping " + stresser.getClass().getSimpleName());
            stresser.stop();
        }

    }

}