import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Raycast extends UIObject {
    public final Point2D startPoint;
    public Point2D hitPoint;

    private Point2D cast;

    private Point2D point1;
    private Point2D point2;

    Raycast(Point2D start, double angle) {
        startPoint = start;
        start(start, angle, Main.boundary);
    }

    /***
     * Raycasts in the specified direction.
     * @param start
     * @param angle In degrees
     * @param boundary
     */
    private void start(Point2D start, double angle, Boundary boundary) {
        // Create a new point for us to manipulate
        cast = new Point2D(start.getX(), start.getY());

        // Find the line we are casting towards
        for (int index = 0; index < boundary.getOuterBound().size(); ++index) {
            boolean sign; //false -> angle to cast less than angle, true -> angle to cast greater than angle
            boolean finished = false;

            double angleToIndex = Math.atan2(boundary.getOuterBound().get(index % (boundary.getOuterBound().size() - 1)).getY() - cast.getY(), boundary.getOuterBound().get(index % (boundary.getOuterBound().size() - 1)).getX() - cast.getX());
            double angleToIndex1 = Math.atan2(boundary.getOuterBound().get((index + 1) % (boundary.getOuterBound().size() - 1)).getY(), boundary.getOuterBound().get((index + 1) % (boundary.getOuterBound().size() - 1)).getX());

            if (angleToIndex == angle) {
                finished = true;
            }
            sign = (angleToIndex < angle);
            if ((angleToIndex1 < angle) != sign) {
                finished = true;
            }
            if (finished) {
                point1 = boundary.getOuterBound().get(index % (boundary.getOuterBound().size() - 1));
                point2 = boundary.getOuterBound().get((index + 1) % (boundary.getOuterBound().size() - 1));
                break;
            }
        }

        // Get the intersection
        Point2D intersect = intersection(cast, new Point2D(Double.MAX_VALUE * Math.cos(Math.toRadians(angle)), Double.MAX_VALUE * Math.sin(Math.toRadians(angle))), point1, point2);
        if (intersect != null) hitPoint = intersect;
    }

    public static Point2D intersection(Point2D line1initial, Point2D line1terminal, Point2D line2initial, Point2D line2terminal) {
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

    public void draw(GraphicsContext gc) {
        gc.setStroke(Color.BLUE);
        gc.setFill(Color.BLUE);

        // Initial point
        gc.fillOval(startPoint.getX(), startPoint.getY(), 3, 3);

        // Hit point
        if (hitPoint != null) gc.fillOval(hitPoint.getX(), hitPoint.getY(), 5, 5);

        // Raycast line
        if (cast != null) gc.strokeLine(startPoint.getX(), startPoint.getY(), cast.getX(), cast.getY());

        // Target line segment
        if (point1 != null && point2 != null) {
            gc.strokeLine(point1.getX(), point1.getY(), point2.getX(), point2.getY());
        }
    }
}
