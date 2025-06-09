package app.shapes;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Triangle implements Shape {

    private double x1, y1, x2, y2;
    private Color color;

    public Triangle(double x1, double y1, double x2, double y2, Color color) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.color = color;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setStroke(color);

        // Calcul des points du triangle
        double width = Math.abs(x2 - x1);
        double height = Math.abs(y2 - y1);
        double minX = Math.min(x1, x2);
        double minY = Math.min(y1, y2);

        double[] xPoints = {
            minX + width / 2, // Sommet supérieur
            minX, // Coin inférieur gauche
            minX + width // Coin inférieur droit
        };

        double[] yPoints = {
            minY, // Sommet supérieur
            minY + height, // Coin inférieur gauche
            minY + height // Coin inférieur droit
        };

        gc.strokePolygon(xPoints, yPoints, 3);
    }

    @Override
    public String toJson() {
        return String.format("{\"type\":\"Triangle\",\"x1\":%.2f,\"y1\":%.2f,\"x2\":%.2f,\"y2\":%.2f,\"color\":\"%s\"}",
                x1, y1, x2, y2, color.toString());
    }
}
