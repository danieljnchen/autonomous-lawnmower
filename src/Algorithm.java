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
        raycastComb(new Point2D(200, 300), -45);
    }

    public void raycastComb(Point2D startPoint, double angle) {

        Point2D currentPoint = new Point2D(startPoint.getX(), startPoint.getY());
        Raycast cast;

        try {
            cast = new Raycast(startPoint, angle);
        } catch (NoHitException e) {
            e.printStackTrace();
            return;
        }

        do {
            try {
                Raycast right = new Raycast(currentPoint, angle + 90);
                Raycast left = new Raycast(currentPoint, angle - 90);
            } catch (NoHitException e) {
                System.out.println("Outside boundary, stopping");
                break;
            }
            currentPoint = currentPoint.add(robot.width * Math.cos(Math.toRadians(angle)), robot.width * Math.sin(Math.toRadians(angle)));
        } while(Raycast.lineContains(startPoint,cast.getHitPoint(),currentPoint));
    }

    private void aroundBound(ArrayList<Point2D> bound) {
        robot.pathNodes.addAll(bound);
        robot.pathNodes.add(bound.get(0));
    }
}
