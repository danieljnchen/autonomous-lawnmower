import javafx.scene.paint.Color;

import java.awt.geom.Point2D;

public class Raycast extends UIObject {
    private Point2D startPoint;
    private Point2D hitPoint;

    private Point2D cast;

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
    private void start(Point2D start, double angle, Boundary boundary) {
        startPoint = start;

        // Create a new point for us to manipulate
        cast = new Point2D.Double(start.getX(), start.getY());

        double speedCoef = 0.5;

        double prevDist1 = 1;
        double prevDist2 = 1;
        double distDelta1 = -1;
        double distDelta2 = -1;

        int index = 0;
        for(int index1 = 1; index1 < boundary.outerBound.size(); ++index1) {
            if (Math.abs(Math.atan2(boundary.outerBound.get(index1).getY() - cast.getY(),
                    boundary.outerBound.get(index1).getX() - cast.getX()) - angle)
                    <
                    Math.abs(Math.atan2(boundary.outerBound.get(index).getY() - cast.getY(),
                            boundary.outerBound.get(index).getX() - cast.getY()) - angle)) {
                index = index1;
            }
        }
        point1 = boundary.outerBound.get(index);
        if(Math.abs(Math.atan2(boundary.outerBound.get(index-1).getY() - cast.getY(),
                boundary.outerBound.get(index-1).getX() - cast.getY()) - angle)
        <
        Math.abs(Math.atan2(boundary.outerBound.get(index+1).getY() - cast.getY(),
                boundary.outerBound.get(index+1).getX() - cast.getY()) - angle)) {
            point2 = boundary.outerBound.get(index-1);
        } else {
            point2 = boundary.outerBound.get(index+1);
        }
        /*
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
        */

        // Iterate the point forwards in the specified direction
        while (true) {
            double angle1 = Math.atan2(point1.getY() - cast.getY(), point1.getX() - cast.getX());
            double angle2 = Math.atan2(point2.getY() - cast.getY(), point2.getX() - cast.getX());

            // Check if the vector angles of the two points are opposite each other
            if (angle1 == -angle2) break;

            cast.setLocation(cast.getX() + Math.cos(angle) * speedCoef, cast.getY() + Math.sin(angle) * speedCoef);
        }

        hitPoint = cast;
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

        // Raycast line
        gc.strokeLine(startPoint.getX(), startPoint.getY(), cast.getX(), cast.getY());
    }
}
