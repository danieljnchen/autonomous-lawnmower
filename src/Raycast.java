import javafx.scene.paint.Color;

import java.awt.geom.Point2D;

public class Raycast extends UIObject {
    private Point2D startPoint;
    private Point2D hitPoint;

    private Point2D point1;
    private Point2D point2;

    public Raycast(Point2D start, double angle) {
        // Assume we want the main boundary
        start(start, angle, Main.boundary);
    }

    public Raycast(Point2D start, double angle, Boundary boundary) {
        start(start, angle, boundary);
    }

    /***
     * Start a raycast in the specified direction. Angle is in degrees.
     * @param start
     * @param angle
     * @param boundary
     */
    public void start(Point2D start, double angle, Boundary boundary) {
        startPoint = start;

        // Create a new point for us to manipulate
        Point2D cast = new Point2D.Double(start.getX(), start.getY());

        double speedCoef = 0.5;

        double prevDist1 = 1;
        double prevDist2 = 1;
        double distDelta1 = -1;
        double distDelta2 = -1;

        // Search for two points closest to a specified direction
        double curAngle = 90; // start at 90 degrees; rotate left/right depending on raycast direction
        if (angle > 90) {

        }

        // First point
        for (Point2D point : boundary.outerBound) {
            if (point.distance(cast) < curAngle) {
                curAngle = point.distance(cast);
                point1 = point;
            }
        }

        curAngle = cast.distance(point2);

        // Second point
        for (Point2D point : boundary.outerBound) {
            if (point.distance(cast) < curAngle && point != point1) {
                curAngle = point.distance(cast);
                point2 = point;
            }
        }

        // Iterate the point forwards in the specified direction
        while (distDelta1 < 0 && distDelta2 < 0) {
            distDelta1 = cast.distance(point1) - prevDist1;
            distDelta2 = cast.distance(point2) - prevDist2;

            cast.setLocation(cast.getX() + Math.cos(angle) * speedCoef, cast.getY() + Math.sin(angle) * speedCoef);
        }
    }

    public Point2D hit() {
        // If raycast check is getting further from all points, stop; we're not going to hit anything
        if (false) System.out.println("Error: raycast failed to hit a target");
        return hitPoint;
    }

    public void draw() {
        gc.setStroke(Color.BLUE);
        gc.setFill(Color.BLUE);

        // Initial raycast point
        gc.fillOval(startPoint.getX(), startPoint.getY(), 5, 5);

        // Target line segment
        gc.strokeLine(point1.getX(), point1.getY(), point2.getX(), point2.getY());
    }
}
