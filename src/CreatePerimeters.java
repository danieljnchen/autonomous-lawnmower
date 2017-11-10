import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.*;
import javafx.scene.*;

import java.awt.geom.Point2D;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class CreatePerimeters extends Application {
    private static boolean drawing = false;
    private static ArrayList<Point2D> perimeter = new ArrayList<>();

    public static void main(String[] args) {
        Application.launch(args);
    }

    private void exit() {
        try {
            PrintWriter out = new PrintWriter("out.txt");
            for(Point2D e : perimeter) {
                out.println(e.getX() + " " + e.getY());
            }
            out.close();
            System.exit(0);
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Drawing");
        Group root = new Group();
        Canvas canvas = new Canvas(500,800);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        Button exit= new Button("Done");
        exit.setOnAction(value -> exit());
        root.getChildren().add(exit);
        canvas.addEventFilter(MouseEvent.MOUSE_MOVED, mouseEvent -> {
            if(drawing) {
                perimeter.add(new Point2D.Double(mouseEvent.getSceneX(), mouseEvent.getSceneY()));
                gc.fillOval(perimeter.get(perimeter.size()-1).getX(), perimeter.get(perimeter.size()-1).getY(),3,3);
            }
        });
        canvas.addEventFilter(MouseEvent.MOUSE_CLICKED, mouseEvent -> drawing = !drawing);
        new AnimationTimer() {
            public void handle(long currentNanoTime) {}
        }.start();
    }
}