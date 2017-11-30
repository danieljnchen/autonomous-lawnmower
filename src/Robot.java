import javafx.scene.paint.Color;

import java.awt.geom.Point2D;
import java.util.ArrayList;

class Robot extends UIObject {
    Point2D pos = new Point2D.Double(); // relative to center of robot
    double length = 15;
    double width = 10;

    ArrayList<Point2D> pathNodes = new ArrayList<>();
    private int curNodeDest = 0;

    void approachNextNode() {
        Point2D node = pathNodes.get(curNodeDest);

        double dx = node.getX() - pos.getX();
        double dy = node.getY() - pos.getY();
        double theta = Math.atan2(dy, dx);

        //pos.setLocation(pos.getX() + Math.cos(theta), pos.getY() + Math.sin(theta));
        pos.setLocation(pos.getX() + dx/5, pos.getY() + dy/5);

        if (pos.distance(node) < 1) {
            if (pathNodes.size() - 1 > curNodeDest) {
                curNodeDest++;
            }
        }
    }

    public void draw() {
        // Robot shape
        gc.setFill(Color.ORANGE);
        gc.fillRect(pos.getX() - length /2, pos.getY() - width /2, length, width);

        // Planned path
        gc.setLineWidth(2);
        gc.setStroke(Color.RED);
        for (int i = 0; i < pathNodes.size() - 1; i++) {
            gc.strokeLine(pathNodes.get(i).getX(), pathNodes.get(i).getY(), pathNodes.get(i+1).getX(), pathNodes.get(i+1).getY());
        }

        if (pathNodes.size() != 0) {
            gc.fillOval(pathNodes.get(pathNodes.size()-1).getX(), pathNodes.get(pathNodes.size()-1).getY(), 5, 5);
            approachNextNode();
        }
    }
}
