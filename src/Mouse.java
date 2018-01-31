import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Mouse extends UIObject {
    Point2D position;

    public Mouse() {
        position = Point2D.ZERO;
    }

    public double getX() {
        return position.getX();
    }

    public double getY() {
        return position.getY();
    }

    public void setPos(double xPos, double yPos) {
        position = new Point2D(xPos,yPos);
    }

    public Point2D getClosestPoint() {
        Point2D closest = Main.boundary.bounds.get(0).get(0);
        double distance = closest.distance(position);
        for(int i = 0; i<Main.boundary.bounds.size(); ++i) {
            for(int j = 0; j<Main.boundary.bounds.get(i).size(); ++j) {
                Point2D test = Main.boundary.bounds.get(i).get(j);
                if(position.distance(test) < distance) {
                    closest = test;
                    distance = position.distance(test);
                }
            }
        }
        return closest;
    }

    public void draw(GraphicsContext gc) {
        gc.setFill(Color.CRIMSON);
        Point2D closest = getClosestPoint();
        //System.out.println(xPos + ", " + yPos);
        //System.out.println(closest.toString());
        gc.fillOval(closest.getX() - 4,closest.getY() - 4,8,8);
    }
}
