package com.example.dsavisualizer;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class QueuePage {

    private VBox root;

    public QueuePage(){
        createUI();
    }

    private void createUI(){

        root = new VBox();
        root.setAlignment(Pos.TOP_CENTER);
        root.setSpacing(20);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #2E3A33;");

        HBox header = createHeader();
        HBox menu=createMenu();

        root.getChildren().addAll(header,menu);

    }

    private HBox createHeader(){

        HBox root=new HBox();
        root.setPadding(new Insets(30,20,20,20));
        root.setPrefHeight(100);
        root.setAlignment(Pos.TOP_CENTER);

        //BACK BUTTON HERE
        Button backButton = backButton();

        //SPACERS FOR CENTER ALIGN
        Region leftSpacer = new Region();
        Region rightSpacer = new Region();
        HBox.setHgrow(leftSpacer, Priority.ALWAYS);
        HBox.setHgrow(rightSpacer, Priority.ALWAYS);

        //LABELS HERE
        VBox labels=new VBox();
        labels.setAlignment(Pos.CENTER);

        Label titleLabel=new Label("Queue Types");
        titleLabel.setStyle("-fx-font-size: 35px; -fx-font-weight: bold; -fx-text-fill: white;");

        Label descriptionLabel=new Label("Select a queue implementation to visualize.");
        descriptionLabel.setStyle("-fx-font-size: 15px; -fx-text-fill:#95A575 ;");

        labels.getChildren().addAll(titleLabel,descriptionLabel);


        root.getChildren().addAll(backButton,leftSpacer,labels,rightSpacer);
        return root;
    }

    private Button backButton(){
        Button button = new Button("<- Back");
        button.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-text-fill: #95A575; -fx-font-size: 17px; -fx-cursor: hand;");
        button.setOnMouseEntered(e->{
            button.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-text-fill: white; -fx-font-size: 17px; -fx-cursor: hand;");
        });

        button.setOnMouseExited(e-> {
            button.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-text-fill: #95A575; -fx-font-size: 17px; -fx-cursor: hand;");
        });
        button.setOnAction(e-> App.showLandingPage());

        return button;
    }

    private HBox createMenu(){
        HBox root=new HBox(20);
        root.setPrefHeight(700);
        root.setAlignment(Pos.TOP_CENTER);

        //LEFT SIDE HERE
        VBox leftSide=new VBox(80);
        leftSide.setAlignment(Pos.TOP_RIGHT);
        leftSide.setPadding(new Insets(60,20,0,0));

        Button linearQB=menuButton("Linear Queue");
        //linearQB.setOnAction(e->showLinearQueueVisualization());

        Button priorityQB=menuButton("Priority Queue");
        //priorityQB.setOnAction(e->showPriorityQueueVisualization());

        Button doubleEndedCircularQB=menuButton("Double Ended Circular Queue");
        //doubleEndedCircularQB.setOnAction(e->showDECircularQueueVisualization());

        leftSide.getChildren().addAll(linearQB,priorityQB,doubleEndedCircularQB);


        //RIGHT SIDE HERE
        VBox rightSide=new VBox(80);
        rightSide.setAlignment(Pos.TOP_LEFT);
        rightSide.setPadding(new Insets(60,0,0,20));

        Button circularQB=menuButton("Circular Queue");
        //circularQB.setOnAction(e->showCircularQueueVisualization());

        Button doubleEndedLinearQB=menuButton("Double Ended Linear Queue");
        //doubleEndedLinearQB.setOnAction(e->showDELinearQueueVisualization());

        rightSide.getChildren().addAll(circularQB,doubleEndedLinearQB);



        root.getChildren().addAll(leftSide,rightSide);
        return root;
    }


    private Button menuButton(String str){
        Button button = new Button(str);
        button.setPrefWidth(300);
        button.setPrefHeight(60);
        button.setStyle("-fx-background-color: #3D4F40; -fx-text-fill: white; -fx-font-size: 15px; -fx-font-weight: bold; -fx-background-radius: 8;-fx-cursor: hand;");


        TranslateTransition hoverIn = new TranslateTransition(Duration.millis(200), button);
        hoverIn.setToY(-3);

        TranslateTransition hoverOut = new TranslateTransition(Duration.millis(200), button);
        hoverOut.setToY(0);

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
/*
    private void showLinearQueueVisualization(){
        LinearQueueVisualization linearQueueVisualization=new LinearQueueVisualization();
        Scene scene = new Scene(linearQueueVisualization.getRoot(), App.WINDOW_WIDTH, App.WINDOW_HEIGHT);
        App.getPrimaryStage().setScene(scene);
    }

    private void showCircularQueueVisualization(){
        CircularQueueVisualization circularQueueVisualization=new CircularQueueVisualization();
        Scene scene = new Scene(circularQueueVisualization.getRoot(), App.WINDOW_WIDTH, App.WINDOW_HEIGHT);
        App.getPrimaryStage().setScene(scene);
    }

    private void showPriorityQueueVisualization(){
        PriorityQueueVisualization priorityQueueVisualization=new PriorityQueueVisualization();
        Scene scene = new Scene(priorityQueueVisualization.getRoot(), App.WINDOW_WIDTH, App.WINDOW_HEIGHT);
        App.getPrimaryStage().setScene(scene);
    }

    private void showDELinearQueueVisualization(){
        DELinearQueueVisualization dELinearQueueVisualization=new DELinearQueueVisualization();
        Scene scene = new Scene(dELinearQueueVisualization.getRoot(), App.WINDOW_WIDTH, App.WINDOW_HEIGHT);
        App.getPrimaryStage().setScene(scene);
    }

    private void showDECircularQueueVisualization(){
        DECircularQueueVisualization dECircularQueueVisualization=new DECircularQueueVisualization();
        Scene scene = new Scene(dECircularQueueVisualization.getRoot(), App.WINDOW_WIDTH, App.WINDOW_HEIGHT);
        App.getPrimaryStage().setScene(scene);
    }
*/
    public VBox getRoot(){return root;}
}
