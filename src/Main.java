import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.awt.geom.Point2D;

public class Main extends Application {
    public static void main(String[] args) {
        Algorithm.boundaryPoints = new Point2D[7];
        Algorithm.boundaryPoints[0] = new Point2D.Double(0, 0);
        Algorithm.boundaryPoints[1] = new Point2D.Double(200, 0);
        Algorithm.boundaryPoints[2] = new Point2D.Double(200, 200);
        Algorithm.boundaryPoints[3] = new Point2D.Double(400, 200);
        Algorithm.boundaryPoints[4] = new Point2D.Double(400, 400);
        Algorithm.boundaryPoints[5] = new Point2D.Double(0, 400);
        Algorithm.boundaryPoints[6] = new Point2D.Double(0, 0);

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Algorithm Simulation");
        Group root = new Group();
        Canvas canvas = new Canvas(800, 600);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        drawShapes(gc);
        root.getChildren().add(canvas);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    private void drawShapes(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(3);

        for (int i = 0; i < Algorithm.boundaryPoints.length; i++) {
            gc.strokeLine(Algorithm.boundaryPoints[i].getX(), Algorithm.boundaryPoints[i].getY(), Algorithm.boundaryPoints[i+1].getX(), Algorithm.boundaryPoints[i+1].getY());
        }
    }
}
