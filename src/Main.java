import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import java.util.ArrayList;

public class Main extends Application {
    static ArrayList<UIObject> uiObjects = new ArrayList<>();

    static Robot robot = new Robot();
    static Boundary boundary = new Boundary();
    static Algorithm algorithm = new Algorithm(robot, boundary);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Algorithm");
        Group root = new Group();
        Canvas canvas = new Canvas(800, 600);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        UIObject.gc = gc;

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
        for (UIObject obj : uiObjects) {
            obj.draw();
        }
    }
}
