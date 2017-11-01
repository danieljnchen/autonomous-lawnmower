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
                drawShapes(gc);
            }
        }.start();

        primaryStage.show();
    }

    private void drawShapes(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(3);

        for (int i = 0; i < Algorithm.boundaryPoints.size() - 1; i++) {
            gc.strokeLine(Algorithm.boundaryPoints.get(i).getX(), Algorithm.boundaryPoints.get(i).getY(), Algorithm.boundaryPoints.get(i+1).getX(), Algorithm.boundaryPoints.get(i+1).getY());
        }

        if (Algorithm.boundaryPoints.size() != 0) {
            gc.fillOval(Algorithm.boundaryPoints.get(Algorithm.boundaryPoints.size()-1).getX(), Algorithm.boundaryPoints.get(Algorithm.boundaryPoints.size()-1).getY(), 5, 5);
            robot.approachPoint(Algorithm.boundaryPoints.get(Algorithm.boundaryPoints.size()-1));
        }

        gc.fillRect(robot.pos.getX(), robot.pos.getY(), robot.width, robot.height);
    }
}
