package app.shapes.decorators;

import app.shapes.Shape;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class BorderDecorator extends ShapeDecorator {

    private final Color borderColor;
    private final double borderWidth;

    public BorderDecorator(Shape decoratedShape, Color borderColor, double borderWidth) {
        super(decoratedShape);
        this.borderColor = borderColor;
        this.borderWidth = borderWidth;
    }

    @Override
    public void draw(GraphicsContext gc) {
        // Sauvegarder les paramètres actuels
        Color originalStroke = (Color) gc.getStroke();
        double originalWidth = gc.getLineWidth();

        // Dessiner la bordure supplémentaire
        gc.setStroke(borderColor);
        gc.setLineWidth(borderWidth);
        decoratedShape.draw(gc);

        // Restaurer les paramètres originaux
        gc.setStroke(originalStroke);
        gc.setLineWidth(originalWidth);

        // Dessiner la forme originale
        decoratedShape.draw(gc);
    }
}
