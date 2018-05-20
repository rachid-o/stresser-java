module com.stresser.gui {
    exports com.stresser.gui to javafx.graphics;
    requires javafx.graphics;
    requires javafx.controls;

    requires com.stresser.generator.api;
    uses com.stresser.generator.api.StressGenerator;
}