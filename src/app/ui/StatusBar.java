package app.ui;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class StatusBar extends HBox {

    private Label statusLabel;

    public StatusBar() {
        statusLabel = new Label();
        this.getChildren().add(statusLabel);
        this.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 5px;");
    }

    public void setText(String text) {
        statusLabel.setText(text);
    }
}
