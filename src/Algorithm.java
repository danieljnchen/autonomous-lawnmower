import java.awt.geom.Point2D;

public class Algorithm {
    public static Point2D[] boundaryPoints;

    public static Point2D[] makeRect(Point2D p1, Point2D p2) {
        Point2D[] points = new Point2D[4];
        points[0] = new Point2D.Double(p1.getX(), p1.getY());
        points[1] = new Point2D.Double(p2.getX(), p1.getY());
        points[2] = new Point2D.Double(p2.getX(), p2.getY());
        points[3] = new Point2D.Double(p1.getX(), p2.getY());

        return points;
    }
}
