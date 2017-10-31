import java.awt.geom.Point2D;

public class RobotSim {
    public Point2D pos = new Point2D.Double(0, 0);
    public double width = 15;
    public double height = 10;

    public void approachPoint(Point2D point) {
        double dx = point.getX() - pos.getX();
        double dy = point.getY() - pos.getY();

        double theta = Math.atan2(dy, dx);

        pos.setLocation(pos.getX() + Math.cos(theta), pos.getY() + Math.sin(theta));
    }
}
