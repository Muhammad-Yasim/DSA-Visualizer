package com.example.dsavisualizer;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.Bindings;
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
import java.util.List;

public class LinearListVisualization {

    private ListLinear list;
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
    private int headIndex = -1;
    private Label headLabel;
    private DoubleBinding headXBinding = null;
    private DoubleBinding headYBinding = null;

    public LinearListVisualization(){
        createUI();
    }

    private void createUI(){
        root = new HBox(20);
        root.setAlignment(Pos.TOP_RIGHT);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: #3D4F40;");

        VBox screen = createScreen();
        controlPanel = createControlPanel();

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

        Label titleLabel=new Label("Linear Linked List Visualization");
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
        button.setOnAction(e-> LandingPage.showLinkedListPage() );

        return button;
    }

    private Pane createWorkArea() {
        workArea = new Pane();
        workArea.setPrefHeight(626);
        workArea.setPrefWidth(1204);

        list = new ListLinear();
        nodeBoxes = new NodeBox[QUEUE_SIZE];

        //create nodeBoxes
        for (int i = 0; i < QUEUE_SIZE; i++) {
            nodeBoxes[i] = new NodeBox("null");
            nodeBoxes[i].setLayoutX(startX + i * spacing);
            nodeBoxes[i].setLayoutY(startY);
        }

        headLabel = new Label("Head");
        headLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #FFD54F;");
        headLabel.setVisible(false); // initially hidden
        workArea.getChildren().add(headLabel);

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
        console.appendText(msg + "\n\n");
    }

    private VBox createControlPanel(){
        VBox root=new VBox(20);
        root.setPrefWidth(300);
        root.setStyle("-fx-background-color: #2E3A33;-fx-background-radius: 8;");
        root.setPadding(new Insets(25));

        Label titleLabel=new Label("Controls");
        titleLabel.setStyle("-fx-font-size: 25px; -fx-font-weight: bold; -fx-text-fill: white;");

        Label valueLabel=new Label("Value (Maximum 6 nodes allowed)");
        valueLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: #95A575;");

        TextField valueEnq=new TextField();
        valueEnq.setPromptText("e.g., 42");
        valueEnq.setPrefHeight(45);
        valueEnq.setStyle("-fx-background-color: #3D4F40;-fx-text-fill: white;-fx-font-size: 16px;");

        Button insertStartButton=controlPanelButton("Insert at Start");
        insertStartButton.setDefaultButton(true);
        insertStartButton.setOnAction(e -> {
            if (animCount > 0) return;
            String text = valueEnq.getText();
            if (text.isEmpty()) return;
            try {
                int value = Integer.parseInt(text);
                insertAtStart(value);
            } catch (NumberFormatException ex) {
                log("Invalid value");
            }
        });

        Button insertEndButton=controlPanelButton("Insert at End");
        insertEndButton.setOnAction(e -> {
            if (animCount > 0) return;
            String text = valueEnq.getText();
            if (text.isEmpty()) return;
            try {
                int value = Integer.parseInt(text);
                insertAtEnd(value);
            } catch (NumberFormatException ex) {
                log("Invalid value");
            }
        });

        Button deleteStartButton=controlPanelButton("Delete from Start");
        deleteStartButton.setOnAction(e -> {
            if (animCount > 0) return;
            deleteFromStart();
        });

        Button deleteEndButton=controlPanelButton("Delete from End");
        deleteEndButton.setOnAction(e -> {
            if (animCount > 0) return;
            deleteFromEnd();
        });

        Label indexLabel=new Label("Index");
        indexLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: #95A575;");

        TextField indexField=new TextField();
        indexField.setPromptText("Must be less than 6");
        indexField.setPrefHeight(45);
        indexField.setStyle("-fx-background-color: #3D4F40;-fx-text-fill: white;-fx-font-size: 16px;");

        Button insertMidButton=controlPanelButton("Insert at Index");
        insertMidButton.setOnAction(e -> {
            if (animCount > 0) return;
            String text = valueEnq.getText();
            String idx = indexField.getText();
            if (text.isEmpty() || idx.isEmpty()) return;
            try {
                int value = Integer.parseInt(text);
                int index = Integer.parseInt(idx);
                insertAtIndex(value, index);
            } catch (NumberFormatException ex) {
                log("Invalid value or index");
            }
        });

        Button deleteMidButton=controlPanelButton("Delete at Index");
        deleteMidButton.setOnAction(e -> {
            if (animCount > 0) return;
            String idx = indexField.getText();
            if (idx.isEmpty()) return;
            try {
                int index = Integer.parseInt(idx);
                deleteAtIndex(index);
            } catch (NumberFormatException ex) {
                log("Invalid index");
            }
        });

        root.getChildren().addAll(titleLabel,valueLabel,valueEnq,insertStartButton,insertEndButton,deleteStartButton,deleteEndButton,indexLabel,indexField,insertMidButton,deleteMidButton);

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

    private void beginAnimation() {
        animCount++;
        if (controlPanel != null) controlPanel.setDisable(true);
    }

    private void endAnimation() {
        if (animCount > 0) animCount--;
        if (animCount == 0 && controlPanel != null) controlPanel.setDisable(false);
    }

    private void attachHeadToIndex(int idx) {
        if (idx < 0 || idx >= QUEUE_SIZE) return;
        //remove any previous binding
        detachHeadBinding();

        //ensure node present so binding works
        ensureNodePresent(idx);

        headLabel.setVisible(true);

        headXBinding = Bindings.createDoubleBinding(
                () -> {
                    double nbx = nodeBoxes[idx].getLayoutX() + nodeBoxes[idx].getTranslateX();
                    double hw = headLabel.getWidth();
                    return nbx + boxWidth / 2.0 - hw / 2.0;
                },
                nodeBoxes[idx].layoutXProperty(), nodeBoxes[idx].translateXProperty(), headLabel.widthProperty()
        );

        headYBinding = Bindings.createDoubleBinding(
                () -> {
                    double nby = nodeBoxes[idx].getLayoutY() + nodeBoxes[idx].getTranslateY();
                    return nby - 30.0;
                },
                nodeBoxes[idx].layoutYProperty(), nodeBoxes[idx].translateYProperty()
        );

        headLabel.layoutXProperty().bind(headXBinding);
        headLabel.layoutYProperty().bind(headYBinding);
    }

    private void detachHeadBinding() {
        try {
            if (headXBinding != null) {
                headLabel.layoutXProperty().unbind();
                headXBinding = null;
            }
        } catch (Exception ignored){}
        try {
            if (headYBinding != null) {
                headLabel.layoutYProperty().unbind();
                headYBinding = null;
            }
        } catch (Exception ignored){}
    }

    private void animateHeadMoveToIndex(int index, Duration duration, Runnable onFinished) {
        if (index < 0 || index >= QUEUE_SIZE) {
            if (onFinished != null) onFinished.run();
            return;
        }

        //ensure we have a concrete start position (if bound, unbind -> preserves current computed layout)
        detachHeadBinding();

        //ensure destination node exists visually so we can compute target coords
        ensureNodePresent(index);

        //compute target coords (use current layoutX/layoutY + translateX since nodes may be moving)
        double targetX = nodeBoxes[index].getLayoutX() + nodeBoxes[index].getTranslateX() + boxWidth / 2.0 - headLabel.getWidth() / 2.0;
        double targetY = nodeBoxes[index].getLayoutY() + nodeBoxes[index].getTranslateY() - 30.0;

        //compute deltas from current screen layout position
        double startXNow = headLabel.getLayoutX();
        double startYNow = headLabel.getLayoutY();
        double dx = targetX - startXNow;
        double dy = targetY - startYNow;

        //if dx/dy are tiny, directly place
        if (Math.abs(dx) < 0.5 && Math.abs(dy) < 0.5) {
            headLabel.setLayoutX(targetX);
            headLabel.setLayoutY(targetY);
            headLabel.setTranslateX(0);
            headLabel.setTranslateY(0);
            if (onFinished != null) onFinished.run();
            return;
        }

        //animate using TranslateTransition (we'll finalize by setting layout to target and clearing translate)
        TranslateTransition tt = new TranslateTransition(duration, headLabel);
        tt.setByX(dx);
        tt.setByY(dy);
        tt.setInterpolator(Interpolator.EASE_BOTH);
        tt.setOnFinished(ev -> {
            headLabel.setLayoutX(targetX);
            headLabel.setLayoutY(targetY);
            headLabel.setTranslateX(0);
            headLabel.setTranslateY(0);
            if (onFinished != null) onFinished.run();
        });
        tt.play();
    }

    private boolean isFull() {
        return list.size() >= QUEUE_SIZE;
    }

    private void ensureNodePresent(int i) {
        if (i < 0 || i >= QUEUE_SIZE) return;
        if (!workArea.getChildren().contains(nodeBoxes[i])) {
            nodeBoxes[i].setLayoutX(startX + i * spacing);
            nodeBoxes[i].setLayoutY(startY);
            nodeBoxes[i].setTranslateX(0); // important: reset transforms
            nodeBoxes[i].setTranslateY(0);
            workArea.getChildren().add(nodeBoxes[i]);
            nodeBoxes[i].toFront();
        } else {
            //if already present,ensure transforms are cleared before further animations
            nodeBoxes[i].setTranslateX(0);
            nodeBoxes[i].setTranslateY(0);
        }
    }

    //remove all node visuals when list becomes empty
    private void removeAllNodeVisualsIfEmpty() {
        if (list.isEmpty()) {
            for (int i = 0; i < QUEUE_SIZE; i++) {
                workArea.getChildren().remove(nodeBoxes[i]);
                nodeBoxes[i].setValue("null");
                nodeBoxes[i].setTranslateX(0);
                nodeBoxes[i].setTranslateY(0);
            }
            for (Arrow a : arrows) {
                a.unbind();
                workArea.getChildren().removeAll(a.line, a.head);
            }
            arrows.clear();
            headIndex = -1;

            detachHeadBinding();
            headLabel.setVisible(false);
        }
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

                headIndex = list.isEmpty() ? -1 : 0;
                redrawArrows();
                headLabel.setVisible(true);
                headLabel.setLayoutX(nodeBoxes[0].getLayoutX() + boxWidth/2.0 - headLabel.getWidth()/2.0);
                headLabel.setLayoutY(nodeBoxes[0].getLayoutY() - 30);
                log(value + " inserted at start");

                endAnimation();
            });

            drop.play();
            return;
        }

        for (int i = 0; i < list.size(); i++) ensureNodePresent(i);

        //Attach head to the current first node so it moves "with" it while shifting
        attachHeadToIndex(0);

        //Shift visuals to the right for existing nodes (sequential as before)
        SequentialTransition shiftAll = new SequentialTransition();
        for (int i = list.size() - 1; i >= 0; i--) {
            NodeBox nb = nodeBoxes[i];
            nb.setTranslateX(0);
            nb.setTranslateY(0);
            TranslateTransition tt = new TranslateTransition(Duration.seconds(0.25), nb);
            tt.setByX(spacing);
            shiftAll.getChildren().add(tt);
        }

        //Create the new node box above the first slot (temporary)
        NodeBox newBox = new NodeBox(value);
        double spawnX = startX + 0 * spacing;
        double spawnY = startY - 160;
        newBox.setLayoutX(spawnX);
        newBox.setLayoutY(spawnY);
        workArea.getChildren().add(newBox);
        newBox.toFront();

        //Drop new node into slot 0
        TranslateTransition drop = new TranslateTransition(Duration.seconds(0.25), newBox);
        drop.setByY(startY - spawnY);

        SequentialTransition full = new SequentialTransition(
                shiftAll,
                new PauseTransition(Duration.seconds(0.05)),
                drop
        );

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
            animateHeadMoveToIndex(0, Duration.seconds(0.18), () -> {
                headIndex = 0;
                redrawArrows();
                log(value + " inserted at start");
                endAnimation();
            });
        });

        full.play();
    }

    private void insertAtEnd(int value) {
        if (isFull()) {
            log("List is FULL! Cannot insert " + value);
            return;
        }

        for (int i = 0; i < list.size(); i++) ensureNodePresent(i);

        int finalIndex = list.size(); //index where it will land
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

            headIndex = list.isEmpty() ? -1 : 0;
            redrawArrows();
            updateHeadPosition();
            log(value + " inserted at end");

            endAnimation();
        });

        drop.play();
    }

    private void insertAtIndex(int value, int index) {
        if (isFull()) {
            log("List is FULL! Cannot insert " + value);
            return;
        }
        if (index < 0 || index > list.size()) {
            log("Cant Insert. Index out of bounds");
            return;
        }

        if (index == 0) {
            insertAtStart(value);
            return;
        }
        if (index == list.size()) {
            insertAtEnd(value);
            return;
        }

        for (int i = 0; i < list.size(); i++) ensureNodePresent(i);

        //traverse visually then insert
        animateHeadTraversal(index, () -> {
            double spawnX = startX + index * spacing + spacing / 2.0;
            double spawnY = startY - 160;
            NodeBox newBox = new NodeBox(value);
            newBox.setLayoutX(spawnX);
            newBox.setLayoutY(spawnY);
            workArea.getChildren().add(newBox);
            newBox.toFront();

            SequentialTransition shiftSeq = new SequentialTransition();
            for (int i = list.size() - 1; i >= index; i--) {
                NodeBox nb = nodeBoxes[i];
                nb.setTranslateX(0);
                nb.setTranslateY(0);
                TranslateTransition tt = new TranslateTransition(Duration.seconds(0.25), nb);
                tt.setByX(spacing);
                shiftSeq.getChildren().add(tt);
            }

            beginAnimation();
            shiftSeq.setOnFinished(ev -> {
                list.insertAtLocation(index, value);

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

                headIndex = list.isEmpty() ? -1 : 0;
                redrawArrows();
                log(value + " inserted at index " + index);

                endAnimation();
            });

            TranslateTransition drop = new TranslateTransition(Duration.seconds(0.18), newBox);
            drop.setByY(startY - spawnY);
            SequentialTransition seq = new SequentialTransition(shiftSeq, new PauseTransition(Duration.seconds(0.05)), drop);
            seq.play();
        });
    }

    private void deleteFromStart() {
        if (list.isEmpty()) {
            log("List is empty.");
            return;
        }

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
        for (int i = 0; i < list.size(); i++) ensureNodePresent(i);

        beginAnimation(); //disable controls during head move + subsequent delete animation

        //animate head to node index 1
        animateHeadMoveToIndex(1, Duration.seconds(0.18), () -> {
            //now proceed to delete the first node (we remove from model and play vanish+shift)
            int removed = list.deleteFromBeginning();

            //ensure visuals; target is current nodeBoxes[0]
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

            //shift remaining nodes left (index 1..size -> 0..size-1)
            SequentialTransition shiftSeq = new SequentialTransition();
            for (int i = 1; i <= list.size(); i++) {
                ensureNodePresent(i);
                NodeBox nb = nodeBoxes[i];
                nb.setTranslateX(0);
                nb.setTranslateY(0);
                TranslateTransition tt = new TranslateTransition(Duration.seconds(0.25), nb);
                tt.setByX(-spacing);
                shiftSeq.getChildren().add(tt);
            }

            vanish.setOnFinished(ev -> workArea.getChildren().remove(temp));

            shiftSeq.setOnFinished(ev -> {
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

                headIndex = list.isEmpty() ? -1 : 0;
                animateHeadMoveToIndex(0, Duration.seconds(0.12), () -> {
                    redrawArrows();
                    log(removed + " deleted from start");
                    if (list.isEmpty()) removeAllNodeVisualsIfEmpty();
                    endAnimation();
                });
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

            headIndex = list.isEmpty() ? -1 : 0;
            redrawArrows();
            updateHeadPosition();
            log(removed + " deleted from end");

            removeAllNodeVisualsIfEmpty();
            endAnimation();
        });

        vanish.play();
    }

    private void deleteAtIndex(int index) {
        if (list.isEmpty()) {
            log("List is empty.");
            return;
        }
        if (index < 0 || index >= list.size()) {
            log("Cant Delete. Index out of bounds");
            return;
        }
        if(index==0){
            deleteFromStart();
            return;
        }


        for (int i = 0; i < list.size(); i++)
            ensureNodePresent(i);

        // traversal is now treated as an animation (prevents interleaving)
        animateHeadTraversal(index, () -> {
            int removed = list.deleteAtLocation(index);
            ensureNodePresent(index);
            NodeBox target = nodeBoxes[index];

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
            vanish.setOnFinished(ev -> {
                workArea.getChildren().remove(temp);

                SequentialTransition shiftSeq = new SequentialTransition();
                for (int i = index; i < list.size(); i++) {
                    ensureNodePresent(i + 1);
                    NodeBox nb = nodeBoxes[i + 1];
                    nb.setTranslateX(0);
                    nb.setTranslateY(0);
                    TranslateTransition tt = new TranslateTransition(Duration.seconds(0.25), nb);
                    tt.setByX(-spacing);
                    shiftSeq.getChildren().add(tt);
                }

                shiftSeq.setOnFinished(evt -> {
                    for (int i = index; i < list.size(); i++) {
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

                    headIndex = list.isEmpty() ? -1 : 0;
                    redrawArrows();
                    updateHeadPosition();
                    log(removed + " deleted at index " + index);

                    removeAllNodeVisualsIfEmpty();
                    endAnimation();
                });

                shiftSeq.play();
            });

            vanish.play();
        });
    }

    private void updateHeadPosition() {
        if (list.isEmpty()) {
            // hide
            detachHeadBinding();
            headLabel.setVisible(false);
            return;
        }
        // show above first node (no animation) — used for operations that don't require attached movement
        ensureNodePresent(0);
        detachHeadBinding();
        double targetX = nodeBoxes[0].getLayoutX() + boxWidth / 2.0 - headLabel.getWidth() / 2.0;
        double targetY = nodeBoxes[0].getLayoutY() - 30;
        headLabel.setLayoutX(targetX);
        headLabel.setLayoutY(targetY);
        headLabel.setTranslateX(0);
        headLabel.setTranslateY(0);
        headLabel.setVisible(true);
    }

    private void animateHeadTraversal(int targetIndex, Runnable onFinished) {
        if (list.isEmpty() || targetIndex < 0) {
            if (onFinished != null) onFinished.run();
            return;
        }

        // treat traversal as an animation unit
        beginAnimation();

        for (int i = 0; i <= targetIndex && i < list.size(); i++) ensureNodePresent(i);

        Timeline timeline = new Timeline();
        double stepMs = 400;
        for (int i = 0; i <= targetIndex; i++) {
            final int idx = i;
            KeyFrame kfOn = new KeyFrame(Duration.millis(i * stepMs), ev -> {
                headIndex = idx;
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
            // traversal finished
            if (onFinished != null) onFinished.run();
            endAnimation();
        });
        timeline.getKeyFrames().add(end);
        timeline.play();
    }

    // redraw arrows between consecutive nodes
    private void redrawArrows() {
        for (Arrow a : arrows) {
            a.unbind();
            workArea.getChildren().removeAll(a.line, a.head);
        }
        arrows.clear();

        for (int i = 0; i < list.size() - 1; i++) {
            if (!workArea.getChildren().contains(nodeBoxes[i])) ensureNodePresent(i);
            if (!workArea.getChildren().contains(nodeBoxes[i + 1])) ensureNodePresent(i + 1);

            NodeBox from = nodeBoxes[i];
            NodeBox to = nodeBoxes[i + 1];
            Arrow a = drawArrow(from, to);
            arrows.add(a);
        }

        for (NodeBox nb : nodeBoxes) if (workArea.getChildren().contains(nb)) nb.toFront();
        // ensure head is on top too
        if (headLabel.isVisible()) headLabel.toFront();
    }

    private Arrow drawArrow(NodeBox from, NodeBox to) {
        double padding = 8;
        double arrowSize = 10;

        DoubleBinding startX = Bindings.createDoubleBinding(
                () -> from.getLayoutX() + from.getTranslateX() + boxWidth - padding,
                from.layoutXProperty(), from.translateXProperty()
        );
        DoubleBinding startY = Bindings.createDoubleBinding(
                () -> from.getLayoutY() + from.getTranslateY() + boxHeight / 2.0,
                from.layoutYProperty(), from.translateYProperty()
        );

        DoubleBinding endX = Bindings.createDoubleBinding(
                () -> to.getLayoutX() + to.getTranslateX() - padding,
                to.layoutXProperty(), to.translateXProperty()
        );
        DoubleBinding endY = Bindings.createDoubleBinding(
                () -> to.getLayoutY() + to.getTranslateY() + boxHeight / 2.0,
                to.layoutYProperty(), to.translateYProperty()
        );

        Line line = new Line();
        line.startXProperty().bind(startX);
        line.startYProperty().bind(startY);
        line.endXProperty().bind(endX);
        line.endYProperty().bind(endY);
        line.setStroke(Color.WHITE);
        line.setStrokeWidth(2);

        Polygon head = new Polygon(0.0, 0.0,
                -arrowSize, -arrowSize / 2.0,
                -arrowSize, arrowSize / 2.0);
        head.setFill(Color.WHITE);
        head.layoutXProperty().bind(endX);
        head.layoutYProperty().bind(endY);

        workArea.getChildren().addAll(line, head);

        Arrow a = new Arrow(line, head, startX, startY, endX, endY);
        return a;
    }

    public HBox getRoot(){return root;}
}
