package utility;

import javafx.geometry.Point2D;

public class RaycastObject {
    private Point2D hitPoint;
    private Point2D segmentPoint1;
    private int segmentPoint1Index;
    private Point2D segmentPoint2;
    private int segmentPoint2Index;
    private int hitPointBound;

    public RaycastObject(Point2D hitPoint, Point2D segmentPoint1, int segmentPoint1Index, Point2D segmentPoint2, int segmentPoint2Index, int hitPointBound) {
        this.hitPoint = hitPoint;
        this.segmentPoint1 = segmentPoint1;
        this.segmentPoint1Index = segmentPoint1Index;
        this.segmentPoint2 = segmentPoint2;
        this.segmentPoint2Index = segmentPoint2Index;
        this.hitPointBound = hitPointBound;
    }

    public Point2D getHitPoint() {
        return hitPoint;
    }

    public Point2D getSegmentPoint1() {
        return segmentPoint1;
    }

    public int getSegmentPoint1Index() {
        return segmentPoint1Index;
    }

    public Point2D getSegmentPoint2() {
        return segmentPoint2;
    }

    public int getSegmentPoint2Index() {
        return segmentPoint2Index;
    }

    public int getHitPointBound() {
        return hitPointBound;
    }
}
