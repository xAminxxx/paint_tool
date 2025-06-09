package app.shapes;

import javafx.scene.paint.Color;

public class ShapeFactory {

    public static Shape createShape(String type, double x1, double y1, double x2, double y2, Color color) {
        switch (type) {
            case "Rectangle":
                return new Rectangle(x1, y1, x2, y2, color);
            case "Cercle":
                return new Circle(x1, y1, x2, y2, color);
            case "Ligne":
                return new Line(x1, y1, x2, y2, color);
            case "Triangle":
                return new Triangle(x1, y1, x2, y2, color);
            default:
                return null;
        }
    }
}
