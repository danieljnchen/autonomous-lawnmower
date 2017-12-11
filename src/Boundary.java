import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Boundary extends UIObject {
    ArrayList<Point2D> outerBound = new ArrayList<>();
    ArrayList<ArrayList<Point2D>> innerBounds = new ArrayList<>();
    ArrayList<ArrayList<Point2D>> allBounds = new ArrayList<>();

    public Boundary() {
        allBounds.add(outerBound);
        allBounds.addAll(innerBounds);
    }

    public void addOuter(Point2D p) {
        outerBound.add(p);
        allBounds.get(0).add(p);
    }

    public void addInner(Point2D p) {
        innerBounds.get(innerBounds.size()-1).add(p);
        allBounds.get(innerBounds.size()).add(p);
    }

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
