import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.ArrayList;

class Robot extends UIObject {
    Point2D pos = Point2D.ZERO; // relative to center of robot
    double length = 15;
    double width = 10;

    ArrayList<Point2D> pathNodes = new ArrayList<>();
    private int curNodeDest = 1;

    public void reset() {
        pathNodes.clear();
        pathNodes.add(Point2D.ZERO);
        pos = pathNodes.get(0);
        curNodeDest = 1;
    }

    private void approachNextNode() {
        if (!(pathNodes.size() > 1)) return;

        Point2D node = pathNodes.get(curNodeDest);
        if (pos.distance(node) < 1) {
            if (curNodeDest < pathNodes.size() - 1) {
                curNodeDest++;
            }
        } else {
            Point2D delta = node.subtract(pos);
            double angle = Math.atan2(delta.getY(), delta.getX());

            pos.add(Math.cos(angle), Math.sin(angle));
        }
    }

    public void draw(GraphicsContext gc) {
        // Robot shape
        gc.setFill(Color.ORANGE);
        gc.fillRect(pos.getX() - length /2, pos.getY() - width /2, length, width);

        // Planned path
        gc.setLineWidth(5);
        gc.setStroke(Color.RED);
        for (int i = 0; i < pathNodes.size() - 1; i++) {
            gc.strokeLine(pathNodes.get(i).getX(), pathNodes.get(i).getY(), pathNodes.get(i+1).getX(), pathNodes.get(i+1).getY());
        }
        gc.fillOval(pathNodes.get(pathNodes.size()-1).getX(), pathNodes.get(pathNodes.size()-1).getY(), 5, 5);

        approachNextNode();
    }
}