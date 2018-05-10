package com.stresser.cli;

import com.stresser.generator.api.StressGenerator;
import com.stresser.generator.cpu.CpuStresser;

public class Main {

    public static void main(String[] args) {
        System.out.println("Starting stresser");

        StressGenerator cpuStresser = new CpuStresser();
        cpuStresser.start();

    }

}