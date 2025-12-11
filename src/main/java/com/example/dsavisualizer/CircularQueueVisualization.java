package com.example.dsavisualizer;

import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.util.Duration;

public class CircularQueueVisualization {

    private QueueCircular queue;                // keeping your existing queue type
    private NodeBox[] nodeBoxes;
    private final int QUEUE_SIZE = 6;         // <--- 6 nodes as you requested

    double w, h;
    private Pane workArea;
    private HBox root;
    private Label rearLabel, frontLabel;
    private TextArea console;

    // visual tuning (feel free to tweak)
    private final double CENTER_X = 602;      // center of circle (workArea width ~1204/2)
    private final double CENTER_Y = 310;      // center Y
    private final double radius = 200;        // distance from center to node center
    private final double BOX_W = 70;          // assumed nodebox width (for centering labels)
    private final double BOX_H = 40;          // assumed nodebox height

    public CircularQueueVisualization(){
        createUI();
    }

    private void createUI(){
        root = new HBox(20);
        root.setAlignment(Pos.TOP_RIGHT);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: #3D4F40;");

        VBox screen = createScreen();
        VBox controlPanel = createControlPanel();

        HBox.setHgrow(screen, Priority.ALWAYS);
        screen.setFillWidth(true);

        root.getChildren().addAll(screen, controlPanel);
    }

    private VBox createScreen(){
        VBox root=new VBox();
        root.setStyle("-fx-background-color: #2E3A33;-fx-background-radius: 8;");

        HBox header=createHeader();
        Pane workArea=createWorkArea();
        TextArea consoleArea=createConsole();

        root.getChildren().setAll(header,workArea,consoleArea);

        javafx.application.Platform.runLater(() -> {
            w = workArea.getWidth();
            h = workArea.getHeight();
            // debug:
            System.out.println("WORKAREA WIDTH = " + w);
            System.out.println("WORKAREA HEIGHT = " + h);
        });

        return root;
    }

    private HBox createHeader(){
        HBox header=new HBox();
        header.setPadding(new Insets(10,10,0,15));

        Label titleLabel=new Label("Circular Queue Visualization");
        titleLabel.setStyle("-fx-font-size: 25px; -fx-font-weight: bold; -fx-text-fill: white;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button backButton=backButton();

        header.getChildren().addAll(titleLabel,spacer,backButton);
        return header;
    }

    private Button backButton(){
        Button button = new Button("<- Back to menu");
        button.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-text-fill: #95A575; -fx-font-size: 17px; -fx-cursor: hand;");
        button.setOnMouseEntered(e->{
            button.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-text-fill: white; -fx-font-size: 17px; -fx-cursor: hand;");
        });

        button.setOnMouseExited(e-> {
            button.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-text-fill: #95A575; -fx-font-size: 17px; -fx-cursor: hand;");
        });
        button.setOnAction(e-> LandingPage.showQueuePage() );

        return button;
    }

    private Pane createWorkArea() {
        workArea = new Pane();
        workArea.setPrefHeight(626);
        workArea.setPrefWidth(1204);
        //workArea.setStyle("-fx-background-color:red;");

        // Initialize queue and boxes
        queue = new QueueCircular(6);
        nodeBoxes = new NodeBox[QUEUE_SIZE];

        // Place nodes around a circle (index 0 at top, then clockwise)
        for (int i = 0; i < QUEUE_SIZE; i++) {
            double angleDeg = -90 + i * (360.0 / QUEUE_SIZE); // -90 so index 0 is top
            double angleRad = Math.toRadians(angleDeg);

            double nodeCenterX = CENTER_X + radius * Math.cos(angleRad);
            double nodeCenterY = CENTER_Y + radius * Math.sin(angleRad);

            nodeBoxes[i] = new NodeBox("null");
            // position NodeBox so its center matches computed nodeCenter
            nodeBoxes[i].setLayoutX(nodeCenterX - BOX_W / 2);
            nodeBoxes[i].setLayoutY(nodeCenterY - BOX_H / 2);

            workArea.getChildren().add(nodeBoxes[i]);
        }

        // Create REAR label once (above the node)
        rearLabel = new Label("rear");
        rearLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: white;");
        // Initially place at node 0 position, adjust offset later as translate is used
        rearLabel.setLayoutX(nodeBoxes[0].getLayoutX()+80);
        rearLabel.setLayoutY(nodeBoxes[0].getLayoutY() - 40);
        rearLabel.setVisible(false);
        workArea.getChildren().add(rearLabel);

        // FRONT label (below the node)
        frontLabel = new Label("front");
        frontLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: white;");
        frontLabel.setLayoutX(nodeBoxes[0].getLayoutX());
        frontLabel.setLayoutY(nodeBoxes[0].getLayoutY() + BOX_H + 10);
        frontLabel.setVisible(false);
        workArea.getChildren().add(frontLabel);

        return workArea;
    }

    private TextArea createConsole(){
        console = new TextArea();
        console.setEditable(false);
        console.setWrapText(true);
        console.setStyle(
                "-fx-control-inner-background: black;-fx-text-fill: #7CFC00;-fx-font-family: Consolas;-fx-font-size: 14px;-fx-background-color: black;-fx-background-insets: 0;-fx-background-radius: 0;-fx-border-color: transparent;-fx-border-width: 0;"
        );
        console.setPrefHeight(125);
        return console;
    }

    private void log(String msg) {
        int f = queue.frontIndex();
        int r = queue.rearIndex();

        console.appendText(
                msg +
                        "\nFront=" + f + "   |   Rear=" + r + "\n\n"
        );
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

        TextField valueEnq=new TextField();
        valueEnq.setPromptText("e.g., 42");
        valueEnq.setPrefHeight(45);
        valueEnq.setStyle("-fx-background-color: #3D4F40;-fx-text-fill: white;-fx-font-size: 16px;");

        Button enqueueButton=controlPanelButton("+ Enqueue");
        enqueueButton.setDefaultButton(true);
        enqueueButton.setOnAction(e -> {
            String text = valueEnq.getText();
            if (text.isEmpty()) return;
            try {
                int value = Integer.parseInt(text);
                enqueue(value);
            } catch (NumberFormatException ex) {
                System.out.println("Invalid value");
            }
        });

        Button dequeueButton=controlPanelButton("- Dequeue");
        dequeueButton.setOnAction(e -> dequeue());


        root.getChildren().addAll(titleLabel,valueLabel,valueEnq,enqueueButton,dequeueButton);
        return root;
    }

    private void enqueue(int value) {

        if (queue.isFull()) {
            log("Queue is FULL! Cannot enqueue " + value);
            return;
        }

        queue.enqueue(value);

        int rear = queue.rearIndex();
        nodeBoxes[rear].setValue(value);

        rearLabel.setVisible(true);

        // FIRST ENQUEUE -> show front pointer at index 0 (circular)
        if (queue.size() == 1) {
            frontLabel.setVisible(true);
            // place front label visually under node 0
            Point2D p0 = nodeCenter(0);
            frontLabel.setLayoutX((p0.getX() - BOX_W/2)+25);
            frontLabel.setLayoutY(p0.getY() + BOX_H/2 + 25);
        }

        // Move rear pointer to new node (animated)
        moveRearPointerToIndex(rear);

        // LOG HERE
        log(value + " is enqueued");
    }

    private void moveRearPointerToIndex(int index) {
        Point2D target = nodeCenter(index);

        // compute current absolute position of the label
        double currentX = rearLabel.getLayoutX() + rearLabel.getTranslateX();
        double currentY = rearLabel.getLayoutY() + rearLabel.getTranslateY();

        // target desired label position (above node)
        double targetX = (target.getX() - BOX_W/2)+28;
        double targetY = target.getY() - BOX_H/2 - 40;

        TranslateTransition tt = new TranslateTransition(Duration.seconds(0.45), rearLabel);
        tt.setByX(targetX - currentX);
        tt.setByY(targetY - currentY);
        tt.play();
    }
/*
    private void moveFrontPointerToIndex(int index) {
        Point2D target = nodeCenter(index);

        double currentX = frontLabel.getLayoutX() + frontLabel.getTranslateX();
        double currentY = frontLabel.getLayoutY() + frontLabel.getTranslateY();

        double targetX = target.getX() - BOX_W/2;
        double targetY = target.getY() - BOX_H/2 + BOX_H + 10;

        TranslateTransition tt = new TranslateTransition(Duration.seconds(0.45), frontLabel);
        tt.setByX(targetX - currentX);
        tt.setByY(targetY - currentY);
        tt.play();
    }*/

    private void moveFrontPointerToIndex(int index) {

        Point2D target = nodeCenter(index);

        // current absolute position
        double currentX = frontLabel.getLayoutX() + frontLabel.getTranslateX();
        double currentY = frontLabel.getLayoutY() + frontLabel.getTranslateY();

        // center front label under the box
        double targetX = (target.getX() - frontLabel.getWidth() / 2)+10;
        double targetY = target.getY() + BOX_H/2 + 25;

        TranslateTransition tt = new TranslateTransition(Duration.seconds(0.45), frontLabel);
        tt.setByX(targetX - currentX);
        tt.setByY(targetY - currentY);
        tt.play();
    }


    // compute the center point of nodeBoxes[index] (based on how we placed them)
    private Point2D nodeCenter(int index) {
        double x = nodeBoxes[index].getLayoutX() + BOX_W/2;
        double y = nodeBoxes[index].getLayoutY() + BOX_H/2;
        return new Point2D(x, y);
    }

    private void dequeue() {

        if (queue.isEmpty()) {
            log("Queue is EMPTY! Cannot dequeue.");
            return;
        }

        // remove value from queue (assumed to return removed value)
        int removed = queue.dequeue();
        // new front index after dequeue
        int front = queue.frontIndex();

        // compute old front index (the index we just cleared)
        int clearedIndex = (front - 1 + QUEUE_SIZE) % QUEUE_SIZE;
        nodeBoxes[clearedIndex].setValue("null");

        log(removed + " is dequeued");

        if (!queue.isEmpty()) {
            frontLabel.setVisible(true);
            moveFrontPointerToIndex(front);   // animate to new front
        } /*else {
            // queue became empty -> reset visuals
            frontLabel.setVisible(false);
            rearLabel.setVisible(false);
            for (NodeBox box : nodeBoxes) box.setValue("null");
            log("Queue is now EMPTY.");
        }*/
     else {
        // Queue became empty -> reset visuals AND pointer positions
        for (NodeBox box : nodeBoxes) {
            box.setValue("null");
        }

        // Hide pointers
        rearLabel.setVisible(false);
        frontLabel.setVisible(false);

        // Reset translation offsets and layout positions for rear
        rearLabel.setTranslateX(0);
        rearLabel.setTranslateY(0);
        rearLabel.setLayoutX(nodeBoxes[0].getLayoutX());
        rearLabel.setLayoutY(nodeBoxes[0].getLayoutY() - 40);

        // Reset translation offsets and layout positions for front
        frontLabel.setTranslateX(0);
        frontLabel.setTranslateY(0);
        frontLabel.setLayoutX(nodeBoxes[0].getLayoutX());
        frontLabel.setLayoutY(nodeBoxes[0].getLayoutY() + BOX_H + 10);

        log("Queue is now EMPTY.");
    }

}

    private Button controlPanelButton(String string){
        Button button = new Button(string);
        button.setPrefWidth(130);
        button.setStyle("-fx-background-color: #9FB873; -fx-border-color: transparent; -fx-text-fill: white; -fx-font-size: 15px; -fx-cursor: hand;");
        button.setOnMouseEntered(e->{
            button.setStyle("-fx-background-color: white; -fx-border-color: transparent; -fx-text-fill: #9FB873; -fx-font-size: 15px; -fx-cursor: hand;");
        });

        button.setOnMouseExited(e-> {
            button.setStyle("-fx-background-color: #9FB873; -fx-border-color: transparent; -fx-text-fill: white; -fx-font-size: 15px; -fx-cursor: hand;");
        });

        return button;
    }

    public HBox getRoot(){return root;}

}
