package com.example.dsavisualizer;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DoublyCircularListVisualization {

    private ListCircularDoubly list;
    private NodeBox[] nodeBoxes;
    private final int QUEUE_SIZE = 6;

    private Pane workArea;
    private HBox root;
    private TextArea console;
    private VBox controlPanel;

    private int animCount = 0;
    private double startX = 205;
    private double startY = 290;
    private double boxWidth = 90;
    private double boxHeight = 60;
    private double spacing = boxWidth + 50;

    private final List<Arrow> arrows = new ArrayList<>();
    private int lastIndex = -1;

    private Label lastLabel;
    private DoubleBinding lastXBinding = null;
    private DoubleBinding lastYBinding = null;

    public DoublyCircularListVisualization() {
        createUI();
    }

    private void createUI() {
        root = new HBox(20);
        root.setAlignment(Pos.TOP_RIGHT);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: #3D4F40;");

        VBox screen = createScreen();
        controlPanel = createControlPanel();

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

        Label titleLabel = new Label("Doubly Circular Linked List Visualization");
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
        button.setOnAction(e -> LandingPage.showLinkedListPage());
        return button;
    }

    private Pane createWorkArea() {
        workArea = new Pane();
        workArea.setPrefHeight(626);
        workArea.setPrefWidth(1204);

        list = new ListCircularDoubly();
        nodeBoxes = new NodeBox[QUEUE_SIZE];

        for (int i = 0; i < QUEUE_SIZE; i++) {
            nodeBoxes[i] = new NodeBox("null");
            nodeBoxes[i].setLayoutX(startX + i * spacing);
            nodeBoxes[i].setLayoutY(startY);
        }

        lastLabel = new Label("Last");
        lastLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #FF7043;");
        lastLabel.setVisible(false);
        workArea.getChildren().add(lastLabel);

        return workArea;
    }

    private TextArea createConsole() {
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
        console.appendText(msg + "\n\n");
    }

    private VBox createControlPanel() {
        VBox root = new VBox(20);
        root.setPrefWidth(300);
        root.setStyle("-fx-background-color: #2E3A33;-fx-background-radius: 8;");
        root.setPadding(new Insets(25));

        Label titleLabel = new Label("Controls");
        titleLabel.setStyle("-fx-font-size: 25px; -fx-font-weight: bold; -fx-text-fill: white;");

        Label valueLabel = new Label("Value (Maximum 6 nodes allowed)");
        valueLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: #95A575;");

        TextField valueEnq = new TextField();
        valueEnq.setPromptText("Value to insert / delete (e.g. 42)");
        valueEnq.setPrefHeight(45);
        valueEnq.setStyle("-fx-background-color: #3D4F40;-fx-text-fill: white;-fx-font-size: 16px;");

        Label targetLabel = new Label("Target value (for Insert After)");
        targetLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: #95A575;");

        TextField targetField = new TextField();
        targetField.setPromptText("After which value? (e.g. 10)");
        targetField.setPrefHeight(45);
        targetField.setStyle("-fx-background-color: #3D4F40;-fx-text-fill: white;-fx-font-size: 16px;");

        Button insertStartButton = controlPanelButton("Insert at Start");
        insertStartButton.setDefaultButton(true);
        insertStartButton.setOnAction(e -> {
            if (animCount > 0) return;
            String text = valueEnq.getText();
            if (text.isEmpty()) return;
            try {
                int value = Integer.parseInt(text);
                insertAtStart(value);
            } catch (NumberFormatException ex) {
                System.out.println("Invalid value");
            }
        });

        Button insertEndButton = controlPanelButton("Insert at End");
        insertEndButton.setOnAction(e -> {
            if (animCount > 0) return;
            String text = valueEnq.getText();
            if (text.isEmpty()) return;
            try {
                int value = Integer.parseInt(text);
                insertAtEnd(value);
            } catch (NumberFormatException ex) {
                System.out.println("Invalid value");
            }
        });

        // NEW: Insert After (matches ListCircularDoubly.insertAfter(data, item))
        Button insertAfterButton = controlPanelButton("Insert After (target)");
        insertAfterButton.setOnAction(e -> {
            if (animCount > 0) return;
            String valTxt = valueEnq.getText();
            String tgtTxt = targetField.getText();
            if (valTxt.isEmpty() || tgtTxt.isEmpty()) return;
            try {
                int value = Integer.parseInt(valTxt);
                int target = Integer.parseInt(tgtTxt);
                insertAfter(value, target);
            } catch (NumberFormatException ex) {
                System.out.println("Invalid value or target");
            }
        });

        Button deleteStartButton = controlPanelButton("Delete from Start");
        deleteStartButton.setOnAction(e -> {
            if (animCount > 0) return;
            deleteFromStart();
        });

        Button deleteEndButton = controlPanelButton("Delete from End");
        deleteEndButton.setOnAction(e -> {
            if (animCount > 0) return;
            deleteFromEnd();
        });

        // NEW: Delete by value (matches ListCircularDoubly.deleteByData(item))
        Button deleteByValueButton = controlPanelButton("Delete (by value)");
        deleteByValueButton.setOnAction(e -> {
            if (animCount > 0) return;
            String valTxt = valueEnq.getText();
            if (valTxt.isEmpty()) return;
            try {
                int item = Integer.parseInt(valTxt);
                deleteByValue(item);
            } catch (NumberFormatException ex) {
                System.out.println("Invalid value");
            }
        });

        root.getChildren().addAll(titleLabel, valueLabel, valueEnq,insertStartButton, insertEndButton,deleteStartButton, deleteEndButton, deleteByValueButton, targetLabel, targetField,insertAfterButton);

        return root;
    }

    private Button controlPanelButton(String string) {
        Button button = new Button(string);
        button.setPrefWidth(250);
        button.setStyle("-fx-background-color: #9FB873; -fx-border-color: transparent; -fx-text-fill: white; -fx-font-size: 15px; -fx-cursor: hand;");
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: white; -fx-border-color: transparent; -fx-text-fill: #9FB873; -fx-font-size: 15px; -fx-cursor: hand;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #9FB873; -fx-border-color: transparent; -fx-text-fill: white; -fx-font-size: 15px; -fx-cursor: hand;"));
        return button;
    }

    private void beginAnimation() {
        animCount++;
        if (controlPanel != null) controlPanel.setDisable(true);
    }

    private void endAnimation() {
        if (animCount > 0) animCount--;
        if (animCount == 0 && controlPanel != null) controlPanel.setDisable(false);
    }

    // ---------- LAST label logic ----------
    private void attachLastToIndex(int idx) {
        if (idx < 0 || idx >= QUEUE_SIZE) return;
        detachLastBinding();
        ensureNodePresent(idx);
        lastLabel.setVisible(true);

        lastXBinding = Bindings.createDoubleBinding(
                () -> nodeBoxes[idx].getLayoutX()
                        + nodeBoxes[idx].getTranslateX()
                        + boxWidth / 2.0
                        - lastLabel.getWidth() / 2.0,
                nodeBoxes[idx].layoutXProperty(),
                nodeBoxes[idx].translateXProperty(),
                lastLabel.widthProperty()
        );

        lastYBinding = Bindings.createDoubleBinding(
                () -> nodeBoxes[idx].getLayoutY()
                        + nodeBoxes[idx].getTranslateY()
                        + boxHeight + 8,
                nodeBoxes[idx].layoutYProperty(),
                nodeBoxes[idx].translateYProperty()
        );

        lastLabel.layoutXProperty().bind(lastXBinding);
        lastLabel.layoutYProperty().bind(lastYBinding);
    }

    private void detachLastBinding() {
        try { lastLabel.layoutXProperty().unbind(); } catch (Exception ignored) {}
        try { lastLabel.layoutYProperty().unbind(); } catch (Exception ignored) {}
        lastXBinding = null;
        lastYBinding = null;
    }

    private void animateLastMoveToIndex(int index, Duration duration, Runnable onFinished) {
        if (index < 0 || index >= QUEUE_SIZE) {
            if (onFinished != null) onFinished.run();
            return;
        }

        detachLastBinding();
        ensureNodePresent(index);

        double targetX = nodeBoxes[index].getLayoutX() + nodeBoxes[index].getTranslateX() + boxWidth / 2.0 - lastLabel.getWidth() / 2.0;
        double targetY = nodeBoxes[index].getLayoutY() + nodeBoxes[index].getTranslateY() + boxHeight + 8;

        double startXNow = lastLabel.getLayoutX();
        double startYNow = lastLabel.getLayoutY();
        double dx = targetX - startXNow;
        double dy = targetY - startYNow;

        if (Math.abs(dx) < 0.5 && Math.abs(dy) < 0.5) {
            lastLabel.setLayoutX(targetX);
            lastLabel.setLayoutY(targetY);
            lastLabel.setTranslateX(0);
            lastLabel.setTranslateY(0);
            if (onFinished != null) onFinished.run();
            return;
        }

        TranslateTransition tt = new TranslateTransition(duration, lastLabel);
        tt.setByX(dx);
        tt.setByY(dy);
        tt.setInterpolator(Interpolator.EASE_BOTH);
        tt.setOnFinished(ev -> {
            lastLabel.setLayoutX(targetX);
            lastLabel.setLayoutY(targetY);
            lastLabel.setTranslateX(0);
            lastLabel.setTranslateY(0);
            if (onFinished != null) onFinished.run();
        });
        tt.play();
    }

    // ---------- NodeBox visual helpers ----------
    private void ensureNodePresent(int i) {
        if (i < 0 || i >= QUEUE_SIZE) return;
        if (!workArea.getChildren().contains(nodeBoxes[i])) {
            nodeBoxes[i].setLayoutX(startX + i * spacing);
            nodeBoxes[i].setLayoutY(startY);
            nodeBoxes[i].setTranslateX(0);
            nodeBoxes[i].setTranslateY(0);
            workArea.getChildren().add(nodeBoxes[i]);
            nodeBoxes[i].toFront();
        } else {
            nodeBoxes[i].setTranslateX(0);
            nodeBoxes[i].setTranslateY(0);
        }
    }

    private void removeAllNodeVisualsIfEmpty() {
        if (list.isEmpty()) {
            for (int i = 0; i < QUEUE_SIZE; i++) {
                workArea.getChildren().remove(nodeBoxes[i]);
                nodeBoxes[i].setValue("null");
            }
            for (Arrow a : arrows) {
                a.unbind();
                workArea.getChildren().removeAll(a.line, a.head);
                Object extras = a.head.getUserData();
                if (extras instanceof List) workArea.getChildren().removeAll((List) extras);
            }
            arrows.clear();
            lastIndex = -1;
            detachLastBinding();
            lastLabel.setVisible(false);
        }
    }

    // ---------- Insert/Delete operations ----------
    private boolean isFull() {
        return list.size() >= QUEUE_SIZE;
    }

    private void insertAtStart(int value) {
        if (isFull()) {
            log("List is FULL! Cannot insert " + value);
            return;
        }

        if (list.size() == 0) {
            NodeBox temp = new NodeBox(value);
            double spawnX = startX + 0 * spacing;
            double spawnY = startY - 160;
            temp.setLayoutX(spawnX);
            temp.setLayoutY(spawnY);
            workArea.getChildren().add(temp);
            temp.toFront();

            TranslateTransition drop = new TranslateTransition(Duration.seconds(0.28), temp);
            drop.setByY(startY - spawnY);

            beginAnimation();
            drop.setOnFinished(e -> {
                list.insertAtBeginning(value);

                ensureNodePresent(0);
                nodeBoxes[0].setLayoutX(startX);
                nodeBoxes[0].setTranslateX(0);
                nodeBoxes[0].setTranslateY(0);
                nodeBoxes[0].setValue(value);

                workArea.getChildren().remove(temp);

                lastIndex = list.isEmpty() ? -1 : list.size() - 1;
                redrawArrows();

                lastLabel.setVisible(true);
                attachLastToIndex(lastIndex);

                log(value + " inserted at start");
                endAnimation();
            });

            drop.play();
            return;
        }

        // non-empty
        for (int i = 0; i < list.size(); i++) ensureNodePresent(i);

        // shift existing to right
        SequentialTransition shiftAll = new SequentialTransition();
        for (int i = list.size() - 1; i >= 0; i--) {
            NodeBox nb = nodeBoxes[i];
            TranslateTransition tt = new TranslateTransition(Duration.seconds(0.25), nb);
            tt.setByX(spacing);
            shiftAll.getChildren().add(tt);
        }

        NodeBox newBox = new NodeBox(value);
        double spawnX = startX + 0 * spacing;
        double spawnY = startY - 160;
        newBox.setLayoutX(spawnX);
        newBox.setLayoutY(spawnY);
        workArea.getChildren().add(newBox);
        newBox.toFront();

        TranslateTransition drop = new TranslateTransition(Duration.seconds(0.25), newBox);
        drop.setByY(startY - spawnY);

        SequentialTransition full = new SequentialTransition(shiftAll, new PauseTransition(Duration.seconds(0.05)), drop);
        beginAnimation();
        full.setOnFinished(e -> {
            list.insertAtBeginning(value);

            for (int i = 0; i < list.size(); i++) {
                ensureNodePresent(i);
                NodeBox nb = nodeBoxes[i];
                nb.setLayoutX(startX + i * spacing);
                nb.setTranslateX(0);
                nb.setTranslateY(0);
                nb.setValue(list.get(i));
            }

            for (int i = list.size(); i < QUEUE_SIZE; i++) {
                if (workArea.getChildren().contains(nodeBoxes[i])) {
                    nodeBoxes[i].setValue("null");
                    workArea.getChildren().remove(nodeBoxes[i]);
                }
            }

            workArea.getChildren().remove(newBox);

            lastIndex = list.size() - 1;
            redrawArrows();
            attachLastToIndex(lastIndex);

            log(value + " inserted at start");
            endAnimation();
        });

        full.play();
    }

    private void insertAtEnd(int value) {
        if (isFull()) {
            log("List is FULL! Cannot insert " + value);
            return;
        }

        for (int i = 0; i < list.size(); i++) ensureNodePresent(i);

        int finalIndex = list.size();
        NodeBox newBox = new NodeBox(value);
        double spawnX = startX + finalIndex * spacing + spacing / 2.0;
        double spawnY = startY - 160;
        newBox.setLayoutX(spawnX);
        newBox.setLayoutY(spawnY);
        workArea.getChildren().add(newBox);
        newBox.toFront();

        TranslateTransition drop = new TranslateTransition(Duration.seconds(0.3), newBox);
        drop.setByY(startY - spawnY);

        beginAnimation();
        drop.setOnFinished(e -> {
            list.insertAtEnd(value);
            ensureNodePresent(finalIndex);
            nodeBoxes[finalIndex].setTranslateX(0);
            nodeBoxes[finalIndex].setTranslateY(0);
            nodeBoxes[finalIndex].setValue(value);

            workArea.getChildren().remove(newBox);

            lastIndex = list.size() - 1;
            redrawArrows();
            attachLastToIndex(lastIndex);

            log(value + " inserted at end");
            endAnimation();
        });

        drop.play();
    }

    // ---------- NEW: Insert After (by value) ----------
    // Calls your ListCircularDoubly.insertAfter(data, item)
    private void insertAfter(int valueToInsert, int targetValue) {
        if (isFull()) {
            log("List is FULL! Cannot insert " + valueToInsert);
            return;
        }

        // find index of targetValue
        int targetIdx = findIndexByValue(targetValue);
        if (targetIdx == -1) {
            log("Target value " + targetValue + " not found.");
            return;
        }

        // ensure visuals for current nodes
        for (int i = 0; i < list.size(); i++) ensureNodePresent(i);

        // animate traversal to targetIdx
        animateHeadTraversal(targetIdx, () -> {
            // visually insert: shift nodes right from end down to targetIdx+1
            SequentialTransition shiftSeq = new SequentialTransition();
            for (int i = list.size() - 1; i > targetIdx; i--) {
                NodeBox nb = nodeBoxes[i];
                TranslateTransition tt = new TranslateTransition(Duration.seconds(0.25), nb);
                tt.setByX(spacing);
                shiftSeq.getChildren().add(tt);
            }

            beginAnimation();
            shiftSeq.setOnFinished(ev -> {
                // show the new node above the slot (targetIdx+1)
                double spawnX = startX + (targetIdx + 1) * spacing;
                double spawnY = startY - 160;
                NodeBox newBox = new NodeBox(valueToInsert);
                newBox.setLayoutX(spawnX);
                newBox.setLayoutY(spawnY);
                workArea.getChildren().add(newBox);
                newBox.toFront();

                TranslateTransition drop = new TranslateTransition(Duration.seconds(0.18), newBox);
                drop.setByY(startY - spawnY);

                drop.setOnFinished(dropEv -> {
                    // commit to model using your API
                    list.insertAfter(valueToInsert, targetValue);

                    // update visuals from model
                    for (int i = 0; i < list.size(); i++) {
                        ensureNodePresent(i);
                        NodeBox nb = nodeBoxes[i];
                        nb.setLayoutX(startX + i * spacing);
                        nb.setTranslateX(0);
                        nb.setTranslateY(0);
                        nb.setValue(list.get(i));
                    }

                    // remove temp node visual
                    workArea.getChildren().remove(newBox);

                    lastIndex = list.size() - 1;
                    redrawArrows();
                    attachLastToIndex(lastIndex);

                    log(valueToInsert + " inserted after " + targetValue);
                    endAnimation();
                });

                drop.play();
            });

            shiftSeq.play();
        });
    }

    // ---------- Delete operations ----------
    private void deleteFromStart() {
        if (list.isEmpty()) {
            log("List is empty.");
            return;
        }

        // special case: single node
        if (list.size() == 1) {
            int removed = list.deleteFromBeginning();

            ensureNodePresent(0);
            NodeBox target = nodeBoxes[0];

            NodeBox temp = new NodeBox(removed);
            temp.setLayoutX(target.getLayoutX());
            temp.setLayoutY(target.getLayoutY());
            workArea.getChildren().add(temp);
            temp.toFront();

            FadeTransition fade = new FadeTransition(Duration.seconds(0.25), temp);
            fade.setFromValue(1.0);
            fade.setToValue(0.0);
            TranslateTransition lift = new TranslateTransition(Duration.seconds(0.25), temp);
            lift.setByY(-80);
            ParallelTransition vanish = new ParallelTransition(fade, lift);

            beginAnimation();
            vanish.setOnFinished(e -> {
                workArea.getChildren().remove(temp);
                Platform.runLater(() -> {
                    removeAllNodeVisualsIfEmpty();
                    redrawArrows();
                    log(removed + " deleted from start");
                    endAnimation();
                });
            });

            vanish.play();
            return;
        }

        // For safe shifting, capture old size before modifying the model
        final int oldSize = list.size();

        for (int i = 0; i < oldSize; i++) ensureNodePresent(i);

        beginAnimation();
        // highlight head only (index 0) to avoid index confusion
        animateHeadTraversal(0, () -> {
            int removed = list.deleteFromBeginning();

            ensureNodePresent(0);
            NodeBox target = nodeBoxes[0];

            NodeBox temp = new NodeBox(removed);
            temp.setLayoutX(target.getLayoutX());
            temp.setLayoutY(target.getLayoutY());
            workArea.getChildren().add(temp);
            temp.toFront();

            FadeTransition fade = new FadeTransition(Duration.seconds(0.25), temp);
            fade.setFromValue(1.0);
            fade.setToValue(0.0);
            TranslateTransition lift = new TranslateTransition(Duration.seconds(0.25), temp);
            lift.setByY(-80);
            ParallelTransition vanish = new ParallelTransition(fade, lift);

            // shift nodes left visually from 1 .. oldSize-1
            SequentialTransition shiftSeq = new SequentialTransition();
            for (int i = 1; i < oldSize; i++) {
                ensureNodePresent(i);
                NodeBox nb = nodeBoxes[i];
                TranslateTransition tt = new TranslateTransition(Duration.seconds(0.25), nb);
                tt.setByX(-spacing);
                shiftSeq.getChildren().add(tt);
            }

            vanish.setOnFinished(ev -> workArea.getChildren().remove(temp));

            shiftSeq.setOnFinished(evt -> {
                for (int i = 0; i < list.size(); i++) {
                    ensureNodePresent(i);
                    NodeBox nb = nodeBoxes[i];
                    nb.setLayoutX(startX + i * spacing);
                    nb.setTranslateX(0);
                    nb.setTranslateY(0);
                    nb.setValue(list.get(i));
                }

                for (int i = list.size(); i < QUEUE_SIZE; i++) {
                    if (workArea.getChildren().contains(nodeBoxes[i])) {
                        nodeBoxes[i].setValue("null");
                        workArea.getChildren().remove(nodeBoxes[i]);
                    }
                }

                lastIndex = list.size() - 1;
                redrawArrows();
                attachLastToIndex(lastIndex);

                log(removed + " deleted from start");
                if (list.isEmpty()) removeAllNodeVisualsIfEmpty();
                endAnimation();
            });

            SequentialTransition full = new SequentialTransition(vanish, new PauseTransition(Duration.seconds(0.05)), shiftSeq);
            full.play();
        });
    }

    private void deleteFromEnd() {
        if (list.isEmpty()) {
            log("List is empty.");
            return;
        }

        int removed = list.deleteFromEnd();
        int idx = list.size(); // old last index
        ensureNodePresent(idx);
        NodeBox target = nodeBoxes[idx];

        NodeBox temp = new NodeBox(removed);
        temp.setLayoutX(target.getLayoutX());
        temp.setLayoutY(target.getLayoutY());
        workArea.getChildren().add(temp);
        temp.toFront();

        FadeTransition fade = new FadeTransition(Duration.seconds(0.25), temp);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);
        TranslateTransition lift = new TranslateTransition(Duration.seconds(0.25), temp);
        lift.setByY(-80);
        ParallelTransition vanish = new ParallelTransition(fade, lift);

        beginAnimation();
        vanish.setOnFinished(e -> {
            workArea.getChildren().remove(temp);

            if (workArea.getChildren().contains(nodeBoxes[idx])) {
                nodeBoxes[idx].setValue("null");
                workArea.getChildren().remove(nodeBoxes[idx]);
            }

            lastIndex = list.size() - 1;
            redrawArrows();
            attachLastToIndex(lastIndex);
            log(removed + " deleted from end");

            removeAllNodeVisualsIfEmpty();
            endAnimation();
        });

        vanish.play();
    }

    // ---------- NEW: delete by value (deleteByData) ----------
    private void deleteByValue(int item) {
        if (list.isEmpty()) {
            log("List is empty.");
            return;
        }

        // find index of the node whose data == item
        int idx = findIndexByValue(item);
        if (idx == -1) {
            log("Value " + item + " not found.");
            return;
        }

        // if it is start/first node, reuse deleteFromStart
        if (idx == 0) {
            deleteFromStart();
            return;
        }

        // otherwise traversal to idx then animate deletion
        for (int i = 0; i < list.size(); i++) ensureNodePresent(i);

        animateHeadTraversal(idx, () -> {
            int removed;
            try {
                removed = list.deleteByData(item);
            } catch (RuntimeException ex) {
                log("Delete failed: " + ex.getMessage());
                return;
            }

            // show vanish animation at idx slot
            ensureNodePresent(idx);
            NodeBox target = nodeBoxes[idx];

            NodeBox temp = new NodeBox(removed);
            temp.setLayoutX(target.getLayoutX());
            temp.setLayoutY(target.getLayoutY());
            workArea.getChildren().add(temp);
            temp.toFront();

            FadeTransition fade = new FadeTransition(Duration.seconds(0.25), temp);
            fade.setFromValue(1.0);
            fade.setToValue(0.0);
            TranslateTransition lift = new TranslateTransition(Duration.seconds(0.25), temp);
            lift.setByY(-80);
            ParallelTransition vanish = new ParallelTransition(fade, lift);

            beginAnimation();
            vanish.setOnFinished(ev -> workArea.getChildren().remove(temp));

            // shift nodes left visually from idx+1 .. end
            SequentialTransition shiftSeq = new SequentialTransition();
            for (int i = idx; i < list.size(); i++) {
                ensureNodePresent(i + 1);
                NodeBox nb = nodeBoxes[i + 1];
                TranslateTransition tt = new TranslateTransition(Duration.seconds(0.25), nb);
                tt.setByX(-spacing);
                shiftSeq.getChildren().add(tt);
            }

            shiftSeq.setOnFinished(evt -> {
                for (int i = idx; i < list.size(); i++) {
                    ensureNodePresent(i);
                    NodeBox nb = nodeBoxes[i];
                    nb.setLayoutX(startX + i * spacing);
                    nb.setTranslateX(0);
                    nb.setTranslateY(0);
                    nb.setValue(list.get(i));
                }

                for (int i = list.size(); i < QUEUE_SIZE; i++) {
                    if (workArea.getChildren().contains(nodeBoxes[i])) {
                        nodeBoxes[i].setValue("null");
                        workArea.getChildren().remove(nodeBoxes[i]);
                    }
                }

                lastIndex = list.size() - 1;
                redrawArrows();
                attachLastToIndex(lastIndex);

                log(removed + " deleted (value " + item + ")");
                endAnimation();
            });

            vanish.play();
            shiftSeq.play();
        });
    }

    // ---------- helpers ----------
    private int findIndexByValue(int val) {
        if (list.isEmpty()) return -1;
        int n = list.size();
        for (int i = 0; i < n; i++) {
            try {
                if (list.get(i) == val) return i;
            } catch (IndexOutOfBoundsException ignored) {}
        }
        return -1;
    }

    private void updateLastPosition() {
        if (list.isEmpty()) {
            detachLastBinding();
            lastLabel.setVisible(false);
            return;
        }
        ensureNodePresent(list.size() - 1);
        detachLastBinding();
        int idx = list.size() - 1;
        double targetX = nodeBoxes[idx].getLayoutX() + boxWidth / 2.0 - lastLabel.getWidth() / 2.0;
        double targetY = nodeBoxes[idx].getLayoutY() + boxHeight + 8;
        lastLabel.setLayoutX(targetX);
        lastLabel.setLayoutY(targetY);
        lastLabel.setTranslateX(0);
        lastLabel.setTranslateY(0);
        lastLabel.setVisible(true);
    }

    private void animateHeadTraversal(int targetIndex, Runnable onFinished) {
        if (list.isEmpty() || targetIndex < 0) {
            if (onFinished != null) onFinished.run();
            return;
        }

        beginAnimation();

        for (int i = 0; i <= targetIndex && i < list.size(); i++) ensureNodePresent(i);

        Timeline timeline = new Timeline();
        double stepMs = 300;
        for (int i = 0; i <= targetIndex; i++) {
            final int idx = i;
            KeyFrame kfOn = new KeyFrame(Duration.millis(i * stepMs), ev -> {
                Rectangle rect = (Rectangle) nodeBoxes[idx].getChildren().get(0);
                rect.setStroke(Color.web("#FFD54F"));
                rect.setStrokeWidth(3);
            });
            KeyFrame kfOff = new KeyFrame(Duration.millis(i * stepMs + stepMs / 2.0), ev -> {
                Rectangle rect = (Rectangle) nodeBoxes[idx].getChildren().get(0);
                rect.setStroke(Color.BLACK);
                rect.setStrokeWidth(1);
            });
            timeline.getKeyFrames().addAll(kfOn, kfOff);
        }

        KeyFrame end = new KeyFrame(Duration.millis((targetIndex + 1) * stepMs), ev -> {
            if (onFinished != null) onFinished.run();
            endAnimation();
        });
        timeline.getKeyFrames().add(end);
        timeline.play();
    }

    // ---------- Arrow redraw ----------
    private void redrawArrows() {
        // remove existing arrows (also remove any extra lines stored in head.userData)
        for (Arrow a : arrows) {
            a.unbind();
            workArea.getChildren().removeAll(a.line, a.head);
            Object extras = a.head.getUserData();
            if (extras instanceof List) workArea.getChildren().removeAll((List) extras);
        }
        arrows.clear();

        int n = list.size();
        if (n <= 0) {
            // nothing
            return;
        }

        // add arrows for adjacent nodes (0..n-2 -> i -> i+1)
        for (int i = 0; i < n - 1; i++) {
            Arrow fwd = drawArrow(nodeBoxes[i], nodeBoxes[i + 1], Color.WHITE, true);
            Arrow back = drawArrow(nodeBoxes[i + 1], nodeBoxes[i], Color.YELLOW, false);
            arrows.add(fwd);
            arrows.add(back);
        }

        // circular connection from last -> head (n-1 -> 0)
        if (n >= 1) {
            NodeBox last = nodeBoxes[n - 1];
            NodeBox head = nodeBoxes[0];

            Arrow circularFwd = drawCircularArrow(last, head, Color.WHITE);
            Arrow circularBack = drawCircularArrow(head, last, Color.YELLOW);
            arrows.add(circularFwd);
            arrows.add(circularBack);
        }

        // make nodes and last label on top, but keep arrow heads above nodes
        for (NodeBox nb : nodeBoxes)
            if (workArea.getChildren().contains(nb)) nb.toFront();
        if (lastLabel.isVisible()) lastLabel.toFront();

        for (Arrow a : arrows) {
            if (a.head != null) a.head.toFront();
        }
    }

    /**
     * Draw regular straight arrows between adjacent boxes.
     * isForward true => direction left->right (arrow points right)
     * isForward false => direction right->left (arrow points left)
     */
    private Arrow drawArrow(NodeBox from, NodeBox to, Color color, boolean isForward) {

        final double arrowSize = 10;
        final double offsetY = 16;
        final double arrowGap = 10;

        DoubleBinding startX = Bindings.createDoubleBinding(
                () -> from.getLayoutX() + from.getTranslateX()
                        + (isForward ? boxWidth : 0),
                from.layoutXProperty(), from.translateXProperty()
        );

        DoubleBinding startY = Bindings.createDoubleBinding(
                () -> from.getLayoutY() + from.getTranslateY()
                        + boxHeight / 2 + (isForward ? -offsetY : offsetY),
                from.layoutYProperty(), from.translateYProperty()
        );

        DoubleBinding endX = Bindings.createDoubleBinding(
                () -> to.getLayoutX() + to.getTranslateX()
                        + (isForward ? 0 : boxWidth),
                to.layoutXProperty(), to.translateXProperty()
        );

        DoubleBinding endY = Bindings.createDoubleBinding(
                () -> to.getLayoutY() + to.getTranslateY()
                        + boxHeight / 2 + (isForward ? -offsetY : offsetY),
                to.layoutYProperty(), to.translateYProperty()
        );

        Line line = new Line();
        line.startXProperty().bind(startX);
        line.startYProperty().bind(startY);
        line.endXProperty().bind(endX);
        line.endYProperty().bind(endY);
        line.setStroke(color);
        line.setStrokeWidth(2);

        Polygon head = new Polygon(
                0.0, 0.0,
                -arrowSize, -arrowSize / 2,
                -arrowSize, arrowSize / 2
        );
        head.setFill(color);

        DoubleBinding headX = Bindings.createDoubleBinding(
                () -> endX.get() + (isForward ? -arrowGap : arrowGap),
                endX
        );

        head.layoutXProperty().bind(headX);
        head.layoutYProperty().bind(endY);

        head.rotateProperty().bind(
                Bindings.createDoubleBinding(() -> (double) (isForward ? 0 : 180))
        );

        workArea.getChildren().addAll(line, head);
        return new Arrow(line, head, startX, startY, endX, endY);
    }


    /**
     * Draw a circular arrow (curved visual) from 'from' to 'to' above the row.
     * The arrow head will be rotated to match the direction. This implementation
     * creates a small vertical-up, horizontal-top, vertical-down polyline look
     * using three Line segments. The horizontal segment is returned as a.line so
     * the existing Arrow removal logic can remove the main shape; vertical
     * segments are attached to the head's userData and removed together.
//     */

    private Arrow drawCircularArrow(NodeBox from, NodeBox to, Color color) {

        final double arrowSize = 10;

        // different heights so white/yellow arcs don’t overlap
        final double liftHigh = 110;
        final double liftLow  = 70;

        // center Y of boxes
        DoubleBinding midYFrom = Bindings.createDoubleBinding(
                () -> from.getLayoutY() + from.getTranslateY() + boxHeight / 2.0,
                from.layoutYProperty(), from.translateYProperty());

        DoubleBinding midYTo = Bindings.createDoubleBinding(
                () -> to.getLayoutY() + to.getTranslateY() + boxHeight / 2.0,
                to.layoutYProperty(), to.translateYProperty());

        // X positions (slightly inside boxes)
        DoubleBinding sx = Bindings.createDoubleBinding(
                () -> from.getLayoutX() + from.getTranslateX() + boxWidth - 6,
                from.layoutXProperty(), from.translateXProperty());

        DoubleBinding ex = Bindings.createDoubleBinding(
                () -> to.getLayoutX() + to.getTranslateX() + 6,
                to.layoutXProperty(), to.translateXProperty());

        // direction (forward / backward)
        BooleanBinding forward = Bindings.createBooleanBinding(
                () -> (from.getLayoutX() + from.getTranslateX())
                        < (to.getLayoutX() + to.getTranslateX()),
                from.layoutXProperty(), from.translateXProperty(),
                to.layoutXProperty(), to.translateXProperty());

        // choose arc height
        DoubleBinding lift = Bindings.createDoubleBinding(
                () -> forward.get() ? liftHigh : liftLow,
                forward);

        DoubleBinding syUp = Bindings.createDoubleBinding(
                () -> midYFrom.get() - lift.get(), midYFrom, lift);

        DoubleBinding eyUp = Bindings.createDoubleBinding(
                () -> midYTo.get() - lift.get(), midYTo, lift);

        // ───────── vertical from FROM node
        Line vLeft = new Line();
        vLeft.startXProperty().bind(sx);
        vLeft.endXProperty().bind(sx);
        vLeft.startYProperty().bind(midYFrom);
        vLeft.endYProperty().bind(syUp);
        vLeft.setStroke(color);
        vLeft.setStrokeWidth(2);

        // ───────── top horizontal
        Line hTop = new Line();
        hTop.startXProperty().bind(sx);
        hTop.startYProperty().bind(syUp);
        hTop.endXProperty().bind(ex);
        hTop.endYProperty().bind(eyUp);
        hTop.setStroke(color);
        hTop.setStrokeWidth(2);

        // ───────── vertical into TO node
        Line vRight = new Line();
        vRight.startXProperty().bind(ex);
        vRight.endXProperty().bind(ex);
        vRight.startYProperty().bind(eyUp);

        vRight.endYProperty().bind(
                midYTo.subtract(boxHeight / 2 + 12)
        );


        vRight.setStroke(color);
        vRight.setStrokeWidth(2);

        // ───────── ARROW HEAD (perfect 90°)
        Polygon head = new Polygon(
                0, 0,
                -arrowSize, -arrowSize / 2,
                -arrowSize,  arrowSize / 2
        );
        head.setFill(color);
        final double headXOffset = 5; // tweak: 4–10 looks good

        head.layoutXProperty().bind(
                ex.add(headXOffset)
        );

        head.layoutYProperty().bind(
                midYTo.subtract(boxHeight / 2 + 12)
        );

        // force exact vertical rotation
        head.rotateProperty().bind(
                Bindings.when(forward).then(90.0).otherwise(90.0)
        );

        workArea.getChildren().addAll(vLeft, hTop, vRight, head);

        // store extra segments for cleanup
        head.setUserData(Arrays.asList(vLeft, vRight));

        // return horizontal segment as main Arrow line
        return new Arrow(hTop, head, sx, syUp, ex, eyUp);
    }

    public HBox getRoot() {
        return root;
    }

}
