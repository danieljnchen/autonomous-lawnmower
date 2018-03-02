package algorithm;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import static ui.Controller.boundary;

public class Mouse extends UIObject {
    private Point2D position;
    private Point2D closest;

    public void setPos(double xPos, double yPos) {
        position = new Point2D(xPos, yPos);
        closest = getClosestPoint();
    }

    public Point2D getClosestPoint() {
        if (boundary.bounds.size() == 0) return null;

        Point2D closest = boundary.bounds.get(0).get(0);
        double distance = closest.distance(position);

        for (int angle = 0; angle <= 360; angle += 2) {
            try {
                Raycast out = new Raycast(position, angle, false);
                if (out.getHitPoint().distance(position) < distance) {
                    closest = out.getHitPoint();
                    distance = closest.distance(position);
                }
            } catch (NoHitException e) {
            }
        }

        return closest;
    }

    public void draw(GraphicsContext gc) {
        if (position == null || closest == null) return;

        gc.setFill(Color.CRIMSON);
        gc.fillOval(closest.getX() - 4, closest.getY() - 4, 8, 8);
    }
}
