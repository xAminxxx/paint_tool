package app.commands;

import app.DrawingApp;
import app.shapes.Shape;

public class DrawCommand {

    private DrawingApp app;
    private Shape shape;

    public DrawCommand(DrawingApp app, Shape shape) {
        this.app = app;
        this.shape = shape;
    }

    public void execute() {
        app.drawShape(shape);
    }
}
