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
        raycastComb(new Point2D(200, 300), -91);
    }

    public void raycastComb(Point2D startPoint, double angle) {
        double searchAngle = angle + 90;

        Point2D currentPoint = new Point2D(startPoint.getX(), startPoint.getY());
        Raycast cast;

        try {
            cast = new Raycast(startPoint, searchAngle);
        } catch (NoHitException e) {
            e.printStackTrace();
            return;
        }

        while (true) {
            try {
                Raycast left = new Raycast(currentPoint, searchAngle + 90);
                Raycast right = new Raycast(currentPoint, searchAngle - 90);
            } catch (NoHitException e) {
                System.out.println("Reached obstruction, stopping");
                break;
            }

            currentPoint = currentPoint.add(robot.width * Math.cos(Math.toRadians(searchAngle)), robot.width * Math.sin(Math.toRadians(searchAngle)));
        }
    }

    private void aroundBound(ArrayList<Point2D> bound) {
        robot.pathNodes.addAll(bound);
        robot.pathNodes.add(bound.get(0));
    }
}
