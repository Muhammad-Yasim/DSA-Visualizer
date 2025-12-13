package com.example.dsavisualizer;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class NodeBox extends Pane{

    private Rectangle rect;
    private Label label;
    private boolean val;

    public NodeBox(int value){
        double width=90;
        double height=60;
        val=true;

        rect=new Rectangle(width,height);
        rect.setFill(Color.WHITE);
        rect.setStroke(Color.BLACK);
        label=new Label(String.valueOf(value));
        label.setPrefSize(width,height);
        label.setAlignment(Pos.CENTER);
        label.setStyle("-fx-font-size:24;-fx-font-weight:bold;");

        this.getChildren().addAll(rect,label);
        this.setPrefSize(width,height);
    }

    public NodeBox(String value){

        double width=90;
        double height=60;
        val=false;

        rect=new Rectangle(width,height);
        rect.setFill(Color.WHITE);
        rect.setStroke(Color.BLACK);
        label=new Label(value);
        label.setPrefSize(width,height);
        label.setAlignment(Pos.CENTER);
        label.setStyle("-fx-font-size:18;-fx-font-weight:bold;");

        this.getChildren().addAll(rect,label);
        this.setPrefSize(width,height);
    }

    public void setValue(String str){
        this.label.setText(str);
    }

    public void setValue(int val){
        this.label.setText(String.valueOf(val));
    }
}
