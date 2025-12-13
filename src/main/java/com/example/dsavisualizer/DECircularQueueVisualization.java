package com.example.dsavisualizer;

import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Duration;

public class DECircularQueueVisualization {

    private QueueDoublyCircular queue;
    private NodeBox[] nodeBoxes;
    private final int QUEUE_SIZE = 6;

    private Pane workArea;
    private HBox root;
    private Label frontLabel, rearLabel;
    private TextArea console;

    private final double CENTER_X = 602;
    private final double CENTER_Y = 310;
    private final double radius = 200;
    private final double BOX_W = 70;
    private final double BOX_H = 40;

    public DECircularQueueVisualization() {
        createUI();
    }

    private void createUI() {
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

    private VBox createScreen() {
        VBox root = new VBox();
        root.setStyle("-fx-background-color: #2E3A33;-fx-background-radius: 8;");

        HBox header = createHeader();
        Pane workArea = createWorkArea();
        TextArea consoleArea = createConsole();

        root.getChildren().setAll(header, workArea, consoleArea);
        return root;
    }
    private HBox createHeader(){
        HBox header=new HBox();
        header.setPadding(new Insets(10,10,0,15));

        Label titleLabel=new Label("Double Ended Circular Queue Visualization");
        titleLabel.setStyle("-fx-font-size: 25px; -fx-font-weight: bold; -fx-text-fill: white;");

        //Spacer Region
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
        workArea.setPrefSize(1204, 626);

        queue = new QueueDoublyCircular(QUEUE_SIZE);
        nodeBoxes = new NodeBox[QUEUE_SIZE];

        //Place nodes in a circle
        for (int i = 0; i < QUEUE_SIZE; i++) {
            double angleDeg = -90 + i * (360.0 / QUEUE_SIZE);
            double angleRad = Math.toRadians(angleDeg);

            double x = CENTER_X + radius * Math.cos(angleRad);
            double y = CENTER_Y + radius * Math.sin(angleRad);

            nodeBoxes[i] = new NodeBox("null");
            nodeBoxes[i].setLayoutX(x - BOX_W / 2);
            nodeBoxes[i].setLayoutY(y - BOX_H / 2);
            workArea.getChildren().add(nodeBoxes[i]);
        }

        frontLabel = new Label("front");
        frontLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: white;");
        frontLabel.setLayoutX(nodeBoxes[0].getLayoutX());
        frontLabel.setLayoutY(nodeBoxes[0].getLayoutY() + BOX_H + 10);
        frontLabel.setVisible(false);
        workArea.getChildren().add(frontLabel);

        rearLabel = new Label("rear");
        rearLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: white;");
        rearLabel.setLayoutX(nodeBoxes[0].getLayoutX());
        rearLabel.setLayoutY(nodeBoxes[0].getLayoutY() - 40);
        rearLabel.setVisible(false);
        workArea.getChildren().add(rearLabel);

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

    private VBox createControlPanel() {
        VBox root = new VBox(20);
        root.setPrefWidth(300);
        root.setPadding(new Insets(25));
        root.setStyle("-fx-background-color: #2E3A33;-fx-background-radius: 8;");

        Label titleLabel = new Label("Controls");
        titleLabel.setStyle("-fx-font-size: 25px; -fx-font-weight: bold; -fx-text-fill: white;");

        Label valueLabel = new Label("Value");
        valueLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: #95A575;");

        TextField valueField = new TextField();
        valueField.setPromptText("e.g., 42");
        valueField.setPrefHeight(45);
        valueField.setStyle("-fx-background-color: #3D4F40;-fx-text-fill: white;-fx-font-size: 16px;");

        Button enqFront = controlPanelButton("+ Enqueue Front");
        enqFront.setOnAction(e -> {
            String text = valueField.getText();
            if (!text.isEmpty()) {
                try {
                    enqueueFront(Integer.parseInt(text));
                }catch (NumberFormatException ex) {
                    log("Invalid value");
                }
            }
        });

        Button enqBack = controlPanelButton("+ Enqueue Back");
        enqBack.setOnAction(e -> {
            String text = valueField.getText();
            if (!text.isEmpty()) {
                try {
                    enqueueBack(Integer.parseInt(text));
                } catch (NumberFormatException ex) {
                    log("Invalid value");
                }
            }
        });

        Button deqFront = controlPanelButton("- Dequeue Front");
        deqFront.setOnAction(e -> dequeueFront());

        Button deqBack = controlPanelButton("- Dequeue Back");
        deqBack.setOnAction(e -> dequeueBack());

        root.getChildren().addAll(titleLabel, valueLabel, valueField, enqFront, enqBack, deqFront, deqBack);
        return root;
    }


    private Button controlPanelButton(String string){
        Button button = new Button(string);
        button.setPrefWidth(250);
        button.setStyle("-fx-background-color: #9FB873; -fx-border-color: transparent; -fx-text-fill: white; -fx-font-size: 15px; -fx-cursor: hand;");
        button.setOnMouseEntered(e->{
            button.setStyle("-fx-background-color: white; -fx-border-color: transparent; -fx-text-fill: #9FB873; -fx-font-size: 15px; -fx-cursor: hand;");
        });

        button.setOnMouseExited(e-> {
            button.setStyle("-fx-background-color: #9FB873; -fx-border-color: transparent; -fx-text-fill: white; -fx-font-size: 15px; -fx-cursor: hand;");
        });

        return button;
    }

    private void enqueueFront(int value) {
        if (queue.size() == QUEUE_SIZE) {
            log("Queue FULL! Cannot enqueue at front.");
            return;
        }
        queue.enqueueFront(value);
        int front = queue.frontIndex();
        nodeBoxes[front].setValue(value);
        frontLabel.setVisible(true);
        rearLabel.setVisible(queue.size() > 1);
        moveFrontPointerToIndex(front);
        log(value + " enqueued at front");
    }

    private void enqueueBack(int value) {
        if (queue.size() == QUEUE_SIZE) {
            log("Queue FULL! Cannot enqueue at back.");
            return;
        }
        queue.enqueueBack(value);
        int rear = queue.rearIndex();
        nodeBoxes[rear].setValue(value);
        rearLabel.setVisible(true);
        frontLabel.setVisible(queue.size() > 1);
        moveRearPointerToIndex(rear);
        log(value + " enqueued at back");
    }

    private void dequeueFront() {
        if (queue.size() == 0) {
            log("Queue empty!");
            return;
        }
        int oldFront = queue.frontIndex();
        int val = queue.dequeueFront();
        nodeBoxes[oldFront].setValue("null");
        log(val + " dequeued from front");

        if (queue.size() > 0) moveFrontPointerToIndex(queue.frontIndex());
        else resetPointers();
    }

    private void dequeueBack() {
        if (queue.size() == 0) {
            log("Queue empty!");
            return;
        }
        int oldRear = queue.rearIndex();
        int val = queue.dequeueBack();
        nodeBoxes[oldRear].setValue("null");
        log(val + " dequeued from back");

        if (queue.size() > 0) moveRearPointerToIndex(queue.rearIndex());
        else resetPointers();
    }

    private void moveFrontPointerToIndex(int index) {
        Point2D target = nodeCenter(index);
        double currentX = frontLabel.getLayoutX() + frontLabel.getTranslateX();
        double currentY = frontLabel.getLayoutY() + frontLabel.getTranslateY();

        double targetX = (target.getX() - BOX_W / 2)+18;
        double targetY = target.getY() + BOX_H / 2 + 20;

        TranslateTransition tt = new TranslateTransition(Duration.seconds(0.45), frontLabel);
        tt.setByX(targetX - currentX);
        tt.setByY(targetY - currentY);
        tt.play();
    }

    private void moveRearPointerToIndex(int index) {
        Point2D target = nodeCenter(index);
        double currentX = rearLabel.getLayoutX() + rearLabel.getTranslateX();
        double currentY = rearLabel.getLayoutY() + rearLabel.getTranslateY();

        double targetX = (target.getX() - BOX_W / 2)+25;
        double targetY = target.getY() - BOX_H / 2 - 40;

        TranslateTransition tt = new TranslateTransition(Duration.seconds(0.45), rearLabel);
        tt.setByX(targetX - currentX);
        tt.setByY(targetY - currentY);
        tt.play();
    }

    private void resetPointers() {
        for (NodeBox box : nodeBoxes) box.setValue("null");

        frontLabel.setVisible(false);
        rearLabel.setVisible(false);

        frontLabel.setTranslateX(0);
        frontLabel.setTranslateY(0);
        frontLabel.setLayoutX(nodeBoxes[0].getLayoutX());
        frontLabel.setLayoutY(nodeBoxes[0].getLayoutY() + BOX_H + 10);

        rearLabel.setTranslateX(0);
        rearLabel.setTranslateY(0);
        rearLabel.setLayoutX(nodeBoxes[0].getLayoutX());
        rearLabel.setLayoutY(nodeBoxes[0].getLayoutY() - 40);
    }

    private Point2D nodeCenter(int index) {
        double x = nodeBoxes[index].getLayoutX() + BOX_W / 2;
        double y = nodeBoxes[index].getLayoutY() + BOX_H / 2;
        return new Point2D(x, y);
    }

    private void log(String msg) {
        console.appendText(msg + "\nFront=" + (queue.size() == 0 ? 0 : queue.frontIndex())
                + "  Rear=" + (queue.size() == 0 ? 0 : queue.rearIndex()) + "\n\n");
    }

    public HBox getRoot() {
        return root;
    }
}