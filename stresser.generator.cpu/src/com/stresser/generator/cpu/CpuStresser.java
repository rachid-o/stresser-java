package com.stresser.generator.cpu;

public class CpuStresser {

    public String getName() {
        return this.getClass().getName();
    }

    public void startStressing() {
        System.out.println(getName() + " Start stressing the CPU");
    }

    public void stopStressing() {
        System.out.println(getName() + " Stop stressing the CPU");
    }

}