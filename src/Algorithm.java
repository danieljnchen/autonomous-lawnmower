import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Algorithm {
    Robot robot;

    ArrayList<Point2D> boundary = new ArrayList<>();
    
    Algorithm(Robot robot) {
        this.robot = robot;
    }

    void generatePath() {
        generateZigZag();
    }

    private void generateZigZag() {
        double width = 50;
        double height = 100;
        
        // Get point that stops the robot
        for (int i = 0; i < height; i+=2) {
            Main.robot.pathNodes.add(new Point2D.Double(0, robot.width * i));
            Main.robot.pathNodes.add(new Point2D.Double(width, robot.width * i));
            Main.robot.pathNodes.add(new Point2D.Double(width, robot.width * (i+1)));
            Main.robot.pathNodes.add(new Point2D.Double(0, robot.width * (i+1)));
            Main.robot.pathNodes.add(new Point2D.Double(0, robot.width * (i+2)));
        }
    }

    public ArrayList<Point2D> makeRect(Point2D p1, Point2D p2) {
        ArrayList<Point2D> points = new ArrayList<>();

        points.add(new Point2D.Double(p1.getX(), p1.getY()));
        points.add(new Point2D.Double(p2.getX(), p1.getY()));
        points.add(new Point2D.Double(p2.getX(), p2.getY()));
        points.add(new Point2D.Double(p1.getX(), p2.getY()));

        return points;
    }
}
