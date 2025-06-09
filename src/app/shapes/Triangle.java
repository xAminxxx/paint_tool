package app.shapes;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Triangle implements Shape {

    public double x1, y1, x2, y2;
    private Color color;

    public Triangle(double x1, double y1, double x2, double y2, Color color) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.color = color;
    }

    private double[] getXPoints() {
        double width = Math.abs(x2 - x1);
        double minX = Math.min(x1, x2);
        return new double[]{minX + width / 2, minX, minX + width};
    }

    private double[] getYPoints() {
        double height = Math.abs(y2 - y1);
        double minY = Math.min(y1, y2);
        return new double[]{minY, minY + height, minY + height};
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setStroke(color);
        gc.strokePolygon(getXPoints(), getYPoints(), 3);
    }

    @Override
    public String toJson() {
        return String.format("{\"type\":\"Triangle\",\"x1\":%.2f,\"y1\":%.2f,\"x2\":%.2f,\"y2\":%.2f,\"color\":\"%s\"}",
                x1, y1, x2, y2, color.toString());
    }
}
