import javafx.geometry.Point2D;

import java.util.ArrayList;

public class Algorithm {
    private Robot robot;
    private Boundary boundary;

    Algorithm(Robot robot, Boundary boundary) {
        this.robot = robot;
        this.boundary = boundary;
    }

    public void raycastIterative(Point2D startPoint, double angle, boolean side) {
        Raycast cast;
        Raycast right, left;
        Point2D distanceNext = new Point2D(robot.width * Math.cos(Math.toRadians(angle)), robot.width * Math.sin(Math.toRadians(angle)));

        try {
            right = new Raycast(startPoint, angle + 90, Main.boundary.getOuterBound()); //raycast to the left and the right
            left = new Raycast(startPoint, angle - 90, Main.boundary.getOuterBound());

            if (side) { //alternate so robot follows a zigzag path
                robot.pathNodes.add(right.getHitPoint());
                robot.pathNodes.add(left.getHitPoint());
            } else {
                robot.pathNodes.add(left.getHitPoint());
                robot.pathNodes.add(right.getHitPoint());
            }

            cast = new Raycast(startPoint, angle, Main.boundary.getOuterBound());
            /*if (cast.getHitPoint().distance(startPoint) <= robot.width) {
                distanceNext = distanceNext.multiply(cast.getHitPoint().distance(startPoint));
            }*/

            raycastIterative(right.getHitPoint().midpoint(left.getHitPoint()).add(distanceNext), angle, !side);
        } catch (NoHitException e) {
            e.printStackTrace();
        }
    }

    /*public double raycastComb(Point2D startPoint, double angle) {

        Point2D currentPoint = new Point2D(startPoint.getX(), startPoint.getY());
        Raycast cast;
        boolean side = false;

        try {
            cast = new Raycast(startPoint, angle);
        } catch (NoHitException e) {
            e.printStackTrace();
            return -1;
        }

        double maxLength = 0;
        do {
            Raycast right;
            Raycast left;

            try {
                right = new Raycast(currentPoint, angle + 90);
                left = new Raycast(currentPoint, angle - 90);
                if(side) {
                    robot.pathNodes.add(right.getHitPoint());
                    robot.pathNodes.add(left.getHitPoint());
                } else {
                    robot.pathNodes.add(left.getHitPoint());
                    robot.pathNodes.add(right.getHitPoint());
                }
                side = !side;
            } catch (NoHitException e) {
                System.out.println("Outside boundary, stopping");
                break;
            }

            currentPoint = currentPoint.add(Math.cos(Math.toRadians(angle)) * robot.width * 2, Math.sin(Math.toRadians(angle)) * robot.width * 2);

            if (right.getHitPoint().subtract(currentPoint).magnitude() + left.getHitPoint().subtract(currentPoint).magnitude() > maxLength) {
                maxLength = right.getHitPoint().subtract(currentPoint).magnitude() + left.getHitPoint().subtract(currentPoint).magnitude();
            }
        } while (Raycast.lineContains(startPoint, cast.getHitPoint(), currentPoint));
        System.out.println("Max Length: " + maxLength);
        return maxLength;
    }*/

    public void followBoundary(ArrayList<Point2D> bound) {
        robot.pathNodes.addAll(bound);
        robot.pathNodes.add(bound.get(0));
    }

    public void followBoundary(ArrayList<Point2D> bound, int indexStart, int indexStop) {
        for (int i = indexStart; i < indexStop; i++) {
            robot.pathNodes.add(bound.get(i));
        }
    }

    public void toPoint(Point2D end) {
        // Always start at the last path node
        Point2D start = robot.pathNodes.get(robot.pathNodes.size() - 1);

        Point2D delta = end.subtract(start);
        double angle = Math.atan2(delta.getY(), delta.getX());

        try {
            Raycast direct = new Raycast(end, angle);

            Point2D otherSide = direct.getHitPoint(1);
        } catch (NoHitException e) {
            // If we haven't hit anything, just go straight to the end point
            robot.pathNodes.add(end);
        }
    }

    public static boolean crossesPoint(Point2D start, Point2D end, Point2D targetPoint) {
        Point2D delta = end.subtract(start);
        double angle = Math.atan2(delta.getY(), delta.getX());

        try {
            Raycast direct = new Raycast(new Point2D(start.getX(), start.getY()), angle);

            return direct.startPoint.distance(direct.getHitPoint()) >= direct.startPoint.distance(targetPoint);
        } catch (NoHitException e) {
            return true;
        }
    }
}
