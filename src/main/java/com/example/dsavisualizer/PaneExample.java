package com.example.dsavisualizer;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class PaneExample extends Application {

    @Override
    public void start(Stage stage) {
        Pane pane = new Pane();   // Create a Pane

        // Create nodes
        Label label = new Label("Hello Pane");
        Button btn = new Button("Click Me");

        // Set manual positions (x, y)
        label.setLayoutX(1600/2);
        label.setLayoutY(900/2);

        btn.setLayoutX(100);
        btn.setLayoutY(80);

        pane.getChildren().addAll(label, btn);

        Scene scene = new Scene(pane, 1600, 900);
        stage.setScene(scene);
        stage.setTitle("Pane Example");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
