package com.example.dsavisualizer;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class LandingPage {
    private VBox root;

    public LandingPage() {
        createUI();
    }

    private void createUI() {
        root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setSpacing(20);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #2E3A33;");

        VBox menuOptions = createMenuOptions();
        HBox exit = createExit();

        root.getChildren().addAll(menuOptions,exit);
    }

    private VBox createMenuOptions(){
        VBox root = new VBox(55);
        root.setPrefHeight(700);
        root.setAlignment(Pos.CENTER);

        //TITLE HERE
        VBox labels=new VBox();
        labels.setAlignment(Pos.CENTER);

        Label titleLabel=new Label("DSA Visualizer");
        titleLabel.setStyle("-fx-font-size: 45px; -fx-font-weight: bold; -fx-text-fill: white;");

        Label descriptionLabel=new Label("Choose a data structure to begin.");
        descriptionLabel.setStyle("-fx-font-size: 16px; -fx-text-fill:#95A575 ;");

        labels.getChildren().addAll(titleLabel,descriptionLabel);

        //BUTTONS HERE
        VBox buttons=new VBox(20);
        buttons.setAlignment(Pos.CENTER);

        Button queueButton = menuButton("QUEUE");
        queueButton.setOnAction(e->showQueuePage());

        Button linkListButton=menuButton("LINKED LIST");
        linkListButton.setOnAction(e->showLinkedListPage());

        buttons.getChildren().addAll(queueButton,linkListButton);


        root.getChildren().addAll(labels,buttons);

        return root;
    }

    private Button menuButton(String str){
        Button button = new Button(str);
        button.setPrefWidth(300);
        button.setPrefHeight(60);
        button.setStyle("-fx-background-color: #3D4F40; -fx-text-fill: white; -fx-font-size: 15px; -fx-font-weight: bold; -fx-background-radius: 8;-fx-cursor: hand;");


        TranslateTransition hoverIn = new TranslateTransition(Duration.millis(200), button);
        hoverIn.setToY(-3);   //Move up 3 pixels

        TranslateTransition hoverOut = new TranslateTransition(Duration.millis(200), button);
        hoverOut.setToY(0);   //Move back to original position

        button.setOnMouseEntered(e -> {
            hoverIn.play();
            button.setStyle("-fx-background-color: #4D634D; -fx-text-fill:white; -fx-font-size: 15px; -fx-font-weight: bold; -fx-background-radius: 8;-fx-cursor: hand;");
        });

        button.setOnMouseExited(e -> {
            hoverOut.play();
            button.setStyle("-fx-background-color: #3D4F40; -fx-text-fill:white; -fx-font-size: 15px; -fx-font-weight: bold; -fx-background-radius: 8;-fx-cursor: hand;");
        });

        return button;
    }

    private HBox createExit(){
        HBox root = new HBox();
        root.setPrefHeight(100);
        root.setAlignment(Pos.BOTTOM_RIGHT);

        Button exitButton = new Button("Exit App");
        exitButton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-text-fill: #95A575; -fx-font-size: 17px; -fx-cursor: hand;");
        exitButton.setOnMouseEntered(e->{
            exitButton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-text-fill: white; -fx-font-size: 17px; -fx-cursor: hand;");
        });

        exitButton.setOnMouseExited(e-> {
            exitButton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-text-fill: #95A575; -fx-font-size: 17px; -fx-cursor: hand;");
        });
        exitButton.setOnAction(e-> Platform.exit());

        root.getChildren().addAll(exitButton);

        return root;
    }



    protected static void showQueuePage() {
        QueuePage addQueuePage = new QueuePage();
        Scene scene = new Scene(addQueuePage.getRoot(), App.WINDOW_WIDTH, App.WINDOW_HEIGHT);
        App.getPrimaryStage().setScene(scene);
    }

    protected static void showLinkedListPage(){
        LinkedListPage addLinkedListPage=new LinkedListPage();
        Scene scene = new Scene(addLinkedListPage.getRoot(), App.WINDOW_WIDTH, App.WINDOW_HEIGHT);
        App.getPrimaryStage().setScene(scene);
    }

    public VBox getRoot(){return root;}
}