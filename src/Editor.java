import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.awt.geom.Point2D;
import java.io.*;
import java.util.ArrayList;

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

        perimeters.add(new ArrayList<>());

        Button newPerimeter = new Button("New Perimeter");
        newPerimeter.setOnAction(actionEvent -> perimeters.add(new ArrayList<>()));
        root.getChildren().add(newPerimeter);

        canvas.addEventFilter(MouseEvent.MOUSE_MOVED, mouseEvent -> {
            Point2D mousePoint = new Point2D.Double(mouseEvent.getSceneX(), mouseEvent.getSceneY());

            switch (drawMode) {
                case DRAW:
                    draw(gc, mousePoint);
                    break;
                case ERASE:
                    erase(gc, mousePoint, 10);
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
            draw(gc, new Point2D.Double(mouseEvent.getSceneX(), mouseEvent.getSceneY()));
        });


        AnimationTimer animator = new AnimationTimer(){
            @Override
            public void handle(long arg0) {}
        };

        animator.start();
    }

    private void exit() {
        saveBoundary();
        System.exit(0);
    }

    private void draw(GraphicsContext gc, Point2D point) {
        gc.setFill(paintCycle[(perimeters.size() - 1) % paintCycle.length]);
        perimeters.get(perimeters.size() - 1).add(new Point2D.Double(point.getX(), point.getY()));
        gc.fillOval(perimeters.get(perimeters.size() - 1).get(perimeters.get(perimeters.size() - 1).size()-1).getX(),
                perimeters.get(perimeters.size() - 1).get(perimeters.get(perimeters.size() - 1).size()-1).getY(),3,3);
    }

    private void erase(GraphicsContext gc, Point2D point, int radius) {
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
        FileReader fileReader = new FileReader(fileName);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        String line;
        while ((line = bufferedReader.readLine()) != null) {
            if (line.equals("Perimeter")) {
                perimeters.add(new ArrayList<>());
                continue;
            }

            perimeters.get(perimeters.size()-1).add(new Point2D.Double(Double.valueOf(line.substring(0, line.indexOf(" "))), Double.valueOf(line.substring(line.indexOf(" ")+1))));

            System.out.println(line.substring(0, line.indexOf(" ")));
            System.out.println(line.substring(line.indexOf(" ")+1));
        }
    }
}
