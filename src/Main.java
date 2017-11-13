import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Main extends Application {
	static Robot robot = new Robot();
	static Algorithm algorithm = new Algorithm(robot);

	public static void main(String[] args) {
		algorithm.outerBoundary.add(new Point2D(0, 0));
		algorithm.outerBoundary.add(new Point2D(100, 0));
		algorithm.outerBoundary.add(new Point2D(100, 200));
		algorithm.outerBoundary.add(new Point2D(200, 200));
		algorithm.outerBoundary.add(new Point2D(200, 400));
		algorithm.outerBoundary.add(new Point2D(200, 400));
		algorithm.outerBoundary.add(new Point2D(100, 500));
		algorithm.outerBoundary.add(new Point2D(0, 500));

		algorithm.generatePath();
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Algorithm Simulation");
		Group root = new Group();
		Canvas canvas = new Canvas(800, 600);
		GraphicsContext gc = canvas.getGraphicsContext2D();

		canvas.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent -> robot.pathNodes.add(new Point2D(mouseEvent.getSceneX(), mouseEvent.getSceneY())));


		root.getChildren().add(canvas);
		primaryStage.setScene(new Scene(root));

		new AnimationTimer() {
			public void handle(long currentNanoTime) {
				gc.clearRect(0, 0, 800, 600);
				drawShapes(gc);
			}
		}.start();

		primaryStage.show();
	}

	private void drawShapes(GraphicsContext gc) {
		gc.setLineWidth(4);

		// Draw outer boundaries
		gc.setStroke(Color.BLUE);
		for (int i = 0; i < algorithm.outerBoundary.size() - 1; i++) {
			gc.strokeLine(algorithm.outerBoundary.get(i).getX(), algorithm.outerBoundary.get(i).getY(), algorithm.outerBoundary.get(i + 1).getX(), algorithm.outerBoundary.get(i + 1).getY());
		}

		// Draw inner boundaries
		gc.setStroke(Color.OLIVEDRAB);
		for (ArrayList bound : algorithm.innerBoundaries) {
			for (int i = 0; i < bound.size(); i++) {
				gc.strokeLine(robot.pathNodes.get(i).getX(), robot.pathNodes.get(i).getY(), robot.pathNodes.get(i + 1).getX(), robot.pathNodes.get(i + 1).getY());
			}
		}

		// Draw robot node path
		gc.setLineWidth(2);
		gc.setStroke(Color.RED);
		for (int i = 0; i < robot.pathNodes.size() - 1; i++) {
			gc.strokeLine(robot.pathNodes.get(i).getX(), robot.pathNodes.get(i).getY(), robot.pathNodes.get(i + 1).getX(), robot.pathNodes.get(i + 1).getY());
		}

		if (robot.pathNodes.size() != 0) {
			gc.fillOval(robot.pathNodes.get(robot.pathNodes.size() - 1).getX(), robot.pathNodes.get(robot.pathNodes.size() - 1).getY(), 5, 5);
			robot.approachNextNode();
		}

		// Draw robot
		gc.fillRect(robot.pos.getX() - robot.width / 2, robot.pos.getY() - robot.height / 2, robot.width, robot.height);
	}
}
