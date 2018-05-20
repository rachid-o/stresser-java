module com.stresser.generator.implementations {
    requires transitive com.stresser.generator.api;
    exports com.stresser.generator.implementations.cpu;
    exports com.stresser.generator.implementations.mem;
    provides com.stresser.generator.api.StressGenerator with com.stresser.generator.implementations.cpu.CpuStresser;
//    provides com.stresser.generator.api.StressGenerator with com.stresser.generator.implementations.mem.MemoryStresser;
}