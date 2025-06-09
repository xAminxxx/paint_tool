package app.shapes;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Rectangle implements Shape {

    public double x1, y1, x2, y2;
    private Color color;

    public Rectangle(double x1, double y1, double x2, double y2, Color color) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.color = color;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setStroke(color);
        gc.strokeRect(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x2 - x1), Math.abs(y2 - y1));
    }

    @Override
    public String toJson() {
        return String.format("{\"type\":\"Rectangle\",\"x1\":%.2f,\"y1\":%.2f,\"x2\":%.2f,\"y2\":%.2f,\"color\":\"%s\"}",
                x1, y1, x2, y2, color.toString());
    }
}
