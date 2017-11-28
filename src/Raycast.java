import java.awt.geom.Point2D;

public class Raycast extends UIObject {
    private Point2D hitPoint;

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
        // Create a new point for us to manipulate
        Point2D cast = new Point2D.Double(start.getX(), start.getY());
        angle = Math.toRadians(angle);
        double speedCoef = 0.5;

        int dx = 1;
        int dy = 1;

        // Iterate the point forwards in the specified direction
        while (Math.abs(dx) > 0 && Math.abs(dy) > 0) {
            cast.setLocation(cast.getX() + Math.cos(angle) * speedCoef, cast.getY() + Math.sin(angle) * speedCoef);
        }
    }

    public Point2D hit() {
        // If raycast check is getting further from all points, stop; we're not going to hit anything
        if (false) System.out.println("Error: raycast failed to hit a target");
        return hitPoint;
    }

    private void draw() {
    }
}
