import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class Algorithm {
    Robot robot;
    Boundary boundary;

    Algorithm(Robot robot, Boundary boundary) {
        this.robot = robot;
        this.boundary = boundary;
    }

    void generatePath() {
        perimeterSweep(boundary.outerBound);

        for (Rectangle2D rect : subDivideIntoRects()) {
            generateZigZag(rect);
        }
    }

    void perimeterSweep(ArrayList<Point2D> perimeter) {
        robot.pathNodes.addAll(perimeter);
        robot.pathNodes.add(perimeter.get(0));
    }

    ArrayList<Rectangle2D> subDivideIntoRects() {
        ArrayList<Rectangle2D> subRects = new ArrayList<>();

        double maxH = getMaxHeight();
        // Track how far we have subdivided
        double curY = 0;

        // Repeat until area is fully subdivided
        while (curY < maxH) {
            // Dimensions of new rect
            double curX = 0;
            double curW = 0;
            double curH = 0;

            // Step 1: Find rect length at the current Y
            for (Point2D point : boundary.outerBound) {
                if (point.getX() != curW && point.getY() == curY) {
                    curW = point.getX();
                }
            }

            // Step 2: Find max rect width based on length
            for (Point2D point : boundary.outerBound) {
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

            if (curH == 0) {
                break;
            }
        }

        return subRects;
    }

    private void generateZigZag(Rectangle2D rect) {
        // Zig zag through defined rectangle based on robot length
        int i = 0;
        while (i < rect.getHeight() / robot.length) {
            robot.pathNodes.add(new Point2D.Double(rect.getX(), rect.getY() + robot.length * i));
            robot.pathNodes.add(new Point2D.Double(rect.getWidth(), rect.getY() + robot.length * i));
            robot.pathNodes.add(new Point2D.Double(rect.getWidth(), rect.getY() + robot.length * (i+1)));
            robot.pathNodes.add(new Point2D.Double(rect.getX(), rect.getY() + robot.length * (i+1)));
            robot.pathNodes.add(new Point2D.Double(rect.getX(), rect.getY() + robot.length * (i+2)));

            i+=2;
        }
    }

    void raycastZigZag(Point2D start, double angle, Boundary boundary) {
        double height = 0;

        // Start a new raycast in the specified direction
        Raycast r = new Raycast(start, angle, boundary);
        Point2D hit = r.hit();

        while (height < getMaxHeight()) {
            height += robot.length;
        }

    }

    private double getMaxHeight() {
        double maxH = 0;

        // Find max width of outer boundary
        for (Point2D point : boundary.outerBound) {
            if (point.getY() > maxH) {
                // If so, our max width is at that point
                maxH = point.getY();
            }
        }

        return maxH;
    }
}
