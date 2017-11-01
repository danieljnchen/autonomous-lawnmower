import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Algorithm {
    public static ArrayList<Point2D> boundaryPoints = new ArrayList<>();

    public static ArrayList<Point2D> makeRect(Point2D p1, Point2D p2) {
        ArrayList<Point2D> points = new ArrayList<>();

        points.add(new Point2D.Double(p1.getX(), p1.getY()));
        points.add(new Point2D.Double(p2.getX(), p1.getY()));
        points.add(new Point2D.Double(p2.getX(), p2.getY()));
        points.add(new Point2D.Double(p1.getX(), p2.getY()));

        return points;
    }
}
