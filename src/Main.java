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
    RobotSim robot = new RobotSim();

    public static void main(String[] args) {
        Algorithm.boundaryPoints.add(0, new Point2D.Double(100, 100));

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Algorithm Simulation");
        Group root = new Group();
        Canvas canvas = new Canvas(800, 600);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        canvas.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent -> Algorithm.boundaryPoints.add(new Point2D.Double(mouseEvent.getSceneX(), mouseEvent.getSceneY())));

        drawShapes(gc);

        root.getChildren().add(canvas);
        primaryStage.setScene(new Scene(root));

        final long startNanoTime = System.nanoTime();

        new AnimationTimer()
        {
            public void handle(long currentNanoTime)
            {
                gc.clearRect(0, 0, 800, 600);

                double t = (currentNanoTime - startNanoTime) / 1000000000.0;

                drawShapes(gc);
            }
        }.start();

        primaryStage.show();
    }

    private void drawShapes(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(3);

        for (int i = 0; i < Algorithm.boundaryPoints.size(); i++) {
            if (i == Algorithm.boundaryPoints.size() - 1) {
                gc.strokeLine(Algorithm.boundaryPoints.get(i).getX(), Algorithm.boundaryPoints.get(i).getY(),
                        Algorithm.boundaryPoints.get(0).getX(), Algorithm.boundaryPoints.get(0).getY());
            } else {
                gc.strokeLine(Algorithm.boundaryPoints.get(i).getX(), Algorithm.boundaryPoints.get(i).getY(),
                        Algorithm.boundaryPoints.get(i+1).getX(), Algorithm.boundaryPoints.get(i+1).getY());
            }

            gc.fillOval(Algorithm.boundaryPoints.get(i).getX(), Algorithm.boundaryPoints.get(i).getY(), 8, 8);
        }

        robot.approachPoint(Algorithm.boundaryPoints.get(Algorithm.boundaryPoints.size()-1));
        gc.fillRect(robot.pos.getX(), robot.pos.getY(), robot.width, robot.height);
    }
}
