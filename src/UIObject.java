import javafx.scene.canvas.GraphicsContext;

public class UIObject {
    static GraphicsContext gc;

    public UIObject() {
        Testing.uiObjects.add(this);
    }

    public void draw() {}
}
