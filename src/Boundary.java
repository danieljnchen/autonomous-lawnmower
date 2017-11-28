import javafx.scene.paint.Color;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Boundary extends UIObject {
    ArrayList<Point2D> outerBound = new ArrayList<>();
    ArrayList<ArrayList<Point2D>> innerBounds = new ArrayList<>();

    public void draw() {
        gc.setStroke(Color.RED);
        // Draw outer boundaries
        for (int i = 0; i < outerBound.size() - 1; i++) {
            gc.strokeLine(outerBound.get(i).getX(), outerBound.get(i).getY(), outerBound.get(i+1).getX(), outerBound.get(i+1).getY());
        }

        // Draw inner boundaries
        for (ArrayList<Point2D> bound : innerBounds) {
            for (int i = 0; i < bound.size(); i++) {
                gc.strokeLine(bound.get(i).getX(), bound.get(i).getY(), bound.get(i+1).getX(), bound.get(i+1).getY());
            }
        }
    }
}
