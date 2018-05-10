package com.stresser.cli;

import com.stresser.generator.cpu.CpuStresser;

public class Main {

    public static void main(String[] args) {
        System.out.println("Starting stresser");

        CpuStresser cpuStresser = new CpuStresser();
        cpuStresser.start();

    }

}