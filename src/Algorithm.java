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
            Raycast right = new Raycast(startPoint, angle + 90, Main.boundary.getOuterBound(), false); //raycast to the left and the right
            System.out.println("Right");
            Raycast left = new Raycast(startPoint, angle - 90, Main.boundary.getOuterBound(), false);
            System.out.println("Left");

            if (side) { //alternate so robot follows a zigzag path
                robot.pathNodes.add(right.getHitPoint(right.getNumHits() - 1));
                toPoint(left.getHitPoint(left.getNumHits() - 1));
            } else {
                robot.pathNodes.add(left.getHitPoint(left.getNumHits() - 1));
                toPoint(right.getHitPoint(right.getNumHits() - 1));
            }

            Raycast next;
            Point2D nextStartPoint = startPoint;
            Point2D distBetween = right.getHitPoint().subtract(left.getHitPoint());
            double maxDistance = 0;

            for (int i = 0; i <= 100; ++i) {
                try {
                    Point2D nextStartPointTest = left.getHitPoint().add(distBetween.multiply((double) i / 100));
                    next = new Raycast(nextStartPointTest, angle, Main.boundary.getOuterBound(), false);
                    if(next.getNumHits() % 2 == 0) {
                        continue;
                    }
                    double newDistance = next.getHitPoint().distance(nextStartPointTest);
                    if (newDistance > maxDistance) {
                        maxDistance = newDistance;
                        nextStartPoint = nextStartPointTest;
                    }
                } catch (NoHitException e) {
                }
            }

            try {
                next = new Raycast(nextStartPoint, angle, true);
            } catch (NoHitException e) {
            }

            raycastIterative(nextStartPoint.add(distanceNext), angle, !side);
        } catch (NoHitException e) {
            System.out.println("NoHitException");
        }
    }

    public void aroundBoundary(ArrayList<Point2D> bound) {
        robot.pathNodes.addAll(bound);
        robot.pathNodes.add(bound.get(0));
    }

    public void followBoundary(ArrayList<Point2D> bound, int indexStart, int indexStop) {
        if (indexStart < indexStop) {
            for (int i = indexStart; i < indexStop; i++) {
                robot.pathNodes.add(bound.get(i));
            }
        } else {
            for (int i = indexStart; i > indexStop; i--) {
                robot.pathNodes.add(bound.get(i));
            }
        }
    }

    public void toPoint(Point2D end) {
        if (robot.pathNodes.size() == 0) robot.pathNodes.add(Point2D.ZERO);

        // Always start at the last path node
        Point2D start = robot.pathNodes.get(robot.pathNodes.size() - 1);

        try {
            Point2D delta = end.subtract(start);
            double angle = Math.atan2(delta.getY(), delta.getX());

            Raycast direct = new Raycast(start, Math.toDegrees(angle), false);

            if (direct.getNumHits() > 1) {
                robot.pathNodes.add(direct.getHitPoint());

                // Go around boundary
                followBoundary(boundary.bounds.get(direct.getHitPointBoundary()), direct.getHitPointSegment(0)[1], direct.getHitPointSegment(1)[0]);

                robot.pathNodes.add(direct.getHitPoint(1));
            }
        } catch (NoHitException e) {
            // If we haven't hit anything, just go straight to the end point
            System.out.println("NoHit");
        }

        robot.pathNodes.add(end);
    }
}
