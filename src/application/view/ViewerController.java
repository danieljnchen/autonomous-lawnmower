package application.view;

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
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;

public class ViewerController {
    public static ArrayList<UIObject> uiObjects = new ArrayList<>();

    public static Robot robot = new Robot(uiObjects);
    public static Boundary boundary = new Boundary(uiObjects);
    public static Algorithm algorithm = new Algorithm();
    public static Mouse mouse = new Mouse(uiObjects);

    @FXML private Canvas canvas;
    @FXML private ComboBox<String> boundarySelector;
    @FXML private Button reset;
    @FXML private Button refreshBoundaries;
    @FXML private Button openEditor;

    private GraphicsContext gc;

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
        for (UIObject obj : uiObjects) {
            obj.draw(gc);
        }
    }

    private void addEventHandlers() {
        boundarySelector.setOnAction(event -> boundary.load(boundarySelector.getValue()));
        reset.setOnAction(event -> boundary.load(boundarySelector.getValue()));
        refreshBoundaries.setOnAction(event -> loadBoundaries());
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

    private void loadBoundaries() {
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
            System.out.println("openEditor");
            Parent root = FXMLLoader.load(getClass().getResource("editor.fxml"));
            Stage primaryStage = new Stage();
            primaryStage.setTitle("IntelliMow Editor");
            primaryStage.setScene(new Scene(root));
            primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("../img/icon.png")));
            primaryStage.show();
        } catch (Exception ignored) {

        }
    }
}
