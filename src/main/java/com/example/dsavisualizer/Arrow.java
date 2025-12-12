package com.example.dsavisualizer;

import javafx.beans.binding.DoubleBinding;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;

public class Arrow {
    Line line;
    Polygon head;
    DoubleBinding sX, sY, eX, eY;
    Arrow(Line line, Polygon head, DoubleBinding sX, DoubleBinding sY, DoubleBinding eX, DoubleBinding eY) {
        this.line = line;
        this.head = head;
        this.sX = sX;
        this.sY = sY;
        this.eX = eX;
        this.eY = eY;
    }
    void unbind() {
        try {
            line.startXProperty().unbind();
            line.startYProperty().unbind();
            line.endXProperty().unbind();
            line.endYProperty().unbind();
            head.layoutXProperty().unbind();
            head.layoutYProperty().unbind();
        } catch (Exception ignored){}
    }
}