package app.shapes.decorators;

import app.shapes.Shape;
import javafx.scene.canvas.GraphicsContext;

public class DashDecorator extends ShapeDecorator {

    private final double[] dashPattern;

    public DashDecorator(Shape decoratedShape, double... dashPattern) {
        super(decoratedShape);
        this.dashPattern = dashPattern;
    }

    @Override
    public void draw(GraphicsContext gc) {
        // Sauvegarder les paramètres actuels
        double[] originalDashes = gc.getLineDashes();
        double originalDashOffset = gc.getLineDashOffset();

        // Appliquer le motif de pointillés
        gc.setLineDashes(dashPattern);
        gc.setLineDashOffset(0);

        // Dessiner la forme décorée
        decoratedShape.draw(gc);

        // Restaurer les paramètres originaux
        gc.setLineDashes(originalDashes);
        gc.setLineDashOffset(originalDashOffset);
    }
}
