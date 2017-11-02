import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.awt.geom.Point2D;

public class Main extends Application {
    static Robot robot = new Robot();
    static Algorithm algorithm = new Algorithm(robot);

    public static void main(String[] args) {
        algorithm.generatePath();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Algorithm Simulation");
        Group root = new Group();
        Canvas canvas = new Canvas(800, 600);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        canvas.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent -> robot.pathNodes.add(new Point2D.Double(mouseEvent.getSceneX(), mouseEvent.getSceneY())));

        root.getChildren().add(canvas);
        primaryStage.setScene(new Scene(root));

        new AnimationTimer()
        {
            public void handle(long currentNanoTime)
            {
                gc.clearRect(0, 0, 800, 600);
                drawShapes(gc);
            }
        }.start();

        primaryStage.show();
    }

    private void drawShapes(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(3);

        for (int i = 0; i < robot.pathNodes.size() - 1; i++) {
            gc.strokeLine(robot.pathNodes.get(i).getX(), robot.pathNodes.get(i).getY(), robot.pathNodes.get(i+1).getX(), robot.pathNodes.get(i+1).getY());
        }

        if (robot.pathNodes.size() != 0) {
            gc.fillOval(robot.pathNodes.get(robot.pathNodes.size()-1).getX(), robot.pathNodes.get(robot.pathNodes.size()-1).getY(), 5, 5);
            robot.approachNextNode();
        }

        gc.fillRect(robot.pos.getX(), robot.pos.getY(), robot.width, robot.height);
    }
}
