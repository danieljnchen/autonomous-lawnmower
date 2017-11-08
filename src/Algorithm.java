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
        double lastW = 0;
        double lastH = 0;

        // Find max height of outer boundary
        for (Point2D point : outerBoundary) {
            if (point.getY() > maxH) {
                // If so, our max height is at that point
                maxH = point.getY();
            }
        }

        // Repeat until area is fully subdivided
        while (lastH < maxH) {
            double curW = 0;
            double curH = 0;

            // Step 1: Find max rect width
            for (Point2D point : outerBoundary) {
                // Check if the X component is bigger
                if (point.getX() > curW && point.getY() == lastH) {
                    curW = point.getX();
                }
            }

            // Step 2: Find max rect height based on width
            for (Point2D point : outerBoundary) {
                if (point.getY() > curH && point.getX() == curW) {
                    curH = point.getY();
                }
            }

            // TODO: set rect coords

            // TODO: respect inner boundaries
            // Step 3: Get the left-most point of all of the boundaries; this will be the divider
            /*for (ArrayList<Point2D> boundary : innerBoundaries) {
                for (int i = 0; i < boundary.size(); i++) {
                    if (boundary.get(curHeight).getX() < nearestPoint) {
                        nearestPoint = boundary.get(curHeight).getX();
                    }
                }
            }*/

            // Add the new subdivision to the list
            subRects.add(new Rectangle2D.Double(0, lastH, curW, curH));
            System.out.println("Added rect: " + curW + ", " + curH);

            lastW = curW;
            lastH = curH;
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
