import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Optional;

public class Editor extends Application {
    static Boundary boundary = new Boundary();

    enum DrawMode {
        NONE,
        DRAW,
        ERASE
    }

    private DrawMode drawMode = DrawMode.NONE;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Editor");

        Group root = new Group();
        Canvas canvas = new Canvas(800, 600);

        GraphicsContext gc = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);
        stage.setScene(new Scene(root));
        stage.show();

        Button new_perimeter = new Button("New perimeter");
        new_perimeter.setOnAction(actionEvent -> boundary.bounds.add(new ArrayList<>()));
        root.getChildren().add(new_perimeter);

        Button load_boundary = new Button("Load boundary");
        load_boundary.setLayoutX(100);
        load_boundary.setOnAction(actionEvent -> boundary.load(Boundary.saveLocation));
        root.getChildren().add(load_boundary);

        Button save_boundary = new Button("Save boundary");
        save_boundary.setLayoutX(200);
        save_boundary.setOnAction(actionEvent -> boundary.save(Boundary.saveLocation));
        root.getChildren().add(save_boundary);

        Button clear_boundary = new Button("Clear boundary");
        clear_boundary.setLayoutX(300);
        clear_boundary.setOnAction(actionEvent -> boundary.clear());
        root.getChildren().add(clear_boundary);

        ToggleButton tb1 = new ToggleButton("Erase mode");
        tb1.setLayoutY(50);
        root.getChildren().add(tb1);

        canvas.addEventFilter(MouseEvent.MOUSE_MOVED, mouseEvent -> runDraw(new Point2D(mouseEvent.getSceneX(), mouseEvent.getSceneY())));

        canvas.addEventFilter(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            if (drawMode == DrawMode.NONE) {
                if (tb1.isSelected()) {
                    drawMode = DrawMode.ERASE;
                } else {
                    drawMode = DrawMode.DRAW;
                }
            } else {
                drawMode = DrawMode.NONE;
            }

            // Run the draw to place a single point
            runDraw(new Point2D(mouseEvent.getSceneX(), mouseEvent.getSceneY()));
        });

        /*stage.setOnCloseRequest(event -> {
            exit();
            event.consume();
        });*/

        AnimationTimer animator = new AnimationTimer() {
            @Override
            public void handle(long arg0) {
                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                draw(gc);
            }
        };
        animator.start();
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
            boundary.save(Boundary.saveLocation);
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

    private void draw(GraphicsContext gc) {
        for (UIObject obj : UIObject.uiObjects) {
            obj.draw(gc);
        }
    }
}
