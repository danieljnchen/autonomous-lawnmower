import java.awt.geom.Point2D;
import java.util.ArrayList;

class Robot {
    Point2D pos = new Point2D.Double(0, 0);
    double width = 15;
    double height = 10;

    ArrayList<Point2D> pathNodes = new ArrayList<>();
    private int curNodeDest = 0;

    void approachNextNode() {
        Point2D node = pathNodes.get(curNodeDest);

        double dx = node.getX() - pos.getX();
        double dy = node.getY() - pos.getY();
        double theta = Math.atan2(dy, dx);

        pos.setLocation(pos.getX() + Math.cos(theta), pos.getY() + Math.sin(theta));

        if (pos.distance(node) < 0.5) {
            if (pathNodes.size() - 1 > curNodeDest) {
                curNodeDest++;
            } else {
                curNodeDest = 0;
            }
        }
    }
}
