import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Mouse extends UIObject {
    private double xPos;
    private double yPos;

    public Mouse() {
        xPos = 0;
        yPos = 0;
    }

    public double getX() {
        return xPos;
    }

    public double getY() {
        return yPos;
    }

    public void setPos(double xPos, double yPos) {
        this.xPos = xPos;
        this.yPos = yPos;
    }

    public Point2D getClosestPoint() {
        double distance = 10000;
        Point2D closest = Point2D.ZERO;
        for(int i = 0; i<Main.boundary.bounds.size(); ++i) {
            for(int j = 0; j<Main.boundary.bounds.get(i).size(); ++j) {
                if(Main.boundary.bounds.get(i).get(j).distance(new Point2D(xPos,yPos)) < distance) {
                    closest = Main.boundary.bounds.get(i).get(j);
                }
            }
        }
        return closest;
    }

    public void draw(GraphicsContext gc) {
        /*gc.setFill(Color.CRIMSON);
        Point2D closest = getClosestPoint();
        System.out.println(closest.toString());
        gc.fillOval(closest.getX() - 2,closest.getY() - 2,10,10);*/
        System.out.println("Mouse drawn");
    }
}
