import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;

public abstract class UIObject {
    static ArrayList<UIObject> uiObjects = new ArrayList<>();

    UIObject() {
        uiObjects.add(this);
    }

    public void draw(GraphicsContext gc) {}
}
