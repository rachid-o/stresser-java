module com.stresser.generator.cpu {
    requires transitive com.stresser.generator.api;
    exports com.stresser.generator.cpu;
    provides com.stresser.generator.api.StressGenerator with com.stresser.generator.cpu.CpuStresser;
}