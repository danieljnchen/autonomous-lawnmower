import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.File;

public class Main extends Application {
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

        root.getChildren().add(canvas);
        primaryStage.setScene(new Scene(root));

        Label label1 = new Label("Comb angle (deg)");
        label1.setLayoutY(40);
        TextField comb_angle = new TextField();
        comb_angle.setText("0");
        comb_angle.setLayoutY(60);
        root.getChildren().add(comb_angle);
        root.getChildren().add(label1);

        // Dropdown for boundary selection
        final ComboBox<String> boundary_select = new ComboBox();
        boundary_select.setOnAction(actionEvent -> boundary.load(boundary_select.getValue()));
        root.getChildren().add(boundary_select);

        // Populate the list with all saves located in the saves folder
        File dir = new File("saves");
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                boundary_select.getItems().add(child.getName());
            }
        }

        // Load the default
        boundary_select.setValue(boundary_select.getItems().get(0));
        boundary.load(boundary_select.getValue());

        // Create comb on mouse click
        //canvas.addEventFilter(MouseEvent.MOUSE_CLICKED, mouseEvent ->
        //        algorithm.raycastComb(new Point2D(mouseEvent.getSceneX(), mouseEvent.getSceneY()), Double.parseDouble(comb_angle.getText())));

        canvas.addEventFilter(MouseEvent.MOUSE_CLICKED, mouseEvent ->
                algorithm.raycastIterativeStart(new Point2D(mouseEvent.getSceneX(), mouseEvent.getSceneY()), Double.parseDouble(comb_angle.getText()), false));

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
