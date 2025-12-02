package com.example.dsavisualizer;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {


    public static final int WINDOW_WIDTH = 1600;
    public static final int WINDOW_HEIGHT = 900;

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) {

        primaryStage=stage;

        primaryStage.setTitle("DSA App");
        primaryStage.setResizable(false);
        primaryStage.setWidth(WINDOW_WIDTH);
        primaryStage.setHeight(WINDOW_HEIGHT);

        showLandingPage();

        primaryStage.show();
    }

    public static void showLandingPage() {
        LandingPage landingPage = new LandingPage();
        Scene scene = new Scene(landingPage.getRoot(), WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setScene(scene);
    }

    public static Stage getPrimaryStage(){
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }


}