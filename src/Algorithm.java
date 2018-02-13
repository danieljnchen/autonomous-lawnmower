import javafx.geometry.Point2D;

import java.util.ArrayList;

public class Algorithm {

    private ArrayList<Point2D> pathNodes = new ArrayList<>();

    public void addPathToRobot() {
        Main.robot.queueNodes(pathNodes);
    }

    public void raycastIterative(Point2D startPoint, double angle, boolean side) {
        Point2D distanceNext = new Point2D(Main.robot.width * Math.cos(Math.toRadians(angle)), Main.robot.width * Math.sin(Math.toRadians(angle)));

        try {
            Raycast right = new Raycast(startPoint, angle + 90, Main.boundary.getOuterBound(), false); //raycast to the left and the right
            System.out.println("Right");
            Raycast left = new Raycast(startPoint, angle - 90, Main.boundary.getOuterBound(), false);
            System.out.println("Left");

            if (side) { //alternate so robot follows a zigzag path
                pathNodes.add(right.getHitPoint(right.getNumHits() - 1));
                toPoint(left.getHitPoint(left.getNumHits() - 1));
            } else {
                pathNodes.add(left.getHitPoint(left.getNumHits() - 1));
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
                    if (next.getNumHits() % 2 == 0) {
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
        pathNodes.addAll(bound);
        pathNodes.add(bound.get(0));
    }

    public void followBoundary(ArrayList<Point2D> bound, int indexStart, int indexStop) {
        if (indexStart < indexStop) {
            double distancePositive = 0;
            double distanceNegative = 0;

            // indexStart to indexStop inner
            for (int i = indexStart; i < indexStop; i++) {
                distancePositive += bound.get(i).distance(bound.get((i + 1) % bound.size()));
            }

            // indexStart to indexStop outer
            for (int i = indexStart; i != indexStop; i = ((i - 1) % bound.size() + bound.size()) % bound.size()) {
                distanceNegative += bound.get(i).distance(bound.get(((i - 1) % bound.size() + bound.size()) % bound.size()));
            }
            System.out.println(distancePositive);
            System.out.println(distanceNegative);

            if (distancePositive < distanceNegative) {
                for (int i = indexStart; i < indexStop; i++) {
                    pathNodes.add(bound.get(i));
                }
            } else {
                for (int i = indexStart; i > indexStop; i--) {
                    pathNodes.add(bound.get(i));
                }
            }
        } else {
            double distancePositive = 0;
            double distanceNegative = 0;

            // indexStart to indexStop inner
            for (int i = indexStart; i > indexStop; i--) {
                distancePositive += bound.get(i).distance(bound.get(((i - 1) % bound.size() + bound.size()) % bound.size()));
            }

            // indexStart to indexStop outer
            for (int i = indexStart; i != indexStop; i = (i + 1) % bound.size()) {
                distanceNegative += bound.get(i).distance(bound.get((i + 1) % bound.size()));
            }
            System.out.println("iStart iStop inner" + distancePositive);
            System.out.println("iStart iStop outer" + distanceNegative);

            if (distancePositive < distanceNegative) {
                for (int i = indexStart; i > indexStop; i--) {
                    pathNodes.add(bound.get(i));
                }
            } else {
                for (int i = indexStart; i > indexStop; i = (i + 1) % bound.size()) {
                    pathNodes.add(bound.get(i));
                }
            }
        }
    }

    public void toPoint(Point2D end) {
        if (pathNodes.size() == 0) pathNodes.add(Point2D.ZERO);

        // Always start at the last path node
        Point2D start = pathNodes.get(pathNodes.size() - 1);

        try {
            Point2D delta = end.subtract(start);
            double angle = Math.atan2(delta.getY(), delta.getX());

            Raycast direct = new Raycast(start, Math.toDegrees(angle), false);

            int endIndex;
            for (endIndex = 0; endIndex < direct.getNumHits(); ++endIndex) {
                if (direct.getHitPoint(endIndex).equals(end)) {
                    break;
                }
            }

            if (endIndex != 0) {
                for (int i = 0; i < endIndex-1; ++i) {
                    pathNodes.add(direct.getHitPoint(i));

                    // Go around boundary
                    followBoundary(Main.boundary.bounds.get(direct.getHitPointBoundary(i)), direct.getHitPointSegment(i)[1], direct.getHitPointSegment(i)[0]);

                    pathNodes.add(direct.getHitPoint(i+1));
                }
            }
        } catch (NoHitException e) {
            // If we haven't hit anything, just go straight to the end point
            pathNodes.add(end);
            System.out.println("NoHit");
        }
    }
}
