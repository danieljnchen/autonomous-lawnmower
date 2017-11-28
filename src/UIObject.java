import javafx.scene.canvas.GraphicsContext;

public class UIObject {
    static GraphicsContext gc;

    public UIObject() {
        Main.uiObjects.add(this);
    }

    public void draw() {}
}
