import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class Algorithm {
    Robot robot;

    ArrayList<Point2D> outerBoundary = new ArrayList<>();
    ArrayList<ArrayList<Point2D>> innerBoundaries = new ArrayList<>();

    Algorithm(Robot robot) {
        this.robot = robot;
    }

    void perimeterSweep(ArrayList<Point2D> perimeter) {
        robot.pathNodes.addAll(perimeter);
        robot.pathNodes.add(perimeter.get(0));
    }

    void generatePath() {
        perimeterSweep(outerBoundary);

        for (Rectangle2D rect : subDivideIntoRects()) {
            generateZigZag(rect);
        }
    }

    ArrayList<Rectangle2D> subDivideIntoRects() {
        ArrayList<Rectangle2D> subRects = new ArrayList<>();

        double maxH = 0;
        // Track how far we have subdivided
        double curY = 0;

        // Find max height of outer boundary
        for (Point2D point : outerBoundary) {
            if (point.getY() > maxH) {
                // If so, our max height is at that point
                maxH = point.getY();
            }
        }

        System.out.println("Max height: " + maxH);

        // Repeat until area is fully subdivided
        while (curY < maxH) {
            // Dimensions of new rect
            double curX = 0;
            double curW = 0;
            double curH = 0;

            // Step 1: Find rect width at the current Y
            for (Point2D point : outerBoundary) {
                if (point.getX() != curW && point.getY() == curY) {
                    curW = point.getX();
                }
            }

            // Step 2: Find max rect height based on width
            for (Point2D point : outerBoundary) {
                if (point.getY() - curY > curH && point.getX() == curW) {
                    curH = point.getY() - curY;
                    break;
                }
            }

            // TODO: respect inner boundaries

            // Add the new subdivision to the list
            Rectangle2D rect = new Rectangle2D.Double(curX, curY, curW, curH);
            subRects.add(rect);
            System.out.println(rect.toString());

            curY += curH;
        }

        return subRects;
    }

    private void generateZigZag(Rectangle2D rect) {
        int i = 0;
        while (i < rect.getHeight() / robot.width) {
            Main.robot.pathNodes.add(new Point2D.Double(rect.getX(), rect.getY() + robot.width * i));
            Main.robot.pathNodes.add(new Point2D.Double(rect.getWidth(), rect.getY() + robot.width * i));
            Main.robot.pathNodes.add(new Point2D.Double(rect.getWidth(), rect.getY() + robot.width * (i+1)));
            Main.robot.pathNodes.add(new Point2D.Double(rect.getX(), rect.getY() + robot.width * (i+1)));
            Main.robot.pathNodes.add(new Point2D.Double(rect.getX(), rect.getY() + robot.width * (i+2)));

            i+=2;
        }
    }

    public ArrayList<Point2D> makeRect(Point2D p1, Point2D p2) {
        ArrayList<Point2D> points = new ArrayList<>();

        points.add(new Point2D.Double(p1.getX(), p1.getY()));
        points.add(new Point2D.Double(p2.getX(), p1.getY()));
        points.add(new Point2D.Double(p2.getX(), p2.getY()));
        points.add(new Point2D.Double(p1.getX(), p2.getY()));

        return points;
    }
}
