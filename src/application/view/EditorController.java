package application.view;

import algorithm.Boundary;
import algorithm.UIObject;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;

import java.io.File;
import java.util.ArrayList;
import java.util.Optional;

public class EditorController {
    public static ArrayList<UIObject> uiObjects = new ArrayList<>();

    public static Boundary boundary = new Boundary(uiObjects);

    @FXML private Canvas canvas;
    @FXML private ComboBox<String> boundarySelector;
    @FXML private Button reset;
    @FXML private Button save;
    @FXML private Button delete;
    @FXML private CheckBox eraseMode;
    @FXML private Button newPerimeter;
    @FXML private Circle currentPerimeter;

    private GraphicsContext gc;
    private final double MIN_DRAW_SPACING = 5;

    enum DrawMode {
        NONE,
        DRAW,
        ERASE
    }

    private DrawMode drawMode = DrawMode.NONE;

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
        reset.setOnAction(event -> {
            boundary.load(boundarySelector.getValue());
            updatePerimColor();
        });
        save.setOnAction(event -> {
            boundary.save(boundarySelector.getValue());
            loadBoundaries();
        });
        delete.setOnAction(event -> {
            Boundary.delete(boundarySelector.getValue());
            loadBoundaries();
        });
        newPerimeter.setOnAction(event -> {
            boundary.bounds.add(new ArrayList<>());
            updatePerimColor();
        });

        canvas.addEventFilter(MouseEvent.MOUSE_DRAGGED, mouseEvent -> {
            if (eraseMode.isSelected()) {
                drawMode = DrawMode.ERASE;
            } else {
                drawMode = DrawMode.DRAW;
            }

            // Run the draw to place a single point
            runDraw(new Point2D(mouseEvent.getX(), mouseEvent.getY()));
        });

        canvas.addEventFilter(MouseEvent.MOUSE_RELEASED, mouseEvent -> drawMode = DrawMode.NONE);
    }

    private void loadBoundaries() {
        boundarySelector.getItems().clear();

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

        // Load the default
        boundarySelector.setValue(boundarySelector.getItems().get(0));
    }

    private void exit() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Do you want to save changes before exiting?");

        ButtonType buttonSave = new ButtonType("Save and exit");
        ButtonType buttonNoSave = new ButtonType("Exit without saving");
        ButtonType buttonCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonSave, buttonNoSave, buttonCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonSave) {
            boundary.save(boundarySelector.getValue());
            System.exit(0);
        } else if (result.get() == buttonNoSave) {
            System.exit(0);
        }
    }

    private void runDraw(Point2D point) {
        switch (drawMode) {
            case DRAW:
                drawPoint(point);
                break;
            case ERASE:
                erasePoint(point, 15);
                break;
        }
    }

    private void drawPoint(Point2D point) {
        if (boundary.bounds.size() == 0) {
            boundary.bounds.add(new ArrayList<>());
        }

        ArrayList<Point2D> curBound = boundary.bounds.get(boundary.bounds.size() - 1);

        if (curBound.size() > 0 && curBound.get(curBound.size() - 1).distance(point) < MIN_DRAW_SPACING) return;

        boundary.bounds.get(boundary.bounds.size() - 1).add(point);
    }

    private void erasePoint(Point2D point, int radius) {
        ArrayList<Point2D> targetPar = null;
        Point2D target = null;

        for (ArrayList<Point2D> bound : boundary.bounds) {
            for (Point2D pt : bound) {
                if (point.distance(pt) < radius) {
                    targetPar = bound;
                    target = pt;
                    break;
                }
            }
        }

        if (targetPar != null) {
            targetPar.remove(target);

            if (targetPar.size() == 0) {
                boundary.bounds.remove(targetPar);
            }
        }
    }

    private void updatePerimColor() {
        if (boundary.bounds.size() == 0) {
            currentPerimeter.setFill(boundary.colorCycle[0]);
            return;
        }

        currentPerimeter.setFill(boundary.colorCycle[boundary.bounds.size()-1]);
    }
}
