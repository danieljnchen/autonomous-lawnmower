import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.io.*;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class Boundary extends UIObject {
    ArrayList<ArrayList<Point2D>> bounds = new ArrayList<>();
    private Color[] colorCycle = { Color.BLACK, Color.TURQUOISE, Color.ORANGE, Color.GREEN, Color.RED, Color.BLUE, Color.SALMON };

    Boundary() {
        Color[] colors = new Color[colorCycle.length];

        for (int i = 0; i < colorCycle.length; i++) {
            colors[i] = colorCycle[i].deriveColor(0, 1, 1, 0.5);
        }

        System.arraycopy(colors, 0, colorCycle, 0, colorCycle.length);
    }

    public static String saveLocation = "out.txt";

    public ArrayList<Point2D> getOuterBound() {
        return bounds.get(0);
    }

    public ArrayList<ArrayList<Point2D>> getInnerBounds() {
        return (ArrayList<ArrayList<Point2D>>) bounds.subList(1, bounds.size() - 1);
    }

    public void save(String fileName) {
        try {
            PrintWriter pw = new PrintWriter(fileName);

            for (ArrayList<Point2D> bound : bounds) {
                pw.println("Bound");

                for (Point2D point : bound) {
                    pw.println(point.getX() + " " + point.getY());
                }
            }

            pw.close();

            System.out.println("Boundary saved.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void load(String fileName) {
        String line = null;
        try {
            // Clear the current boundary before loading the new one in
            bounds.clear();

            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
                if (line.equals("Bound")) {
                    bounds.add(new ArrayList<>());
                    continue;
                }

                Scanner coords = new Scanner(line).useLocale(Locale.US);
                bounds.get(bounds.size() - 1).add(new Point2D(coords.nextDouble(), coords.nextDouble()));
            }

            bufferedReader.close();

            System.out.println("Boundary loaded.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clear() {
        bounds.clear();
    }

    public void draw(GraphicsContext gc) {
        // Draw boundaries
        for (int i = 0; i < bounds.size(); i++) {
            gc.setFill(colorCycle[i % colorCycle.length]);
            gc.setStroke(colorCycle[i % colorCycle.length]);
            gc.setLineWidth(3);

            ArrayList<Point2D> bound = bounds.get(i);
            for (int j = 0; j < bound.size(); j++) {
                gc.strokeLine(bound.get(j).getX(), bound.get(j).getY(),
                        bound.get((j + 1) % bound.size()).getX(), bound.get((j + 1) % bound.size()).getY());
            }
        }
    }
}
