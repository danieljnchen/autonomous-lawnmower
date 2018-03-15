package algorithm;

import javafx.geometry.Point2D;

import java.util.ArrayList;

import static application.view.ViewerController.boundary;
import static application.view.ViewerController.robot;

public class Algorithm {

    private ArrayList<Point2D> pathNodes = new ArrayList<>();

    public Algorithm() {
        pathNodes.add(Point2D.ZERO);
    }

    public void addPathToRobot() {
        robot.queueNodes(pathNodes);
        pathNodes.clear();
        pathNodes.add(Point2D.ZERO);
    }

    public void raycastIterative(Point2D startPoint, double angle, boolean side) {
        try {
            // Raycast to the left and right of the current point
            Raycast right = new Raycast(startPoint, angle + 90, boundary.getOuterBound(), false);
            Raycast left = new Raycast(startPoint, angle - 90, boundary.getOuterBound(), false);

            // Alternate so the path is zigzag
            if (side) {
                toPoint(right.getHitPoint(right.getNumHits() - 1));
                toPoint(left.getHitPoint(left.getNumHits() - 1));
            } else {
                toPoint(left.getHitPoint(left.getNumHits() - 1));
                toPoint(right.getHitPoint(right.getNumHits() - 1));
            }

            // Determine where to start the next iteration
            Point2D nextStart = startPoint;
            Point2D distBetween = right.getHitPoint().subtract(left.getHitPoint());
            double maxDistance = 0;
            // Gets the max distance over 100 intervals
            for (int i = 1; i < 100; ++i) {
                try {
                    Point2D testPoint = left.getHitPoint().add(distBetween.multiply((double) i / 100));
                    Raycast next = new Raycast(testPoint, angle, boundary.getOuterBound(), false);

                    if (next.getNumHits() % 2 == 0) continue;

                    double newDistance = next.getHitPoint().distance(testPoint);
                    if (newDistance > maxDistance) {
                        maxDistance = newDistance;
                        nextStart = testPoint;
                    }
                } catch (NoHitException ignored) {}
            }

            try {
                new Raycast(nextStart, angle, true);
            } catch (NoHitException ignored) {}

            Point2D distanceNext = new Point2D(robot.width * Math.cos(Math.toRadians(angle)), robot.width * Math.sin(Math.toRadians(angle)));
            raycastIterative(nextStart.add(distanceNext), angle, !side);
        } catch (NoHitException e) {
            System.out.println("raycastIterative reached end");
        }
    }

    public void followBoundary(ArrayList<Point2D> bound) {
        pathNodes.addAll(bound);
        pathNodes.add(bound.get(0));
    }

    public void followBoundary(ArrayList<Point2D> bound, int indexStart, int indexStop) {
        if (indexStart < 0 || indexStart >= bound.size()
                || indexStop < 0 || indexStop >= bound.size()) {
            throw new IndexOutOfBoundsException("followBoundary index out of bounds ("
                    + indexStart + ", "  + indexStop + ", max=" + (bound.size()-1) + ")");
        }

        double distanceInc = 0;
        double distanceDec = 0;

        // Incrementing
        for (int i = indexStart; i != modulus(indexStop - 1, bound.size()); i = modulus(i + 1, bound.size())) {
            distanceInc += bound.get(i).distance(bound.get(modulus(i + 1, bound.size())));
        }

        // Decrementing
        for (int i = indexStart; i != modulus(indexStop + 1, bound.size()); i = modulus(i - 1, bound.size())) {
            distanceDec += bound.get(i).distance(bound.get(modulus(i - 1, bound.size())));
        }

        if (distanceInc < distanceDec) {
            for (int i = indexStart; i != indexStop; i = modulus(i + 1, bound.size())) {
                pathNodes.add(bound.get(i));
            }
        } else {
            for (int i = indexStart; i != indexStop; i = modulus(i - 1, bound.size())) {
                pathNodes.add(bound.get(i));
            }
        }
        pathNodes.add(bound.get(indexStop));
    }

    public void toPoint(Point2D end) {
        assert pathNodes.size() != 0;

        // Always start at the last path node
        Point2D start = pathNodes.get(pathNodes.size() - 1);

        Point2D delta = end.subtract(start);
        double angle = Math.atan2(delta.getY(), delta.getX());

        try {
            // Raycast directly to the end point
            Raycast direct = new Raycast(start, Math.toDegrees(angle), false);

            // Find the hit point that corresponds with the point we are going to
            int endIndex = -1;
            for (int i = 0; i < direct.getNumHits(); i++) {
                if (direct.getHitPoint(i).distance(end) < 0.01) {
                    endIndex = i;
                }
            }

            // If the end point does not lie on a boundary, go to the end
            if (endIndex == -1) {
                pathNodes.add(end);
                return;
            }

            // Go around each pair of hitpoints
            for (int i = 0; i < endIndex; i += 2) {
                pathNodes.add(direct.getHitPoint(i));

                // Go around the boundary of this hitpoint pair
                followBoundary(boundary.bounds.get(direct.getHitPointBoundary(i)), direct.getHitPointSegment(i)[1], direct.getHitPointSegment(i + 1)[0]);

                pathNodes.add(direct.getHitPoint(i + 1));
            }

        } catch (NoHitException e) {
            // Raycast went outside of boundary, go to the end
        }

        pathNodes.add(end);
    }

    private static int modulus(int a, int n) {
        return ((a % n) + n) % n;
    }
}
