package app.shapes;

import javafx.scene.canvas.GraphicsContext;

public interface Shape {

    void draw(GraphicsContext gc);

    String toJson();
}
