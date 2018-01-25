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
            if (cast.getHitPoint().distance(startPoint) <= robot.width) {
                distanceNext = distanceNext.multiply(cast.getHitPoint().distance(startPoint));
            }

            raycastIterative(right.getHitPoint().midpoint(left.getHitPoint()).add(distanceNext), angle, !side);
        } catch (NoHitException e) {
            e.printStackTrace();
        }
    }

    public void aroundBoundary(ArrayList<Point2D> bound) {
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
            Raycast direct = new Raycast(start, angle);
            Point2D otherSide = direct.getHitPoint(1);

            followBoundary(
                boundary.bounds.get(direct.getHitPointBoundary()),
                direct.getHitPointSegment()[1],
                direct.getHitPointSegment(1)[0]
            );
            robot.pathNodes.add(otherSide);
        } catch (NoHitException e) {
            // If we haven't hit anything, just go straight to the end point
        }

        robot.pathNodes.add(end);
    }
}
