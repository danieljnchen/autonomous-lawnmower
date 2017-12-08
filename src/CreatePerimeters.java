import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import javafx.stage.*;
import javafx.scene.*;

import java.awt.geom.Point2D;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class CreatePerimeters extends Application {
    private static boolean drawing = false;
    private static ArrayList<ArrayList<Point2D>> perimeters = new ArrayList<>();
    private Paint[] paintCycle = {Paint.valueOf("Black"), Paint.valueOf("Yellow"), Paint.valueOf("Green"), Paint.valueOf("Red"), Paint.valueOf("Blue"), Paint.valueOf("Orange")};

    public static void main(String[] args) {
        Application.launch(args);
    }

    private void exit() {
        try {
            PrintWriter out = new PrintWriter("out.txt");
            for(ArrayList<Point2D> p : perimeters) {
                out.println("Perimeter");
                for(Point2D e : p) {
                    out.println(e.getX() + " " + e.getY());
                }
            }
            out.close();
            System.exit(0);
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        perimeters.add(new ArrayList<>());
        primaryStage.setTitle("Drawing");
        Group root = new Group();
        Canvas canvas = new Canvas(500,800);

        GraphicsContext gc = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        Button exit = new Button("Done");
        exit.setOnAction(actionEvent -> exit());
        exit.setLayoutX(50);
        exit.setLayoutY(50);
        root.getChildren().add(exit);

        Button newPerimeter = new Button("New Perimeter");
        newPerimeter.setOnAction(actionEvent -> perimeters.add(new ArrayList<>()));
        root.getChildren().add(newPerimeter);
        canvas.addEventFilter(MouseEvent.MOUSE_MOVED, mouseEvent -> {
            if(drawing) {
                gc.setFill(paintCycle[(perimeters.size() - 1) % paintCycle.length]);
                perimeters.get(perimeters.size() - 1).add(new Point2D.Double(mouseEvent.getSceneX(), mouseEvent.getSceneY()));
                gc.fillOval(perimeters.get(perimeters.size() - 1).get(perimeters.get(perimeters.size() - 1).size()-1).getX(),
                        perimeters.get(perimeters.size() - 1).get(perimeters.get(perimeters.size() - 1).size()-1).getY(),3,3);
            }
        });
        canvas.addEventFilter(MouseEvent.MOUSE_CLICKED, mouseEvent -> drawing = !drawing);
        new AnimationTimer() {
            public void handle(long currentNanoTime) {}
        }.start();
    }
}