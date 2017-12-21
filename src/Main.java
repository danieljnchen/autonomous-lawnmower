import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.awt.geom.Line2D;

public class Main extends Application {
    static Robot robot = new Robot();
    static Boundary boundary = new Boundary();
    static Algorithm algorithm = new Algorithm(robot, boundary);

    public static void main(String[] args) {
        boundary.load(Boundary.saveLocation);

        algorithm.generatePath();

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

        // Create comb on mouse click
        canvas.addEventFilter(MouseEvent.MOUSE_CLICKED, mouseEvent -> algorithm.raycastComb(new Point2D(mouseEvent.getSceneX(), mouseEvent.getSceneY()), -91));

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
