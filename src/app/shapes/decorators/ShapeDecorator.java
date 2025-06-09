package app.shapes.decorators;

import app.shapes.Shape;
import javafx.scene.canvas.GraphicsContext;

public abstract class ShapeDecorator implements Shape {

    protected Shape decoratedShape;

    public ShapeDecorator(Shape decoratedShape) {
        this.decoratedShape = decoratedShape;
    }

    @Override
    public void draw(GraphicsContext gc) {
        decoratedShape.draw(gc);
    }

    @Override
    public String toJson() {
        return decoratedShape.toJson();
    }
}
