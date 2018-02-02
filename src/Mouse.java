import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Mouse extends UIObject {
    private Point2D position = Point2D.ZERO;
    private Point2D closest = Point2D.ZERO;


    public double getX() {
        return position.getX();
    }

    public double getY() {
        return position.getY();
    }

    public void setPos(double xPos, double yPos) {
        position = new Point2D(xPos,yPos);
        closest = getClosestPoint();
    }

    public Point2D getClosestPoint() {
        Point2D closest = Main.boundary.bounds.get(0).get(0);
        double distance = closest.distance(position);
        for(int angle = 0; angle <= 360; angle+=2) {
            try {
                Raycast out = new Raycast(position, angle);
                UIObject.uiObjects.remove(out);
                if(out.getHitPoint().distance(position) < distance) {
                    closest = out.getHitPoint();
                    distance = closest.distance(position);
                }
            } catch(NoHitException e) {}
        }
        return closest;
    }

    public void draw(GraphicsContext gc) {
        gc.setFill(Color.CRIMSON);
        gc.fillOval(closest.getX() - 4,closest.getY() - 4,8,8);
    }
}
