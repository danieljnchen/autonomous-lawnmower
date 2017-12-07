import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;

public class Editor extends Application {
	private static ArrayList<Point2D> perimeter = new ArrayList<>();
	private static boolean drawing = false;
	private static String fileName = "save.txt";

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Perimeter Editor");
		Group root = new Group();
		Canvas canvas = new Canvas(800, 600);
		GraphicsContext gc = canvas.getGraphicsContext2D();

		root.getChildren().add(canvas);
		primaryStage.setScene(new Scene(root));

		canvas.addEventFilter(MouseEvent.MOUSE_MOVED, mouseEvent -> {
			if (drawing) {
				perimeter.add(new Point2D(mouseEvent.getSceneX(), mouseEvent.getSceneY()));
				gc.fillOval(perimeter.get(perimeter.size() - 1).getX(), perimeter.get(perimeter.size() - 1).getY(), 3, 3);
			}
		});
		canvas.addEventFilter(MouseEvent.MOUSE_CLICKED, mouseEvent -> drawing = !drawing);

		primaryStage.show();

		Button exit = new Button("Done");
		exit.setOnAction(value -> {
			savePerimeter();
			primaryStage.close();
		});
		root.getChildren().add(exit);
	}

	public void savePerimeter() {
		try {
			PrintWriter out = new PrintWriter(fileName);

			for (Point2D e : perimeter) {
				out.println(e.getX() + " " + e.getY());
			}

			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static ArrayList<Point2D> loadPerimeter() {
		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			String line;

			while ((line = bufferedReader.readLine()) != null) {
				perimeter.add(new Point2D(Double.valueOf(line.substring(0, line.indexOf(" "))), Double.valueOf(line.substring(line.indexOf(" ") + 1))));
				/*System.out.println(line.substring(0, line.indexOf(" ")));
				System.out.println(line.substring(line.indexOf(" ")+1));*/
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return perimeter;
	}
}
