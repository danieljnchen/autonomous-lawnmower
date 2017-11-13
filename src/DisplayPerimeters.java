import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;

import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class DisplayPerimeters extends Application {
    private static String fileName = "out.txt";
    private static String line = null;
    private static ArrayList<Point2D>  perimeter = new ArrayList<>();

    public static void main(String[] args) {
        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while((line = bufferedReader.readLine()) != null) {
                perimeter.add(new Point2D.Double(Double.valueOf(line.substring(0, line.indexOf(" "))), Double.valueOf(line.substring(line.indexOf(" ")+1))));
               /*System.out.println(line.substring(0, line.indexOf(" ")));
               System.out.println(line.substring(line.indexOf(" ")+1));*/
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
        for(int i = 0; i<perimeter.size()-1; ++i) {
            gc.strokeLine(perimeter.get(i).getX(),perimeter.get(i).getY(),perimeter.get(i+1).getX(),perimeter.get(i+1).getY());
        }
        gc.strokeLine(perimeter.get(perimeter.size()-1).getX(),perimeter.get(perimeter.size()-1).getY(),perimeter.get(0).getX(),perimeter.get(0).getY());
        new AnimationTimer() {
            public void handle(long currentNanoTime){}
        }.start();
    }
}