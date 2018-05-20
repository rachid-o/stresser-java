module com.stresser.generator.mem {
    requires transitive com.stresser.generator.api;
    exports com.stresser.generator.mem;
    provides com.stresser.generator.api.StressGenerator with com.stresser.generator.mem.MemoryStresser;
}