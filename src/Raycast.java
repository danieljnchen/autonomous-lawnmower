import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

import java.util.ArrayList;


public class Raycast extends UIObject {
    private Point2D startPoint;
    private ArrayList<Point2D> hitPoints = new ArrayList<>();

    private Point2D cast;

    private ArrayList<Point2D> points1 = new ArrayList<>();
    private ArrayList<Point2D> points2 = new ArrayList<>();

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
        // Create a new point for us to manipulate
        startPoint = start;
        //cast = new Point2D(start.getX(), start.getY());
/*        double speedCoef = .5;

        for(int index = 0; index < boundary.outerBound.size(); ++index) {
            boolean sign; //false -> angle to cast less than angle, true -> angle to cast greater than angle
            boolean finished = false;
            Point2D point1C = boundary.outerBound.get(index%(boundary.outerBound.size()-1));
            Point2D point2C = boundary.outerBound.get((index+1)%(boundary.outerBound.size()-1));
            double angleToIndex = Math.atan2(point1C.getY() - startPoint.getY(),point1C.getX() - startPoint.getX());
            double angleToIndex1 = Math.atan2(point2C.getY() - startPoint.getY(),point2C.getX() - startPoint.getX());

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
        for (int index = 0; index <= boundary.outerBound.size(); index++) {
            Point2D point1 = boundary.outerBound.get(index%boundary.outerBound.size());
            Point2D point2 = boundary.outerBound.get((index+1)%boundary.outerBound.size());
            Point2D hitPoint = intersection(startPoint,new Point2D(startPoint.getX()+1000*Math.cos(angle),startPoint.getY()+1000*Math.sin(angle)),point1,point2);
            if(hitPoint != null) {
                points1.add(point1);
                points2.add(point2);
                hitPoints.add(hitPoint);
            }
        }
    }

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
            out = new Point2D(line1initial.getX()+vector1.getX()*scalar1,line1initial.getY()+vector1.getY()*scalar1);
        } else {
            out = null;
        }

        return out;
    }

    private double distance(Point2D a, Point2D b) {
        return Math.sqrt(Math.pow(b.getX()-a.getX(),2)+Math.pow(b.getY()-a.getY(),2));
    }

    public void draw() {
        gc.setStroke(Color.BLUE);
        gc.setFill(Color.BLUE);

        // Initial raycast point
        gc.fillOval(startPoint.getX(),startPoint.getY(),5,5);

        if(hitPoints.size() != 0) {
            Point2D hitPoint = hitPoints.get(0);
            for (int i = 0; i < points1.size(); ++i) {

                // Target line segment
                gc.strokeLine(points1.get(i).getX(), points1.get(i).getY(), points2.get(i).getX(), points2.get(i).getY());
                if (distance(startPoint, hitPoints.get(i)) < distance(startPoint, hitPoint)) {
                    hitPoint = hitPoints.get(i);
                }
                gc.fillOval(hitPoints.get(i).getX(), hitPoints.get(i).getY(), 5, 5);
            }

            // Hit point
            //gc.fillOval(hitPoint.getX(), hitPoint.getY(), 5, 5);

            // Raycast line
            gc.strokeLine(startPoint.getX(),startPoint.getY(),hitPoint.getX(),hitPoint.getY());
        } else {
            System.out.println("Failed to hit a point.");
        }
    }
}
