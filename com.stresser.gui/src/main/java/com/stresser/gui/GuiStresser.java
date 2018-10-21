package com.stresser.gui;

import com.stresser.generator.api.StressGenerator;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ServiceLoader;


public class GuiStresser extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Stresser");

        ServiceLoader<StressGenerator> loader = ServiceLoader.load(StressGenerator.class);
        
        if (!loader.iterator().hasNext()) {
            System.out.println("WARN: No implementations of StressGenerator found!");
        }
        var vbox = new VBox();
        for (StressGenerator stresser : loader) {
            String stresserName = stresser.getClass().getSimpleName();
            System.out.println("\tAdding " + stresserName);

            Button btnStart = new Button("Start");
            Button btnStop = new Button("Stop");
            btnStart.setOnAction(event -> stresser.start());
            btnStop.setOnAction(event -> stresser.stop());

            var hbox = new HBox();
            hbox.setPadding(new Insets(3));
            hbox.setSpacing(5);
            hbox.getChildren().add(btnStart);
            hbox.getChildren().add(btnStop);
            Label label = new Label(stresserName);
            hbox.setAlignment(Pos.CENTER_LEFT);
            hbox.getChildren().add(label);

            vbox.getChildren().add(hbox);
        }

        Button btnGC= new Button("Run GC");
        vbox.getChildren().add(btnGC);
        btnGC.setOnAction( event -> {
            println("Start of GC");
            System.gc();
            println("End of GC");

        });

        BorderPane pane = new BorderPane();
        pane.setLeft(vbox);
        primaryStage.setScene(new Scene(pane, 300, 200));
        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> {
            for (StressGenerator stresser : loader) {
                System.out.println("\tStopping " + stresser.getClass().getSimpleName());
                stresser.stop();
            }
//            Platform.exit();
//            System.exit(0);
        });

    }

    static void println(Object message) {
        String timestamp = DateTimeFormatter.ofPattern("HH:mm:ss.SSS").format(ZonedDateTime.now());
        System.out.println(timestamp + " - " + message);
    }

}
