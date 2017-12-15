import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;

import java.util.ArrayList;

public class Algorithm {
    private Robot robot;
    private Boundary boundary;

    Algorithm(Robot robot, Boundary boundary) {
        this.robot = robot;
        this.boundary = boundary;
    }

    void generatePath() {
        boundarySweep(boundary.getOuterBound());

        raycastZigZag(0);
    }

    void boundarySweep(ArrayList<Point2D> perimeter) {
        robot.pathNodes.addAll(perimeter);
        robot.pathNodes.add(perimeter.get(0));
    }

    ArrayList<Rectangle2D> subDivideIntoRects() {
        ArrayList<Rectangle2D> subRects = new ArrayList<>();

        double maxH = getMaxLength(0);
        // Track how far we have subdivided
        double curY = 0;

        // Repeat until area is fully subdivided
        while (curY < maxH) {
            // Dimensions of new rect
            double curX = 0;
            double curW = 0;
            double curH = 0;

            // Step 1: Find rect length at the current Y
            for (Point2D point : boundary.getOuterBound()) {
                if (point.getX() != curW && point.getY() == curY) {
                    curW = point.getX();
                }
            }

            // Step 2: Find max rect width based on length
            for (Point2D point : boundary.getOuterBound()) {
                if (point.getY() - curY > curH && point.getX() == curW) {
                    curH = point.getY() - curY;
                    break;
                }
            }

            // TODO: respect inner boundaries

            // Add the new subdivision to the list
            Rectangle2D rect = new Rectangle2D(curX, curY, curW, curH);
            subRects.add(rect);
            System.out.println(rect.toString());

            curY += curH;

            if (curH == 0) {
                break;
            }
        }

        return subRects;
    }

    private void rectZigZag(Rectangle2D rect) {
        // Zig zag through defined rectangle based on robot length
        int i = 0;
        while (i < rect.getHeight() / robot.length) {
            robot.pathNodes.add(new Point2D(rect.getMinX(), rect.getMinY() + robot.length * i));
            robot.pathNodes.add(new Point2D(rect.getWidth(), rect.getMinY() + robot.length * i));
            robot.pathNodes.add(new Point2D(rect.getWidth(), rect.getMinY() + robot.length * (i+1)));
            robot.pathNodes.add(new Point2D(rect.getMinX(), rect.getMinY() + robot.length * (i+1)));
            robot.pathNodes.add(new Point2D(rect.getMinX(), rect.getMinY() + robot.length * (i+2)));

            i+=2;
        }
    }

    private void raycastZigZag(double angle) {
        double lengthCovered = 0;
        Point2D curPoint = boundary.getOuterBound().get(0);

        while (lengthCovered < getMaxLength(0)) {
            Raycast ray1 = new Raycast(curPoint, angle);
            Raycast ray2 = new Raycast(ray1.getHitPoint(), angle + 180);

            curPoint = new Point2D(curPoint.getX(), curPoint.getY() + robot.length);
            lengthCovered += robot.length;
        }

    }

    /**
     * Finds the greatest distance from one side of a boundary to another
     * @param angle In degrees
     * @return Distance
     */
    private double getMaxLength(double angle) {
        double maxLength = 0;
        angle = Math.toRadians(angle);
        Point2D startPoint = Point2D.ZERO;

        // Raycast to determine distance
        Raycast cast = new Raycast(startPoint, angle);
        if (cast.getHitPoint() == null) return -1;

        Point2D currentPoint = new Point2D(startPoint.getX(), startPoint.getY());

        while (currentPoint.distance(cast.getHitPoint()) > robot.length) {
            // Raycast to the left and right to find our max length
            Raycast left = new Raycast(currentPoint, angle - Math.PI/2);
            Raycast right = new Raycast(currentPoint, angle + Math.PI/2);

            if (left.getHitPoint().distance(right.getHitPoint()) > maxLength) {
                maxLength = left.getHitPoint().distance(right.getHitPoint());
            }

            currentPoint = new Point2D(currentPoint.getX() + Math.cos(angle), currentPoint.getY() + Math.sin(angle));
        }

        return maxLength;
    }
}
