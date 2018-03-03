package algorithm;

import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;

public abstract class UIObject {
    public ArrayList<UIObject> uiObjects;

    UIObject(ArrayList<UIObject> uiObjects) {
        this.uiObjects = uiObjects;
        uiObjects.add(this);
    }

    public void draw(GraphicsContext gc) {}

    public static void removeType(ArrayList<UIObject> uiObjects, Class<?> type) {
        uiObjects.removeIf(o -> o.getClass() == type);
    }
}
