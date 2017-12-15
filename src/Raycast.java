import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;


public class Raycast extends UIObject {
    public final Point2D startPoint;
    public ArrayList<Point2D> hitPoints = new ArrayList<>();

    private ArrayList<Point2D> points1 = new ArrayList<>();
    private ArrayList<Point2D> points2 = new ArrayList<>();

    public int index;

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
        for (int index = 0; index <= boundary.bounds.get(0).size(); index++) {
            Point2D point1 = boundary.bounds.get(0).get(index%boundary.bounds.get(0).size());
            Point2D point2 = boundary.bounds.get(0).get((index+1)%boundary.bounds.get(0).size());
            Point2D hitPoint = intersection(startPoint,new Point2D(startPoint.getX()+1000*Math.cos(Math.toRadians(angle)),startPoint.getY()+1000*Math.sin(Math.toRadians(angle))),point1,point2);
            if(hitPoint != null) {
                points1.add(point1);
                points2.add(point2);
                hitPoints.add(hitPoint);
            }
        }
        Point2D hitPoint = hitPoints.get(0);
        for (int i = 1; i < hitPoints.size(); i++) {
            if(startPoint.distance(hitPoints.get(i)) < startPoint.distance(hitPoint)) {
                hitPoint = hitPoints.get(i);
                index = i;
            }
        }
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

        // Initial raycast point
        gc.fillOval(startPoint.getX(),startPoint.getY(),5,5);

        if(hitPoints.size() != 0) {
            Point2D hitPoint = hitPoints.get(0);
            for (int i = 0; i < points1.size(); ++i) {

                // Target line segment
                gc.strokeLine(points1.get(i).getX(), points1.get(i).getY(), points2.get(i).getX(), points2.get(i).getY());
                if (startPoint.distance(hitPoints.get(i)) < startPoint.distance(hitPoint)) {
                    hitPoint = hitPoints.get(i);
                }
                //All hitPoints
                //gc.fillOval(hitPoints.get(i).getX(), hitPoints.get(i).getY(), 5, 5);
            }

            // Hit point
            gc.fillOval(hitPoint.getX(), hitPoint.getY(), 5, 5);

            // Raycast line
            gc.strokeLine(startPoint.getX(),startPoint.getY(),hitPoint.getX(),hitPoint.getY());
        } else {
            System.out.println("Failed to hit a point.");
        }
    }
}
