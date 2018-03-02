package ui;

import algorithm.*;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.File;

public class ViewerController {
    public static Robot robot = new Robot();
    public static Boundary boundary = new Boundary();
    public static Algorithm algorithm = new Algorithm();
    public static Mouse mouse = new Mouse();

    @FXML private Canvas canvas;
    @FXML private ComboBox<String> boundarySelector;
    @FXML private Button reset;
    @FXML private Button openEditor;

    GraphicsContext gc;

    @FXML
    public void initialize() {
        gc = canvas.getGraphicsContext2D();

        new AnimationTimer() {
            public void handle(long currentNanoTime) {
                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                draw(gc);
            }
        }.start();

        addEventHandlers();
        loadBoundaries();
    }

    private void draw(GraphicsContext gc) {
        for (UIObject obj : UIObject.uiObjects) {
            obj.draw(gc);
        }
    }

    private void addEventHandlers() {
        boundarySelector.setOnAction(event -> boundary.load(boundarySelector.getValue()));
        reset.setOnAction(event -> boundary.load(boundarySelector.getValue()));
        openEditor.setOnAction((event -> openEditor()));

        // Track mouse
        canvas.addEventHandler(MouseEvent.MOUSE_MOVED, mouseEvent -> {
            mouse.setPos(mouseEvent.getX(), mouseEvent.getY());
        });

        // Create path on mouse click
        canvas.addEventFilter(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            algorithm.raycastIterative(
                    mouse.getClosestPoint().add(robot.width / 2, robot.width / 2),
                    0,
                    false);
            algorithm.addPathToRobot();
        });
    }

    public void loadBoundaries() {
        // Populate the list with all saves located in the saves folder
        File dir = new File("saves");
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                boundarySelector.getItems().add(child.getName());
            }
        } else {
            dir.mkdir();
        }

        if (boundarySelector.getItems().size() > 0) {
            // Select the default
            boundarySelector.setValue(boundarySelector.getItems().get(0));
            boundary.load(boundarySelector.getValue());
        }
    }

    private void openEditor() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("editor.fxml"));
            Stage primaryStage = new Stage();
            primaryStage.setTitle("IntelliMow Editor");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (Exception e) {

        }
    }
}
