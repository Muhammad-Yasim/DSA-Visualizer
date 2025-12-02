package com.example.dsavisualizer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.ValueAxis;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class LinearQueueVisualization {
    private HBox root;

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
        HBox workArea=createWorkArea();
        TextArea consoleArea=createConsole();

        //Make screen take all remaining space
        VBox.setVgrow(workArea, javafx.scene.layout.Priority.ALWAYS);
        workArea.setFillHeight(true);

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

    private HBox createWorkArea(){
        HBox workArea=new HBox();
        workArea.setStyle("-fx-background-color:red;");

        return workArea;
    }

    private TextArea createConsole(){
        TextArea logArea = new TextArea();
        logArea.setEditable(false); //User cannot type
        logArea.setWrapText(true);
        logArea.setStyle(
                "-fx-control-inner-background: black;-fx-text-fill: #7CFC00;-fx-font-family: Consolas;-fx-font-size: 14px;-fx-background-color: black;-fx-background-insets: 0;-fx-background-radius: 0;-fx-border-color: transparent;-fx-border-width: 0;"
        );
        logArea.setPrefHeight(125);
        return logArea;
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
        Button dequeueButton=controlPanelButton("- Dequeue");

        root.getChildren().addAll(titleLabel,valueLabel,valueEnq,enqueueButton,dequeueButton);
        return root;
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
        //button.setOnAction(e-> App.showLandingPage());

        return button;
    }

    public HBox getRoot(){return root;}

}
