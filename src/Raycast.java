import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

public class Raycast extends UIObject {
    private Point2D startPoint;
    private Point2D hitPoint;

    private Point2D cast;

    private Point2D point1;
    private Point2D point2;

    public Raycast(Point2D start, double angle, Boundary boundary) {
        super();
        start(start, angle, boundary);
    }

    /***
     * Start a raycast in the specified direction. Angle is in degrees.
     * @param start
     * @param angle
     * @param boundary
     */
    private void start(Point2D start, double angle, Boundary boundary) {
        startPoint = start;

        // Create a new point for us to manipulate
        cast = new Point2D(start.getX(), start.getY());

        for(int index = 0; index < boundary.outerBound.size(); ++index) {
            boolean sign; //false -> angle to cast less than angle, true -> angle to cast greater than angle
            boolean finished = false;
            double angleToIndex = Math.atan2(boundary.outerBound.get(index%(boundary.outerBound.size()-1)).getY() - cast.getY(), boundary.outerBound.get(index%(boundary.outerBound.size()-1)).getX() - cast.getX());
            double angleToIndex1 = Math.atan2(boundary.outerBound.get((index+1)%(boundary.outerBound.size()-1)).getY(),boundary.outerBound.get((index+1)%(boundary.outerBound.size()-1)).getX());

            if(angleToIndex == angle) {
                finished = true;
            }
            sign = (angleToIndex < angle);
            if((angleToIndex1 < angle) != sign) {
                finished = true;
            }
            if(finished) {
                point1 = boundary.outerBound.get(index%(boundary.outerBound.size()-1));
                point2 = boundary.outerBound.get((index+1)%(boundary.outerBound.size()-1));
                break;
            }
        }

        /*
        // Search for two points closest to a specified direction
        double curAngle = 90; // start at 90 degrees; rotate left/right depending on raycast direction
        if (angle > 90) {

        }

        // First point
        for (Point2D point : boundary.outerBound) {
            if (point.distance(cast) < curAngle) {
                curAngle = point.distance(cast);
                point1 = point;
            }
        }

        curAngle = cast.distance(point2);

        // Second point
        for (Point2D point : boundary.outerBound) {
            if (point.distance(cast) < curAngle && point != point1) {
                curAngle = point.distance(cast);
                point2 = point;
            }
        }
        */

        // Iterate the point forwards in the specified direction
        /*while (true) {
            double angle1 = Math.atan2(point1.getY() - cast.getY(), point1.getX() - cast.getX());
            double angle2 = Math.atan2(point2.getY() - cast.getY(), point2.getX() - cast.getX());

            // Check if the vector angles of the two points are opposite each other
            if (angle1 + angle2 < .05) break;

            cast = new Point2D(cast.getX() + Math.cos(angle) * speedCoef, cast.getY() + Math.sin(angle) * speedCoef);
        }*/
        //for(int i = 0; i<=boundary.outerBound.size(); ++i) {

        /*
        while(true) {
            //Point2D intersect = intersection(startPoint,cast,boundary.outerBound.get(i),boundary.outerBound.get((i+1)%(boundary.outerBound.size()-1)));
            Point2D intersect = intersection(startPoint,cast,point1,point2);
            if(intersect != null) {
                hitPoint = intersect;
                break;
            } else if(Math.abs(cast.getX())>500) {
                break;
            }
            cast = new Point2D(cast.getX()+Math.cos(angle)*speedCoef,cast.getY()+Math.sin(angle)*speedCoef);
            System.out.println(cast.getX() + " " + cast.getY());
        }
        */
    }

    /*public Point2D hit() {
        // If raycast check is getting further from all points, stop; we're not going to hit anything
        if (false) System.out.println("Error: raycast failed to hit a target");
        return hitPoint;
    }*/

    public static Point2D intersection(Point2D line1initial, Point2D line1terminal, Point2D line2initial, Point2D line2terminal) {
        Point2D vector1;
        Point2D vector2;

        vector1 = new Point2D(line1terminal.getX()-line1initial.getX(),line1terminal.getY()-line1initial.getY());
        vector2 = new Point2D(line2terminal.getX()-line2initial.getX(),line2terminal.getY()-line2initial.getY());

        double scalar1, scalar2;
        scalar2 = (vector1.getY()/vector1.getX()*(line2initial.getX()-line1initial.getX())-(line2initial.getY()-line1initial.getY()))
                /(vector2.getY()*(1-(vector1.getY()*vector2.getX())/(vector1.getX()*vector2.getY())));
        scalar1 = (line2initial.getX()-line1initial.getX()+scalar2*vector2.getX())/vector1.getX();

        Point2D out;
        if(scalar1 >= 0 && scalar1 <= 1 && scalar2 >= 0 && scalar2 <=1) {
            out = Point2D.ZERO;
            out = new Point2D(line1initial.getX()+vector1.getX()*scalar1,line1initial.getY()+vector1.getY()*scalar1);
        } else {
            out = null;
        }

        return out;
    }

    public void draw() {
        gc.setStroke(Color.BLUE);
        gc.setFill(Color.BLUE);

        // Initial raycast point
        //gc.fillOval(startPoint.getX(),startPoint.getY(),5,5);

        // Target line segment
        gc.strokeLine(point1.getX(),point1.getY(),point2.getX(),point2.getY());

        // Raycast line
        //gc.strokeLine(startPoint.getX(),startPoint.getY(),cast.getX(),cast.getY());

        // Hit point
        if(hitPoint != null) {
            //gc.fillOval(hitPoint.getX(), hitPoint.getY(), 5, 5);
        }
    }
}
