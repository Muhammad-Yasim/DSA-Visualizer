package com.example.dsavisualizer;

import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.util.Duration;

public class StackVisualization{

    private Stacks stack;
    private NodeBoxStack[] nodeBoxes;
    private final int STACK_SIZE=7;

    private Pane workArea;
    private HBox root;
    private TextArea console;

    private final double stackX=520;
    private final double spawnX=750;
    private double bottomY;
    private double boxHeight;

    public StackVisualization(){
        createUI();
    }

    private void createUI(){
        root=new HBox(20);
        root.setAlignment(Pos.TOP_RIGHT);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: #3D4F40;");

        VBox screen=createScreen();
        VBox controlPanel=createControlPanel();

        HBox.setHgrow(screen,Priority.ALWAYS);
        screen.setFillWidth(true);

        root.getChildren().addAll(screen,controlPanel);
    }

    private VBox createScreen(){
        VBox root=new VBox();
        root.setStyle("-fx-background-color: #2E3A33;-fx-background-radius: 8;");

        HBox header=createHeader();
        Pane workArea=createWorkArea();
        TextArea consoleArea=createConsole();

        root.getChildren().setAll(header,workArea,consoleArea);
        return root;
    }

    private HBox createHeader(){
        HBox header=new HBox();
        header.setPadding(new Insets(10,10,0,15));

        Label titleLabel=new Label("Stack Visualization");
        titleLabel.setStyle("-fx-font-size: 25px; -fx-font-weight: bold; -fx-text-fill: white;");

        Region spacer=new Region();
        HBox.setHgrow(spacer,Priority.ALWAYS);

        Button backButton=backButton();

        header.getChildren().addAll(titleLabel,spacer,backButton);
        return header;
    }

    private Button backButton(){
        Button button=new Button("<- Back to menu");
        button.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-text-fill: #95A575; -fx-font-size: 17px; -fx-cursor: hand;");
        button.setOnMouseEntered(e->{
            button.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-text-fill: white; -fx-font-size: 17px; -fx-cursor: hand;");
        });

        button.setOnMouseExited(e->{
            button.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-text-fill: #95A575; -fx-font-size: 17px; -fx-cursor: hand;");
        });
        button.setOnAction(e-> App.showLandingPage());

        return button;
    }

    private Pane createWorkArea(){
        workArea=new Pane();
        workArea.setPrefHeight(626);
        workArea.setPrefWidth(1204);

        stack=new Stacks();
        nodeBoxes=new NodeBoxStack[STACK_SIZE];

        NodeBoxStack temp=new NodeBoxStack("null");
        workArea.getChildren().add(temp);
        temp.applyCss();
        temp.layout();
        boxHeight=temp.getBoundsInParent().getHeight();
        if(boxHeight<=0) boxHeight=90;
        workArea.getChildren().remove(temp);

        bottomY=500;

        return workArea;
    }

    private TextArea createConsole(){
        console=new TextArea();
        console.setEditable(false);
        console.setWrapText(true);
        console.setStyle("-fx-control-inner-background: black;-fx-text-fill: #7CFC00;-fx-font-family: Consolas;-fx-font-size: 14px;-fx-background-color: black;-fx-background-insets: 0;-fx-background-radius: 0;-fx-border-color: transparent;-fx-border-width: 0;");
        console.setPrefHeight(125);
        return console;
    }

    private void log(String msg){
        console.appendText(msg+"\n\n");
    }

    private VBox createControlPanel(){
        VBox root=new VBox(20);
        root.setPrefWidth(300);
        root.setStyle("-fx-background-color: #2E3A33;-fx-background-radius: 8;");
        root.setPadding(new Insets(25));

        Label titleLabel=new Label("Controls");
        titleLabel.setStyle("-fx-font-size: 25px; -fx-font-weight: bold; -fx-text-fill: white;");

        Label valueLabel=new Label("Value");
        valueLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: #95A575;");

        TextField valuePush=new TextField();
        valuePush.setPromptText("e.g., 42");
        valuePush.setPrefHeight(45);
        valuePush.setStyle("-fx-background-color: #3D4F40;-fx-text-fill: white;-fx-font-size: 16px;");

        Button pushButton=controlPanelButton("Push");
        pushButton.setDefaultButton(true);
        pushButton.setOnAction(e->{
            String text=valuePush.getText();
            if(text.isEmpty()) return;
            try{
                int value=Integer.parseInt(text);
                push(value);
            }catch(NumberFormatException ex){
                log("Invalid value");
            }
        });

        Button popButton=controlPanelButton("Pop");
        popButton.setOnAction(e-> pop());

        root.getChildren().addAll(titleLabel,valueLabel,valuePush,pushButton,popButton);
        return root;
    }

    private void push(int value){
        if(stack.isFull()){
            log("Stack is FULL! Cannot push "+value);
            return;
        }

        stack.push(value);
        int idx=stack.size()-1; // visual index (0 bottom)

        NodeBoxStack box=new NodeBoxStack(value);
        box.setLayoutX(spawnX);
        box.setLayoutY(bottomY - idx*boxHeight);
        workArea.getChildren().add(box);
        nodeBoxes[idx]=box;

        double targetX=stackX;

        TranslateTransition tt=new TranslateTransition(Duration.millis(300),box);
        tt.setToX(targetX - spawnX);
        tt.setToY(0);
        tt.play();

        box.setLayoutX(targetX);

        log(value+" is pushed");
    }

    private void pop(){
        if(stack.isEmpty()){
            log("Stack is EMPTY! Cannot pop.");
            return;
        }

        int removed=stack.pop();
        int idx=stack.size(); // index of box to remove
        NodeBoxStack box=nodeBoxes[idx];

        if(box!=null){
            TranslateTransition tt=new TranslateTransition(Duration.millis(300),box);
            tt.setToX(spawnX - stackX);
            tt.setOnFinished(e-> workArea.getChildren().remove(box));
            tt.play();
            nodeBoxes[idx]=null;
        }

        log(removed+" is popped");

        if(stack.isEmpty()){
            log("Stack is now EMPTY.");
        }
    }

    private Button controlPanelButton(String string){
        Button button=new Button(string);
        button.setPrefWidth(130);
        button.setStyle("-fx-background-color: #9FB873; -fx-border-color: transparent; -fx-text-fill: white; -fx-font-size: 15px; -fx-cursor: hand;");
        button.setOnMouseEntered(e->{
            button.setStyle("-fx-background-color: white; -fx-border-color: transparent; -fx-text-fill: #9FB873; -fx-font-size: 15px; -fx-cursor: hand;");
        });

        button.setOnMouseExited(e->{
            button.setStyle("-fx-background-color: #9FB873; -fx-border-color: transparent; -fx-text-fill: white; -fx-font-size: 15px; -fx-cursor: hand;");
        });
        return button;
    }

    public HBox getRoot(){return root;}

}
