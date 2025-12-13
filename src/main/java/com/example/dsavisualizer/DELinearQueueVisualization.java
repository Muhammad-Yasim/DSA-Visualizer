package com.example.dsavisualizer;

import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Duration;

public class DELinearQueueVisualization {

    private QueueDoublyLinear queue;
    private NodeBox[] nodeBoxes;
    private final int QUEUE_SIZE = 7;

    private double startX = 280;
    private double startY = 290;
    private double boxWidth = 90;

    private Pane workArea;
    private HBox root;
    private Label rearLabel, frontLabel;
    private TextArea console;

    public DELinearQueueVisualization() {
        queue = new QueueDoublyLinear(QUEUE_SIZE);
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

    private HBox createHeader() {
        HBox header = new HBox();
        header.setPadding(new Insets(10, 10, 0, 15));

        Label titleLabel = new Label("Double Ended Linear Queue Visualization");
        titleLabel.setStyle("-fx-font-size: 25px; -fx-font-weight: bold; -fx-text-fill: white;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button backButton = backButton();

        header.getChildren().addAll(titleLabel, spacer, backButton);
        return header;
    }

    private Button backButton() {
        Button button = new Button("<- Back to menu");
        button.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-text-fill: #95A575; -fx-font-size: 17px; -fx-cursor: hand;");
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-text-fill: white; -fx-font-size: 17px; -fx-cursor: hand;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-text-fill: #95A575; -fx-font-size: 17px; -fx-cursor: hand;"));
        button.setOnAction(e -> LandingPage.showQueuePage());
        return button;
    }

    private Pane createWorkArea() {
        workArea = new Pane();
        workArea.setPrefHeight(626);
        workArea.setPrefWidth(1204);

        nodeBoxes = new NodeBox[QUEUE_SIZE];
        for (int i = 0; i < QUEUE_SIZE; i++) {
            nodeBoxes[i] = new NodeBox("null");
            nodeBoxes[i].setLayoutX(startX + i * boxWidth);
            nodeBoxes[i].setLayoutY(startY);
            workArea.getChildren().add(nodeBoxes[i]);
        }

        rearLabel = new Label("rear");
        rearLabel.setStyle("-fx-font-size: 22px; -fx-text-fill: white;");
        rearLabel.setLayoutX(startX);
        rearLabel.setLayoutY(startY - 40);
        rearLabel.setVisible(false);
        workArea.getChildren().add(rearLabel);

        frontLabel = new Label("front");
        frontLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: white;");
        frontLabel.setLayoutX(startX);
        frontLabel.setLayoutY(startY + 100);
        frontLabel.setVisible(false);
        workArea.getChildren().add(frontLabel);

        return workArea;
    }

    private TextArea createConsole() {
        console = new TextArea();
        console.setEditable(false);
        console.setWrapText(true);
        console.setStyle("-fx-control-inner-background: black;-fx-text-fill: #7CFC00;-fx-font-family: Consolas;-fx-font-size: 14px;-fx-background-color: black;-fx-background-insets: 0;-fx-background-radius: 0;-fx-border-color: transparent;-fx-border-width: 0;");
        console.setPrefHeight(125);
        return console;
    }

    private void log(String msg) {
        console.appendText(msg + "\nFront=" + queue.frontIndex() + "   |   Rear=" + queue.rearIndex() + "\n\n");
    }

    private VBox createControlPanel() {
        VBox root = new VBox(20);
        root.setPrefWidth(300);
        root.setStyle("-fx-background-color: #2E3A33;-fx-background-radius: 8;");
        root.setPadding(new Insets(25));

        Label titleLabel = new Label("Controls");
        titleLabel.setStyle("-fx-font-size: 25px; -fx-font-weight: bold; -fx-text-fill: white;");

        Label valueLabel = new Label("Value");
        valueLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: #95A575;");

        TextField valueEnq = new TextField();
        valueEnq.setPromptText("e.g., 42");
        valueEnq.setPrefHeight(45);
        valueEnq.setStyle("-fx-background-color: #3D4F40;-fx-text-fill: white;-fx-font-size: 16px;");

        Button enqFrontButton = controlPanelButton("+ Enqueue Front");
        enqFrontButton.setOnAction(e -> {
            if (!valueEnq.getText().isEmpty()) {
                try{
                enqueueFront(Integer.parseInt(valueEnq.getText()));
                }catch(NumberFormatException ex) {
                    log("Invalid value");
                }
            }
        });


        Button enqBackButton = controlPanelButton("+ Enqueue Back");
        enqBackButton.setOnAction(e -> {
            if (!valueEnq.getText().isEmpty()){
                try {
                    enqueueBack(Integer.parseInt(valueEnq.getText()));
                }catch (NumberFormatException ex) {
                    log("Invalid value");
                }
            }
        });

        Button deqFrontButton = controlPanelButton("- Dequeue Front");
        deqFrontButton.setOnAction(e -> dequeueFront());

        Button deqBackButton = controlPanelButton("- Dequeue Back");
        deqBackButton.setOnAction(e -> dequeueBack());

        root.getChildren().addAll(titleLabel, valueLabel, valueEnq, enqFrontButton,enqBackButton,deqFrontButton,deqBackButton);
        return root;
    }

    private void enqueueFront(int value) {
        if(queue.isFull()){
            log("Queue is full!! Cant enqueue "+value+" at Front.");
            return;
        }
        queue.enqueueFront(value);
        refreshBoxes();
        log(value + " enqueued at FRONT");
        updatePointers();
    }

    private void enqueueBack(int value) {
        if(queue.isFull()){
            log("Queue is full!! Cant enqueue "+value+" at Back.");
            return;
        }
        queue.enqueueBack(value);
        refreshBoxes();
        log(value + " enqueued at BACK");
        updatePointers();
    }

    private void dequeueFront() {
        int val = queue.dequeueFront();
        if (val != -1) {
            refreshBoxes();
            log(val + " dequeued from FRONT");
            updatePointers();
        } else log("Queue is EMPTY! Cannot dequeueFront");
    }

    private void dequeueBack() {
        int val = queue.dequeueBack();
        if (val != -1) {
            refreshBoxes();
            log(val + " dequeued from BACK");
            updatePointers();
        } else log("Queue is EMPTY! Cannot dequeueBack");
    }

    private void refreshBoxes() {
        for (int i = 0; i < QUEUE_SIZE; i++) {
            if (i >= queue.frontIndex() && i <= queue.rearIndex()) {
                nodeBoxes[i].setValue(queue.getValueAt(i));
            } else {
                nodeBoxes[i].setValue("null");
            }
            nodeBoxes[i].setTranslateX(0);
            nodeBoxes[i].setTranslateY(0);
        }
    }

    private void updatePointers() {
        int f = queue.frontIndex();
        int r = queue.rearIndex();

        frontLabel.setVisible(f != -1);
        rearLabel.setVisible(r != -1);

        if (f != -1) moveFrontPointer(nodeBoxes[f].getLayoutX(), nodeBoxes[f].getLayoutY() + 100);
        if (r != -1) moveRearPointer(nodeBoxes[r].getLayoutX(), nodeBoxes[r].getLayoutY() - 40);
    }

    private void moveRearPointer(double x, double y) {
        TranslateTransition tt = new TranslateTransition(Duration.seconds(0.35), rearLabel);
        tt.setToX(x - rearLabel.getLayoutX());
        tt.setToY(y - rearLabel.getLayoutY());
        tt.play();
    }

    private void moveFrontPointer(double x, double y) {
        TranslateTransition tt = new TranslateTransition(Duration.seconds(0.35), frontLabel);
        tt.setToX(x - frontLabel.getLayoutX());
        tt.setToY(y - frontLabel.getLayoutY());
        tt.play();
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

    public HBox getRoot() {
        return root;
    }
}
