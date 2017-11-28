import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class DisplayPerimeters extends Application {
    private static String fileName = "out.txt";
    private static String line = null;
    private static ArrayList<ArrayList<Point2D>> perimeters = new ArrayList<>();
    private Paint[] paintCycle = {Paint.valueOf("Black"), Paint.valueOf("Yellow"), Paint.valueOf("Green"), Paint.valueOf("Red"), Paint.valueOf("Blue"), Paint.valueOf("Orange")};

    public static void main(String[] args) {
        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while((line = bufferedReader.readLine()) != null) {
                if(line.equals("Perimeter")) {
                    perimeters.add(new ArrayList<>());
                    continue;
                }
                perimeters.get(perimeters.size()-1).add(new Point2D.Double(Double.valueOf(line.substring(0, line.indexOf(" "))), Double.valueOf(line.substring(line.indexOf(" ")+1))));
               System.out.println(line.substring(0, line.indexOf(" ")));
               System.out.println(line.substring(line.indexOf(" ")+1));
            }
            Application.launch(args);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Perimeter");
        Group root = new Group();
        Canvas canvas = new Canvas(500,800);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        for(int i = 0; i<perimeters.size(); ++i) {
            gc.setStroke(paintCycle[i]);
            for(int j = 0; j<perimeters.get(i).size()-1; ++j) {
                gc.strokeLine(perimeters.get(i).get(j).getX(),perimeters.get(i).get(j).getY(),perimeters.get(i).get(j+1).getX(),perimeters.get(i).get(j+1).getY());
                gc.strokeLine(perimeters.get(i).get(perimeters.get(i).size()-1).getX(),perimeters.get(i).get(perimeters.get(i).size()-1).getY(),perimeters.get(i).get(0).getX(),perimeters.get(i).get(0).getY());
            }
        }

        new AnimationTimer() {
            public void handle(long currentNanoTime){}
        }.start();
    }
}