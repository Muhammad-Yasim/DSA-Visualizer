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

public class LinearQueueVisualization {

    private QueueLinear queue;
    private NodeBox[] nodeBoxes;
    private final int QUEUE_SIZE = 7;

    private Pane workArea;
    private HBox root;
    private Label rearLabel,frontLabel;
    private TextArea console;


    public LinearQueueVisualization(){
        createUI();
    }


    private void createUI(){
        root = new HBox(20);
        root.setAlignment(Pos.TOP_RIGHT);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: #3D4F40;");

        VBox screen = createScreen();
        VBox controlPanel = createControlPanel();

        //Make screen take all remaining space
        HBox.setHgrow(screen, javafx.scene.layout.Priority.ALWAYS);
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
        return root;
    }

    private HBox createHeader(){
        HBox header=new HBox();
        header.setPadding(new Insets(10,10,0,15));

        Label titleLabel=new Label("Linear Queue Visualization");
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
        workArea.setPrefHeight(626);
        workArea.setPrefWidth(1204);
        //workArea.setStyle("-fx-background-color:red;");

        // Initialize queue and boxes
        queue = new QueueLinear();
        nodeBoxes = new NodeBox[QUEUE_SIZE];

        //Place all boxes initially with null (empty value)
        double startX = 280;
        double startY = 290;
        double spacing = 90;

        for (int i = 0; i < QUEUE_SIZE; i++) {
            nodeBoxes[i] = new NodeBox("null"); // -1 = null value
            nodeBoxes[i].setLayoutX(startX + i * spacing);
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

        console.appendText(msg + "\nFront=" + f + "   |   Rear=" + r + "\n\n");
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
                log("Invalid value");
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


        if (queue.size() == 1) {
            frontLabel.setVisible(true);

            double frontX = 305;
            double frontY = 390;
            moveFrontPointer(frontX, frontY);
        }

        double newX = 305 + rear * 90;
        double newY = 250;
        moveRearPointer(newX, newY);

        log(value + " is enqueued");
    }

    private void moveRearPointer(double x, double y) {
        TranslateTransition tt = new TranslateTransition(Duration.seconds(0.5), rearLabel);

        double currentX = rearLabel.getLayoutX() + rearLabel.getTranslateX();
        double currentY = rearLabel.getLayoutY() + rearLabel.getTranslateY();

        tt.setByX(x - currentX);
        tt.setByY(y - currentY);

        tt.play();
    }



    private void dequeue() {

        if (queue.isEmpty()) {
            log("Queue is EMPTY! Cannot dequeue.");
            return;
        }

        int removed = queue.dequeue();  // returns value
        int front = queue.frontIndex(); // NEW front index

        int clearedIndex = front - 1; // old front index
        if (clearedIndex >= 0)
            nodeBoxes[clearedIndex].setValue("null");

        log(removed + " is dequeued");

        if (!queue.isEmpty()) {
            frontLabel.setVisible(true);

            double newX = 305 + front * 90;
            double newY = 390;
            moveFrontPointer(newX, newY);
        }
        else {
            frontLabel.setVisible(false);
            rearLabel.setVisible(false);

            for (NodeBox box : nodeBoxes)
                box.setValue("null");

            log("Queue is now EMPTY.");
        }
    }

    private void moveFrontPointer(double x, double y) {
        TranslateTransition tt = new TranslateTransition(Duration.seconds(0.5), frontLabel);

        double currentX = frontLabel.getLayoutX() + frontLabel.getTranslateX();
        double currentY = frontLabel.getLayoutY() + frontLabel.getTranslateY();

        tt.setByX(x - currentX);
        tt.setByY(y - currentY);

        tt.play();
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
