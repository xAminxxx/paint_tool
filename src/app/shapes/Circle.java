package app.shapes;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Circle implements Shape {

    private double x1, y1, x2, y2;
    private Color color;

    public Circle(double x1, double y1, double x2, double y2, Color color) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.color = color;
    }

    @Override
    public void draw(GraphicsContext gc) {
        double radius = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
        gc.setStroke(color);
        gc.strokeOval(x1 - radius, y1 - radius, radius * 2, radius * 2);
    }

    @Override
    public String toJson() {
        return String.format("{\"type\":\"Circle\",\"x1\":%.2f,\"y1\":%.2f,\"x2\":%.2f,\"y2\":%.2f,\"color\":\"%s\"}",
                x1, y1, x2, y2, color.toString());
    }
}
