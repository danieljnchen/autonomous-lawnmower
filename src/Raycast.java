import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Raycast extends UIObject {

    public final Point2D startPoint;
    private final double angle;
    private ArrayList<ArrayList<Point2D>> bounds = new ArrayList<>();

    private ArrayList<RaycastObject> hitPointInfo = new ArrayList<>();
    private boolean render;

    /***
     * Raycasts a ray at the specified angle. Contains the point the ray intersects with a boundary.
     * @param startPoint point to start raycast
     * @param angle in degrees
     */
    Raycast(Point2D startPoint, double angle, boolean render) throws NoHitException {
        this.startPoint = startPoint;
        this.angle = angle;
        this.bounds = Main.boundary.bounds;
        this.render = render;

        start(startPoint, angle);
    }

    Raycast(Point2D startPoint, double angle, ArrayList<Point2D> bound, boolean render) throws NoHitException {
        this.startPoint = startPoint;
        this.angle = angle;
        this.bounds.add(bound);
        this.render = render;

        start(startPoint, angle);
    }

    private void start(Point2D startPoint, double angle) throws NoHitException {

        for (int i = 0; i < bounds.size(); i++) {
            for (int j = 0; j < bounds.get(i).size(); j++) {
                Point2D point1 = bounds.get(i).get(j);
                Point2D point2 = bounds.get(i).get((j + 1) % bounds.get(i).size());

                Point2D hitPoint = intersection(startPoint, startPoint.add(5000 * Math.cos(Math.toRadians(angle)), 5000 * Math.sin(Math.toRadians(angle))), point1, point2);

                if (hitPoint != null) {
                    hitPointInfo.add(new RaycastObject(hitPoint, point1, j, point2, (j+1)%bounds.get(i).size(), i));
                }
            }
        }

        if (hitPointInfo.size() != 0) {
            hitPointInfo.sort((raycastObject1, raycastObject2) -> {
                double distance1 = raycastObject1.getHitPoint().distance(startPoint);
                double distance2 = raycastObject2.getHitPoint().distance(startPoint);
                return (int) (distance1 - distance2);
            });
        } else {
            throw new NoHitException(this + " did not hit a target");
        }
    }

    private static Point2D intersection(Point2D line1initial, Point2D line1terminal, Point2D line2initial, Point2D line2terminal) {
        Point2D vector1 = new Point2D(line1terminal.getX() - line1initial.getX(), line1terminal.getY() - line1initial.getY());
        Point2D vector2 = new Point2D(line2terminal.getX() - line2initial.getX(), line2terminal.getY() - line2initial.getY());

        double scalar1, scalar2;
        Point2D out;

        if ((Math.abs(vector1.getX()) < .0001) && (Math.abs(vector2.getY()) < .0001)) {
            out = new Point2D(line1initial.getX(), line2initial.getY());
        } else if (Math.abs(vector1.getX()) < .0001) {
            out = new Point2D(line1initial.getX(), line2initial.getY() + (line1initial.getX() - line2initial.getX()) / vector2.getX() * vector2.getY());
        } else if (Math.abs(vector2.getY()) < .0001) {
            out = new Point2D(line1initial.getX() + (line2initial.getY() - line1initial.getY()) / vector1.getY() * vector1.getX(), line2initial.getY());
        } else {
            scalar2 = (vector1.getY() / vector1.getX() * (line2initial.getX() - line1initial.getX()) - (line2initial.getY() - line1initial.getY()))
                    / (vector2.getY() * (1 - (vector1.getY() * vector2.getX()) / (vector1.getX() * vector2.getY())));
            scalar1 = (line2initial.getX() - line1initial.getX() + scalar2 * vector2.getX()) / vector1.getX();
            out = new Point2D(line1initial.getX() + vector1.getX() * scalar1, line1initial.getY() + vector1.getY() * scalar1);
        }

        if (lineContains(line1initial, line1terminal, out) && lineContains(line2initial, line2terminal, out)) {
            return out;
        } else {
            return null;
        }
    }

    public static boolean lineContains(Point2D lineInitial, Point2D lineTerminal, Point2D point) {
        Point2D vectorLine = lineTerminal.subtract(lineInitial);
        Point2D vectorPoint = point.subtract(lineInitial);

        if (Math.abs(vectorLine.angle(vectorPoint)) < .0001) {
            return vectorLine.magnitude() >= vectorPoint.magnitude();
        }

        return false;
    }

    public int getNumHits() {
        return hitPointInfo.size();
    }

    /**
     * Returns the hit point the index points to. For example, index 0 will retrieve the first hit point
     * @return
     */

    public Point2D getHitPoint() {
        return getHitPoint(0);
    }

    public Point2D getHitPoint(int hitIndex) {
        return hitPointInfo.get(hitIndex).getHitPoint();
    }

    public int[] getHitPointSegment() {
        return getHitPointSegment(0);
    }

    public int[] getHitPointSegment(int hitIndex) {
        return new int[] {
                hitPointInfo.get(hitIndex).getSegmentPoint1Index(),
                hitPointInfo.get(hitIndex).getSegmentPoint2Index()
        };
    }

    public int getHitPointBoundary() {
        return getHitPointBoundary(0);
    }

    public int getHitPointBoundary(int hitIndex) {
        return hitPointInfo.get(hitIndex).getHitPointBound();
    }

    public void draw(GraphicsContext gc) {
        if(render) {
            gc.setStroke(Color.BLUE);
            gc.setFill(Color.DARKBLUE);
            gc.setLineWidth(1);

            if (hitPointInfo.size() != 0) {
                RaycastObject raycastObject = hitPointInfo.get(0);
                // Target line segment
                gc.strokeLine(raycastObject.getSegmentPoint1().getX(), raycastObject.getSegmentPoint1().getY(), raycastObject.getSegmentPoint2().getX(), raycastObject.getSegmentPoint2().getY());

                // Hit point
                gc.fillOval(getHitPoint().getX() - 2, getHitPoint().getY() - 2, 4, 4);

                // Raycast line
                gc.strokeLine(startPoint.getX(), startPoint.getY(), raycastObject.getHitPoint().getX(), raycastObject.getHitPoint().getY());
            } else {
                //gc.strokeLine(startPoint.getX(), startPoint.getY(), startPoint.getX() + 5000 * Math.cos(Math.toRadians(angle)), startPoint.getY() + 5000 * Math.sin(Math.toRadians(angle)));
            }
        }
    }

    @Override
    public String toString() {
        //return "Raycast [" + "startPoint = " + startPoint + ", angle = " + angle + "]";
        return "";
    }
}

class NoHitException extends Exception {
    NoHitException(String message) {
        super(message);
    }
}
