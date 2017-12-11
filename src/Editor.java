import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.awt.geom.Point2D;
import java.io.*;
import java.util.ArrayList;
import java.util.Optional;

public class Editor extends Application {
    private static ArrayList<ArrayList<Point2D>> perimeters = new ArrayList<>();

    enum DrawMode {
        NONE,
        DRAW,
        ERASE
    }

    private Color[] paintCycle = { Color.BLACK, Color.ORANGE, Color.GREEN, Color.RED, Color.BLUE, Color.SALMON };
    private DrawMode drawMode = DrawMode.NONE;
    private String fileName = "out.txt";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Editor");

        Group root = new Group();
        Canvas canvas = new Canvas(800,600);

        GraphicsContext gc = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);
        stage.setScene(new Scene(root));
        stage.show();

        Button new_perimeter = new Button("New perimeter");
        new_perimeter.setOnAction(actionEvent -> perimeters.add(new ArrayList<>()));
        root.getChildren().add(new_perimeter);

        Button load_boundary = new Button("Load boundary");
        load_boundary.setLayoutX(100);
        load_boundary.setOnAction(actionEvent -> {
            try {
                loadBoundary();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        root.getChildren().add(load_boundary);

        Button save_boundary = new Button("Save boundary");
        save_boundary.setLayoutX(200);
        save_boundary.setOnAction(actionEvent -> saveBoundary());
        root.getChildren().add(save_boundary);

        Button clear_boundary = new Button("Clear boundary");
        clear_boundary.setLayoutX(300);
        clear_boundary.setOnAction(actionEvent -> clearBoundary());
        root.getChildren().add(clear_boundary);

        ToggleGroup group = new ToggleGroup();
        RadioButton buttonModeDraw = new RadioButton("Draw");
        buttonModeDraw.setToggleGroup(group);
        buttonModeDraw.setSelected(true);
        RadioButton buttonModeErase = new RadioButton("Erase");
        buttonModeErase.setToggleGroup(group);

        canvas.addEventFilter(MouseEvent.MOUSE_MOVED, mouseEvent -> {
            Point2D mousePoint = new Point2D.Double(mouseEvent.getSceneX(), mouseEvent.getSceneY());

            switch (drawMode) {
                case DRAW:
                    drawPoint(mousePoint);
                    break;
                case ERASE:
                    erasePoint(mousePoint, 10);
                    break;
            }
        });

        canvas.addEventFilter(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            switch (mouseEvent.getButton()) {
                case PRIMARY:
                    if (drawMode == DrawMode.NONE) {
                        drawMode = DrawMode.DRAW;
                    } else {
                        drawMode = DrawMode.NONE;
                    }
                    break;
                /*case SECONDARY:
                    if (drawMode == DrawMode.NONE) {
                        drawMode = DrawMode.ERASE;
                    } else if (drawMode == DrawMode.DRAW) {
                        drawMode = DrawMode.ERASE;
                    } else if (drawMode == DrawMode.ERASE) {
                        drawMode = DrawMode.NONE;
                    }
                    break;*/
            }

            // Run the draw to place a single point
            drawPoint(new Point2D.Double(mouseEvent.getSceneX(), mouseEvent.getSceneY()));
        });

        stage.setOnCloseRequest(event -> {
            exit();
            event.consume();
        });

                AnimationTimer animator = new AnimationTimer(){
            @Override
            public void handle(long arg0) {
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
        if (result.get() == buttonSave){
            saveBoundary();
            System.exit(0);
        } else if (result.get() == buttonNoSave) {
            System.exit(0);
        } else {

        }
    }

    private void drawPoint(Point2D point) {
        if (perimeters.size() == 0) perimeters.add(new ArrayList<>());

        perimeters.get(perimeters.size() - 1).add(point);
    }

    private void erasePoint(Point2D point, int radius) {
        ArrayList<Point2D> perim = null;

        for (ArrayList<Point2D> perimeter : perimeters) {
            for (Point2D pt : perimeter) {
                if (point.distance(pt) < radius) {
                    perim = perimeter;
                    break;
                }
            }
        }

        if (perim != null) perimeters.remove(perim);
    }

    private void saveBoundary() {
        try {
            PrintWriter out = new PrintWriter(fileName);
            for (ArrayList<Point2D> p : perimeters) {
                out.println("Perimeter");
                for(Point2D e : p) {
                    out.println(e.getX() + " " + e.getY());
                }
            }

            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void loadBoundary() throws IOException {
        // Clear current bounds
        perimeters.clear();

        FileReader fileReader = new FileReader(fileName);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        String line;
        while ((line = bufferedReader.readLine()) != null) {
            if (line.equals("Perimeter")) {
                perimeters.add(new ArrayList<>());
                continue;
            }

            perimeters.get(perimeters.size()-1).add(new Point2D.Double(Double.valueOf(line.substring(0, line.indexOf(" "))), Double.valueOf(line.substring(line.indexOf(" ")+1))));
        }
    }

    private void clearBoundary() {
        perimeters.clear();
    }

    public void draw(GraphicsContext gc) {
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

        // Perimeters
        int i = 0;
        for (ArrayList<Point2D> perimeter : perimeters) {
            int j = 0;
            if (perimeter.size() > 0) {
                gc.setFill(paintCycle[i % paintCycle.length]);
                gc.setStroke(paintCycle[i % paintCycle.length]);

                for (Point2D point : perimeter) {
                    gc.fillOval(point.getX(), point.getY(), 3, 3);
                    gc.strokeLine(point.getX(), point.getY(),
                            perimeter.get((j + 1) % perimeter.size()).getX(), perimeter.get((j + 1) % perimeter.size()).getY());

                    j++;
                }
            }

            i++;
        }
    }
}
