import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;

import java.lang.reflect.Array;
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

    public void raycastIterativeStart(Point2D startPoint, double angle, boolean side) {
        ArrayList<ArrayList<Point2D>> pathNodes = raycastIterative(startPoint, angle, side, new ArrayList<>(), 0);
        ArrayList<Point2D> pathNodesOut = new ArrayList<>();
        for(int i = 0; i<pathNodes.size(); ++i) {
            pathNodesOut.addAll(pathNodes.get(i));
            if(i<(pathNodes.size()-1)) {
                //pathNodes.addAll(traverse(pathNodes.get(i).get(pathNodes.get(i).size()-1), pathNodes.get(i+1).get(0)));
            }
        }
        robot.pathNodes.addAll(pathNodesOut);
    }
    //TODO split raycastIterative into different clusters
    public ArrayList<ArrayList<Point2D>> raycastIterative(Point2D startPoint, double angle, boolean side, ArrayList<ArrayList<Point2D>> pathNodesOut, int cluster) {
        Raycast cast;

        try {
            cast = new Raycast(startPoint, angle);
            if(cast.getHitPoint().distance(startPoint) <= robot.width) {
                //split
            }
        } catch(NoHitException e) {
            e.printStackTrace();
        }

        Raycast right, left;
        try {
            right = new Raycast(startPoint, angle + 90); //raycast to the left and the right
            left = new Raycast(startPoint, angle - 90);
            //if(right.getNumHit()> 1)
            if(side) { //alternate so robot follows a zigzag path
                pathNodesOut.get(cluster).add(right.getHitPoint());
                pathNodesOut.get(cluster).add(left.getHitPoint());
            } else {
                pathNodesOut.get(cluster).add(left.getHitPoint());
                pathNodesOut.get(cluster).add(right.getHitPoint());
            }
            raycastIterative(right.getHitPoint().midpoint(left.getHitPoint()).add(new Point2D(robot.width*Math.cos(Math.toRadians(angle)),
                    robot.width*Math.sin(Math.toRadians(angle)))), angle, !side, pathNodesOut, cluster);
        } catch(NoHitException e) {
            e.printStackTrace();
            return pathNodesOut;
        }
        return pathNodesOut;
    }
    public double raycastComb(Point2D startPoint, double angle) {

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
    }

    private void aroundBound(ArrayList<Point2D> bound) {
        robot.pathNodes.addAll(bound);
        robot.pathNodes.add(bound.get(0));
    }
}
