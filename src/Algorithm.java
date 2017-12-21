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
        //boundarySweep(boundary.getOuterBound());

        raycastComb(boundary.getOuterBound().get(boundary.getOuterBound().size() - 1).add(10, 0), -90);
    }

    void boundarySweep(ArrayList<Point2D> perimeter) {
        robot.pathNodes.addAll(perimeter);
        robot.pathNodes.add(perimeter.get(0));
    }

    ArrayList<Rectangle2D> subDivideIntoRects() {
        ArrayList<Rectangle2D> subRects = new ArrayList<>();

        double maxH = getMaxLength(Point2D.ZERO, -90);
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

    public void raycastComb(Point2D startPoint, double angle) {
        double searchAngle = angle + 90;
        boolean escaped = false; // whether we've escaped a starting vertex

        // Raycast up and down
        Raycast cast;
        try {
            cast = new Raycast(startPoint, searchAngle);
        } catch (NoHitException e) {
            e.printStackTrace();
            return;
        }
        System.out.println(startPoint);
        System.out.println(cast.getHitPoint());

        Point2D currentPoint = new Point2D(startPoint.getX(), startPoint.getY());
        Point2D lastLeft = currentPoint.add(0, 0);
        Point2D lastRight = currentPoint.add(0, 0);

        while (true) {
            System.out.println(currentPoint.distance(cast.getHitPoint()));
            boolean reachedFinish = currentPoint.distance(cast.getHitPoint()) < robot.length;

            if (reachedFinish) {
                if (escaped) break;
            } else {
                escaped = true;
            }

            Raycast left;
            Raycast right;
            try {
                left = new Raycast(currentPoint, searchAngle + 90);
                right = new Raycast(currentPoint, searchAngle - 90);
            } catch (NoHitException e) {
                e.printStackTrace();
            }

            currentPoint = currentPoint.add(Math.cos(Math.toRadians(searchAngle)), Math.sin(Math.toRadians(searchAngle)));

            // Add the points to the queue
            /*robot.pathNodes.add(lastLeft);
            robot.pathNodes.add(lastRight);
            robot.pathNodes.add(right.getHitPoint());
            robot.pathNodes.add(left.getHitPoint());*/
        }
    }

    /*private void raycastZigZag(Point2D startPoint, double angle) {
        Point2D curPoint = new Point2D(startPoint.getX(), startPoint.getY());
        double maxLength = getMaxLength(curPoint, angle - 90);

        while (curPoint.getY() < maxLength + startPoint.getY()) {
            robot.pathNodes.add(curPoint);

            Raycast ray1 = null;
            try {
                ray1 = new Raycast(curPoint, angle);
                robot.pathNodes.add(ray1.getHitPoint());
            } catch (NoHitException e) {
                e.printStackTrace();
            }

            // Move over for the next cast
            assert ray1 != null;
            curPoint = ray1.getHitPoint().add(robot.length * Math.sin(Math.toRadians(angle)), robot.length * Math.cos(Math.toRadians(angle)));
            robot.pathNodes.add(curPoint);

            // Cast in the opposite direction
            Raycast ray2 = null;
            try {
                ray2 = new Raycast(curPoint, angle + 180);
                robot.pathNodes.add(ray2.getHitPoint());
            } catch (NoHitException e) {
                e.printStackTrace();
            }

            assert ray2 != null;
            curPoint = ray2.getHitPoint().add(robot.length * Math.sin(Math.toRadians(angle)), robot.length * Math.cos(Math.toRadians(angle)));
            robot.pathNodes.add(curPoint);
        }
    }*/

    private double getMaxLength(Point2D startPoint, double angle) {
        double maxLength = 0;
        boolean escaped = false;

        // Raycast to determine distance to search
        Raycast cast;
        try {
            cast = new Raycast(startPoint, angle);
        } catch (NoHitException e) {
            e.printStackTrace();
            return 0;
        }

        Point2D currentPoint = new Point2D(startPoint.getX(), startPoint.getY());

        while (true) {
            boolean reachedFinish = currentPoint.distance(cast.getHitPoint()) < robot.length;

            if (reachedFinish) {
                if (escaped) break;
            } else {
                escaped = true;
            }

            // Raycast to the left and right to find our max length
            Raycast left;
            Raycast right;
            try {
                left = new Raycast(currentPoint, angle + 90);
                right = new Raycast(currentPoint, angle - 90);
            } catch (NoHitException e) {
                e.printStackTrace();
                return 0;
            }

            if (left.getHitPoint().distance(right.getHitPoint()) > maxLength) {
                maxLength = left.getHitPoint().distance(right.getHitPoint());
            }

            currentPoint = currentPoint.add(Math.cos(angle), Math.sin(angle));
        }

        return maxLength;
    }
}
