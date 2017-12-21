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

    public void generatePath() {
        aroundBound(boundary.getOuterBound());
        raycastComb(new Point2D(200, 300), -91);
    }

    public void raycastComb(Point2D startPoint, double angle) {
        double searchAngle = angle + 90;

        Raycast cast;
        Point2D currentPoint = new Point2D(startPoint.getX(), startPoint.getY());

        try {
            cast = new Raycast(startPoint, searchAngle);
        } catch (NoHitException e) {
            e.printStackTrace();
            return;
        }

        do {
            Raycast left;
            Raycast right;
            try {
                left = new Raycast(currentPoint, searchAngle + 90);
                right = new Raycast(currentPoint, searchAngle - 90);
            } catch (NoHitException e) {
                e.printStackTrace();
            }

            System.out.println(currentPoint.distance(cast.getHitPoint()));
            currentPoint = currentPoint.add(robot.width * Math.cos(Math.toRadians(searchAngle)), robot.width * Math.sin(Math.toRadians(searchAngle)));
        } while (currentPoint.distance(cast.getHitPoint()) > robot.length);
    }

    private void aroundBound(ArrayList<Point2D> bound) {
        robot.pathNodes.addAll(bound);
        robot.pathNodes.add(bound.get(0));
    }
}
