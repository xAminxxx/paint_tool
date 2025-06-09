package app.shapes.decorators;

import app.shapes.Shape;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class ShadowDecorator extends ShapeDecorator {

    private final Color shadowColor;
    private final double shadowOffset;

    public ShadowDecorator(Shape decoratedShape, Color shadowColor, double shadowOffset) {
        super(decoratedShape);
        this.shadowColor = shadowColor;
        this.shadowOffset = shadowOffset;
    }

    @Override
    public void draw(GraphicsContext gc) {
        // Sauvegarder les paramètres actuels
        Color originalStroke = (Color) gc.getStroke();

        // Dessiner l'ombre
        gc.setStroke(shadowColor);
        gc.save();
        gc.translate(shadowOffset, shadowOffset);
        decoratedShape.draw(gc);
        gc.restore();

        // Dessiner la forme originale
        gc.setStroke(originalStroke);
        decoratedShape.draw(gc);
    }
}
