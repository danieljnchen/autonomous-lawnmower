package algorithm;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Robot extends UIObject {
    private final double speed = 3;

    public double length = 15;
    public double width = 10;
    private Point2D pos = Point2D.ZERO;

    private ArrayList<Point2D> pathNodes = new ArrayList<>(); // the robot will travel between these nodes
    private int curNodeDest = 1; // the index of the pathNode that the robot is currently heading towards

    private ArrayList<Point2D> pathTraveled = new ArrayList<>(); // tracks where the robot has been

    public Robot(ArrayList<UIObject> uiObjects) {
        super(uiObjects); // the robot is a rendered object
        pathNodes.add(Point2D.ZERO);
    }

    public void queueNodes(ArrayList<Point2D> nodes) {
        pathNodes.addAll(nodes);
    }

    public void reset() {
        pathNodes.clear();
        pathTraveled.clear();
        pathNodes.add(Point2D.ZERO);
        pos = pathNodes.get(0);
        curNodeDest = 0; // reset the path node index
    }

    /**
     * If the robot has reached its target path node, change the target to the next path node (if it exists).
     * Otherwise, go towards the target path node.
     */
    private void approachNextNode() {
        if (pathNodes.size() < 2) return;

        pathTraveled.add(new Point2D(pos.getX(),pos.getY()));
        Point2D node = pathNodes.get(curNodeDest);
        if (pos.distance(node) < speed) {
            if (curNodeDest < pathNodes.size() - 1) {
                curNodeDest++;
            }
        } else {
            Point2D delta = node.subtract(pos).normalize();

            pos = pos.add(speed * delta.getX(), speed * delta.getY());
        }
    }

    /**
     * Draw the robot and the path. Move the robot towards the next path node.
     * @param gc
     */
    public void draw(GraphicsContext gc) {
        //  Robot shape
        gc.setFill(Color.ORANGE);
        gc.fillRect(pos.getX() - width / 2, pos.getY() - length / 2, width, length);

        /*
        //  Planned path
        gc.setLineWidth(5);
        gc.setStroke(Color.RED);
        for (int i = 2; i < pathNodes.size() - 1; i++) {
            gc.strokeLine(pathNodes.get(i).getX(), pathNodes.get(i).getY(), pathNodes.get(i + 1).getX(), pathNodes.get(i + 1).getY());
        }*/

        // Traveled path
        gc.setLineWidth(5);
        gc.setStroke(Color.RED);
        for (int i = 0; i < pathTraveled.size()-1; i++) {
            gc.strokeLine(pathTraveled.get(i).getX(),pathTraveled.get(i).getY(),pathTraveled.get(i+1).getX(),pathTraveled.get(i+1).getY());
        }

        if (pathNodes.size() != 1) {
            if (pos == Point2D.ZERO) {
                if(pathNodes.size() > 2) {
                    pos = pathNodes.get(2);
                    curNodeDest = 3;
                }
            }
            approachNextNode();
        }
    }
}
