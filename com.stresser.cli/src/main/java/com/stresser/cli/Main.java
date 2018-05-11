package com.stresser.cli;

import com.stresser.generator.api.StressGenerator;

import java.util.ServiceLoader;

public class Main {

    public static void main(String[] args) {
        System.out.println("Starting stresser");


        ServiceLoader<StressGenerator> loader = ServiceLoader.load(StressGenerator.class);
        if (!loader.iterator().hasNext()) {
            System.out.println("No implementations of StressGenerator found!");
        }
        for (StressGenerator stresser : loader) {
            System.out.println("\tStarting " + stresser.getClass().getSimpleName());
            stresser.start();
        }
    }

}