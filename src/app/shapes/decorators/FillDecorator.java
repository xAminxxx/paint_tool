package app.shapes.decorators;

import app.shapes.Shape;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class FillDecorator extends ShapeDecorator {

    private final Color fillColor;
    private final double opacity;

    public FillDecorator(Shape decoratedShape, Color fillColor, double opacity) {
        super(decoratedShape);
        this.fillColor = fillColor;
        this.opacity = opacity;
    }

    @Override
    public void draw(GraphicsContext gc) {
        // Dessiner la forme remplie
        Color originalFill = (Color) gc.getFill();
        double originalOpacity = gc.getGlobalAlpha();

        gc.setFill(fillColor);
        gc.setGlobalAlpha(opacity);

        // Pour le remplissage, nous devons adapter selon la forme
        if (decoratedShape instanceof Rectangle) {
            Rectangle rect = (Rectangle) decoratedShape;
            gc.fillRect(
                    Math.min(rect.x1, rect.x2),
                    Math.min(rect.y1, rect.y2),
                    Math.abs(rect.x2 - rect.x1),
                    Math.abs(rect.y2 - rect.y1)
            );
        } else if (decoratedShape instanceof Circle) {
            Circle circle = (Circle) decoratedShape;
            double radius = Math.sqrt(Math.pow(circle.x2 - circle.x1, 2) + Math.pow(circle.y2 - circle.y1, 2));
            gc.fillOval(circle.x1 - radius, circle.y1 - radius, radius * 2, radius * 2);
        } else if (decoratedShape instanceof Triangle) {
            Triangle triangle = (Triangle) decoratedShape;
            double width = Math.abs(triangle.x2 - triangle.x1);
            double height = Math.abs(triangle.y2 - triangle.y1);
            double minX = Math.min(triangle.x1, triangle.x2);
            double minY = Math.min(triangle.y1, triangle.y2);

            double[] xPoints = {minX + width / 2, minX, minX + width};
            double[] yPoints = {minY, minY + height, minY + height};

            gc.fillPolygon(xPoints, yPoints, 3);
        }

        // Restaurer les paramètres
        gc.setFill(originalFill);
        gc.setGlobalAlpha(originalOpacity);

        // Dessiner le contour original
        decoratedShape.draw(gc);
    }
}
