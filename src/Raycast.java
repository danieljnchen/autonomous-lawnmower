import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;


public class Raycast extends UIObject {
    public final Point2D startPoint;
    private double angle;

    private int index;

    private ArrayList<Point2D> hitPoints = new ArrayList<>();
    private ArrayList<Point2D> points1 = new ArrayList<>();
    private ArrayList<Point2D> points2 = new ArrayList<>();

    /***
     * Raycasts a ray at the specified angle. Contains the point the ray intersects with a boundary.
     * @param startPoint point to start raycast
     * @param angle in degrees
     */
    Raycast(Point2D startPoint, double angle) throws NoHitException {
        this.startPoint = startPoint;
        this.angle = angle;

        start(startPoint, angle, Main.boundary);
    }

    private void start(Point2D startPoint, double angle, Boundary boundary) throws NoHitException {
        for (int index = 0; index <= boundary.bounds.get(0).size(); index++) {
            Point2D point1 = boundary.bounds.get(0).get(index % boundary.bounds.get(0).size());
            Point2D point2 = boundary.bounds.get(0).get((index + 1) % boundary.bounds.get(0).size());

            Point2D hitPoint = intersection(startPoint, startPoint.add(5000 * Math.cos(Math.toRadians(angle)), 5000 * Math.sin(Math.toRadians(angle))), point1, point2);

            if (hitPoint != null) {
                points1.add(point1);
                points2.add(point2);
                hitPoints.add(hitPoint);
            }
        }

        if (hitPoints.size() != 0) {
            Point2D hitPoint = hitPoints.get(0);
            for (int i = 1; i < hitPoints.size(); i++) {
                if (startPoint.distance(hitPoints.get(i)) < startPoint.distance(hitPoint)) {
                    hitPoint = hitPoints.get(i);
                    index = i;
                }
            }
        } else {
            index = -1;
            throw new NoHitException(this + " did not hit a target");
        }
    }

    private static Point2D intersection(Point2D line1initial, Point2D line1terminal, Point2D line2initial, Point2D line2terminal) {
        Point2D vector1 = new Point2D(line1terminal.getX() - line1initial.getX(), line1terminal.getY() - line1initial.getY());
        Point2D vector2 = new Point2D(line2terminal.getX() - line2initial.getX(), line2terminal.getY() - line2initial.getY());

        double scalar1, scalar2;
        scalar2 = (vector1.getY() / vector1.getX() * (line2initial.getX() - line1initial.getX()) - (line2initial.getY() - line1initial.getY()))
                / (vector2.getY() * (1 - (vector1.getY() * vector2.getX()) / (vector1.getX() * vector2.getY())));
        scalar1 = (line2initial.getX() - line1initial.getX() + scalar2 * vector2.getX()) / vector1.getX();

        Point2D out;
        if (scalar1 >= 0 && scalar1 <= 1 && scalar2 >= 0 && scalar2 <= 1) {
            out = new Point2D(line1initial.getX() + vector1.getX() * scalar1, line1initial.getY() + vector1.getY() * scalar1);
        } else {
            out = null;
        }

        return out;
    }

    public Point2D getHitPoint() {
        return hitPoints.get(index);
    }

    public void draw(GraphicsContext gc) {
        gc.setStroke(Color.BLUE);
        gc.setFill(Color.DARKBLUE);
        gc.setLineWidth(1);

        if (index != -1) {
            // Target line segment
            //gc.strokeLine(points1.get(index).getX(), points1.get(index).getY(), points2.get(index).getX(), points2.get(index).getY());

            // Hit point
            gc.fillOval(hitPoints.get(index).getX() - 2, hitPoints.get(index).getY() - 2, 4, 4);

            // Raycast line
            gc.strokeLine(startPoint.getX(), startPoint.getY(), hitPoints.get(index).getX(), hitPoints.get(index).getY());
        } else {
            gc.strokeLine(startPoint.getX(), startPoint.getY(), 5000 * Math.cos(Math.toRadians(angle)), startPoint.getY() + 5000 * Math.sin(Math.toRadians(angle)));
        }
    }

    @Override
    public String toString() {
        return "Raycast [" + "startPoint = " + startPoint + ", angle = " + angle + "]";
    }
}

class NoHitException extends Exception {
    NoHitException(String message) {
        super(message);
    }
}
