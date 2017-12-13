import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import java.util.ArrayList;

public class Testing extends Application {

    static ArrayList<UIObject> uiObjects = new ArrayList<>();
    static Boundary boundary = new Boundary();
    static Raycast r;

    public static void main(String[] args) {
        boundary.outerBound.add(new Point2D(0, 0));
        boundary.outerBound.add(new Point2D(100, 0));
        boundary.outerBound.add(new Point2D(100, 200));
        boundary.outerBound.add(new Point2D(200, 200));
        boundary.outerBound.add(new Point2D(200, 400));
        boundary.outerBound.add(new Point2D(200, 400));
        boundary.outerBound.add(new Point2D(100, 500));
        boundary.outerBound.add(new Point2D(0, 500));

        launch(args);
    }

    public void start(Stage primaryStage) {
        primaryStage.setTitle("Algorithm Simulation");
        Group root = new Group();
        Canvas canvas = new Canvas(800, 600);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        UIObject.gc = gc;
        r = new Raycast(new Point2D(10,10),Math.PI/3, boundary);


        root.getChildren().add(canvas);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        new AnimationTimer()
        {
            public void handle(long currentNanoTime)
            {
                gc.clearRect(0, 0, 800, 600);
                drawShapes(gc);
            }
        }.start();
    }

    private void drawShapes(GraphicsContext gc) {
        for (UIObject obj : uiObjects) {
            obj.draw();
        }
    }
}
