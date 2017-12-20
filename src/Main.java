import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Main extends Application {
    static Robot robot = new Robot();
    static Boundary boundary = new Boundary();
    static Algorithm algorithm = new Algorithm(robot, boundary);

    public static void main(String[] args) {
        //boundary.load(Boundary.saveLocation);

        boundary.bounds.add(new ArrayList<>());
        boundary.getOuterBound().add(new Point2D(100, 100));
        boundary.getOuterBound().add(new Point2D(200, 100));
        boundary.getOuterBound().add(new Point2D(200, 200));
        boundary.getOuterBound().add(new Point2D(100, 200));

        try {
            Raycast r = new Raycast(new Point2D(80, 50), 90);
        } catch (NoHitException e) {
            e.printStackTrace();
        }
        //algorithm.generatePath();

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Algorithm");
        Group root = new Group();
        Canvas canvas = new Canvas(800, 600);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        root.getChildren().add(canvas);
        primaryStage.setScene(new Scene(root));

        new AnimationTimer()
        {
            public void handle(long currentNanoTime)
            {
                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                draw(gc);
            }
        }.start();

        primaryStage.show();
    }

    private void draw(GraphicsContext gc) {
        for (UIObject obj : UIObject.uiObjects) {
            obj.draw(gc);
        }
    }
}
