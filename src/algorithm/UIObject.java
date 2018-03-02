package algorithm;

import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;

public abstract class UIObject {
    public static ArrayList<UIObject> uiObjects = new ArrayList<>();

    UIObject() {
        uiObjects.add(this);
    }

    public void draw(GraphicsContext gc) {}

    public static void clearObjects(Class<?> type) {
        uiObjects.removeIf((o) -> o.getClass() == type);
    }
}
