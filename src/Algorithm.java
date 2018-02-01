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
        Point2D distanceNext = new Point2D(robot.width * Math.cos(Math.toRadians(angle)), robot.width * Math.sin(Math.toRadians(angle)));

        try {
            Raycast right = new Raycast(startPoint, angle + 90, Main.boundary.getOuterBound()); //raycast to the left and the right
            Raycast left = new Raycast(startPoint, angle - 90, Main.boundary.getOuterBound());

            if (side) { //alternate so robot follows a zigzag path
                toPoint(right.getHitPoint(right.getNumHits()-1));
                toPoint(left.getHitPoint(left.getNumHits()-1));
            } else {
                toPoint(left.getHitPoint(left.getNumHits()-1));
                toPoint(right.getHitPoint(right.getNumHits()-1));
            }

            Raycast next = new Raycast(startPoint, angle, Main.boundary.getOuterBound());
            if (next.getHitPoint().distance(startPoint) <= robot.width) return;

            raycastIterative(right.getHitPoint().midpoint(left.getHitPoint()).add(distanceNext), angle, !side);
        } catch (NoHitException e) {
            //e.printStackTrace();
            System.out.println("NoHitException");
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
        if (robot.pathNodes.size() == 0) robot.pathNodes.add(Point2D.ZERO);

        // Always start at the last path node
        Point2D start = robot.pathNodes.get(robot.pathNodes.size() - 1);

        try {
            Point2D delta = end.subtract(start);
            double angle = Math.atan2(delta.getY(), delta.getX());

            Raycast direct = new Raycast(start, angle);

        } catch (NoHitException e) {
            // If we haven't hit anything, just go straight to the end point
        }

    }
}
