import java.awt.geom.Point2D;

class RobotSim {
    Point2D pos = new Point2D.Double(0, 0);
    double width = 15;
    double height = 10;
    private Point2D currentTarget;

    void approachPoint(Point2D point) {
        currentTarget = point;

        double dx = point.getX() - pos.getX();
        double dy = point.getY() - pos.getY();

        double theta = Math.atan2(dy, dx);

        pos.setLocation(pos.getX() + Math.cos(theta), pos.getY() + Math.sin(theta));
    }
}
