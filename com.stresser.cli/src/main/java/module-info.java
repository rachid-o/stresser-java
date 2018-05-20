module com.stresser.cli {
    requires com.stresser.generator.api;
    requires com.stresser.generator.cpu;
    requires com.stresser.generator.mem;
    uses com.stresser.generator.api.StressGenerator;
}