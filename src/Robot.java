import javafx.geometry.Point2D;
import java.util.ArrayList;

class Robot {
	Point2D pos = Point2D.ZERO; // relative to center of robot
	double width = 15;
	double height = 10;

	ArrayList<Point2D> pathNodes = new ArrayList<>();
	private int curNodeDest = 0;

	void start() {
		curNodeDest = 0;

		Main.algorithm.outerBoundary = Editor.loadPerimeter();
		Main.algorithm.generatePath();
	}

	void approachNextNode() {
		Point2D node = pathNodes.get(curNodeDest);

		double dx = node.getX() - pos.getX();
		double dy = node.getY() - pos.getY();
		double theta = Math.atan2(dy, dx);

		//pos = new Point2D(pos.getX() + Math.cos(theta), pos.getY() + Math.sin(theta));
		pos = new Point2D(pos.getX() + dx / 5, pos.getY() + dy / 5);

		if (pos.distance(node) < 1) {
			if (pathNodes.size() - 1 > curNodeDest) {
				curNodeDest++;
			}
		}
	}
}
