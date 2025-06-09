package app;

import app.commands.DrawCommand;
import app.dao.DrawingDAO;
import app.logger.Logger;
import app.logger.ConsoleLogger;
import app.logger.DatabaseLogger;
import app.logger.FileLogger;
import app.shapes.*;
import app.shapes.decorators.BorderDecorator;
import app.shapes.decorators.DashDecorator;
import app.shapes.decorators.ShadowDecorator;
import app.ui.StatusBar;
import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class DrawingApp extends Application {

    private Canvas drawingCanvas;
    private GraphicsContext gc;
    private String selectedShape = "Rectangle";
    private Color currentColor = Color.BLACK;
    private List<Shape> shapes = new ArrayList<>();
    private Logger logger;
    private DrawingDAO drawingDAO;
    private Shape previewShape = null;
    private boolean dashedStyle = false;
    private boolean borderEnabled = false;
    private boolean shadowEnabled = false;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        drawingDAO = DrawingDAO.getInstance();
        logger = new ConsoleLogger();

        primaryStage.setTitle("Application de Dessin Géométrique");

        BorderPane root = new BorderPane();
        ToolBar toolBar = createToolBar();
        root.setTop(toolBar);

        drawingCanvas = new Canvas(800, 500);
        gc = drawingCanvas.getGraphicsContext2D();
        clearCanvas();
        setupMouseHandlers();
        root.setCenter(drawingCanvas);

        StatusBar statusBar = new StatusBar();
        root.setBottom(statusBar);

        Scene scene = new Scene(root, 900, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showLogs() {
        List<String> logs = DatabaseLogger.getLogs();
        if (logs.isEmpty()) {
            showAlert("Journaux", "Aucun journal enregistré dans la base de données.");
            return;
        }

        // Création d'une fenêtre de dialogue pour afficher les logs
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Journaux de la base de données");
        dialog.setHeaderText("Liste des actions journalisées:");

        // Création d'une zone de texte pour afficher les logs
        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setPrefSize(600, 400);

        // Remplissage de la zone de texte avec les logs
        StringBuilder sb = new StringBuilder();
        for (String log : logs) {
            sb.append(log).append("\n");
        }
        textArea.setText(sb.toString());

        // Ajout à la boîte de dialogue
        dialog.getDialogPane().setContent(textArea);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dialog.showAndWait();
    }

    private ToolBar createToolBar() {
        ToolBar toolBar = new ToolBar();
        ToggleGroup shapeGroup = new ToggleGroup();

        // === Section Formes ===
        Label shapeLabel = new Label("Formes:");
        shapeLabel.setStyle("-fx-font-weight: bold; -fx-padding: 0 5 0 5;");

        RadioButton rectBtn = new RadioButton("Rectangle");
        rectBtn.setToggleGroup(shapeGroup);
        rectBtn.setSelected(true);
        rectBtn.setOnAction(e -> {
            selectedShape = "Rectangle";
            logger.log("Forme sélectionnée: Rectangle");
        });

        RadioButton circleBtn = new RadioButton("Cercle");
        circleBtn.setToggleGroup(shapeGroup);
        circleBtn.setOnAction(e -> {
            selectedShape = "Cercle";
            logger.log("Forme sélectionnée: Cercle");
        });

        RadioButton lineBtn = new RadioButton("Ligne");
        lineBtn.setToggleGroup(shapeGroup);
        lineBtn.setOnAction(e -> {
            selectedShape = "Ligne";
            logger.log("Forme sélectionnée: Ligne");
        });

        RadioButton triangleBtn = new RadioButton("Triangle");
        triangleBtn.setToggleGroup(shapeGroup);
        triangleBtn.setOnAction(e -> {
            selectedShape = "Triangle";
            logger.log("Forme sélectionnée: Triangle");
        });

        // === Section Couleur ===
        Separator shapeColorSeparator = new Separator(Orientation.VERTICAL);

        Label colorLabel = new Label("Couleur:");
        colorLabel.setStyle("-fx-font-weight: bold; -fx-padding: 0 5 0 5;");

        ColorPicker colorPicker = new ColorPicker(currentColor);
        colorPicker.setPrefWidth(80);
        colorPicker.setOnAction(e -> {
            currentColor = colorPicker.getValue();
            logger.log("Couleur changée: " + currentColor.toString());
        });

        // === Section Effets Décorateurs ===
        Separator decoratorSeparator = new Separator(Orientation.VERTICAL);

        Label decoratorLabel = new Label("Effets:");
        decoratorLabel.setStyle("-fx-font-weight: bold; -fx-padding: 0 5 0 5;");

        // Option Pointillés
        CheckBox dashCheck = new CheckBox("Pointillés");
        dashCheck.setStyle("-fx-font-size: 11px;");
        dashCheck.setOnAction(e -> {
            dashedStyle = dashCheck.isSelected();
            logger.log("Style pointillé: " + (dashedStyle ? "activé" : "désactivé"));
        });

        // Option Bordure Épaisse
        CheckBox borderCheck = new CheckBox("Bordure épaisse");
        borderCheck.setStyle("-fx-font-size: 11px;");
        borderCheck.setOnAction(e -> {
            borderEnabled = borderCheck.isSelected();
            logger.log("Bordure épaisse: " + (borderEnabled ? "activée" : "désactivée"));
        });

        // Option Ombre
        CheckBox shadowCheck = new CheckBox("Ombre");
        shadowCheck.setStyle("-fx-font-size: 11px;");
        shadowCheck.setOnAction(e -> {
            shadowEnabled = shadowCheck.isSelected();
            logger.log("Ombre: " + (shadowEnabled ? "activée" : "désactivée"));
        });

        // === Section Actions ===
        Separator actionSeparator = new Separator(Orientation.VERTICAL);

        Button clearBtn = new Button("Effacer");
        clearBtn.setOnAction(e -> {
            clearCanvas();
            shapes.clear();
            logger.log("Zone de dessin effacée");
        });

        Button saveBtn = new Button("Sauvegarder");
        saveBtn.setOnAction(e -> {
            saveDrawing();
            logger.log("Dessin sauvegardé dans la base de données");
        });

        Button loadBtn = new Button("Ouvrir");
        loadBtn.setOnAction(e -> {
            loadDrawing();
            logger.log("Dessin chargé depuis la base de données");
        });
        Button showLogsBtn = new Button("Afficher Journaux");
        showLogsBtn.setOnAction(e -> {
            showLogs();
            logger.log("Affichage des journaux demandé");
        });

        // === Section Journalisation ===
        Separator loggerSeparator = new Separator(Orientation.VERTICAL);

        Label loggerLabel = new Label("Journalisation:");
        loggerLabel.setStyle("-fx-padding: 0 5 0 5;");

        ComboBox<String> loggerCombo = new ComboBox<>();
        loggerCombo.getItems().addAll("Console", "Fichier", "Base de données");
        loggerCombo.setValue("Console");
        loggerCombo.setOnAction(e -> {
            String choice = loggerCombo.getValue();
            switch (choice) {
                case "Console":
                    logger = new ConsoleLogger();
                    break;
                case "Fichier":
                    logger = new FileLogger();
                    break;
                case "Base de données":
                    logger = new DatabaseLogger();
                    break;
            }
            logger.log("Stratégie de journalisation changée: " + choice);
        });

        // Ajout de tous les éléments à la barre d'outils
        toolBar.getItems().addAll(
                // Section Formes
                shapeLabel,
                rectBtn, circleBtn, lineBtn, triangleBtn,
                shapeColorSeparator,
                // Section Couleur
                colorLabel, colorPicker,
                decoratorSeparator,
                // Section Effets
                decoratorLabel,
                dashCheck, borderCheck, shadowCheck,
                actionSeparator,
                // Section Actions
                clearBtn, saveBtn, loadBtn,
                loggerSeparator,
                showLogsBtn,
                // Section Journalisation
                loggerLabel, loggerCombo
        );

        return toolBar;
    }

    private void setupMouseHandlers() {
        final double[] startX = new double[1];
        final double[] startY = new double[1];

        drawingCanvas.setOnMousePressed(e -> {
            startX[0] = e.getX();
            startY[0] = e.getY();
        });

        drawingCanvas.setOnMouseDragged(e -> {
            double endX = e.getX();
            double endY = e.getY();

            // Créer une forme temporaire
            Shape tempShape = ShapeFactory.createShape(selectedShape, startX[0], startY[0], endX, endY, currentColor);

            if (tempShape != null) {
                // Appliquer les décorateurs
                if (dashedStyle) {
                    tempShape = new DashDecorator(tempShape, 10.0, 5.0);
                }
                if (borderEnabled) {
                    tempShape = new BorderDecorator(tempShape, Color.DARKGRAY, 3.0);
                }
                if (shadowEnabled) {
                    tempShape = new ShadowDecorator(tempShape, Color.GRAY, 3.0);
                }

                previewShape = tempShape;
                redrawCanvas();
            }
        });

        drawingCanvas.setOnMouseReleased(e -> {
            double endX = e.getX();
            double endY = e.getY();

            Shape shape = ShapeFactory.createShape(selectedShape, startX[0], startY[0], endX, endY, currentColor);

            if (shape != null) {
                // Appliquer les décorateurs
                if (dashedStyle) {
                    shape = new DashDecorator(shape, 10.0, 5.0);
                }
                if (borderEnabled) {
                    shape = new BorderDecorator(shape, Color.DARKGRAY, 3.0);
                }
                if (shadowEnabled) {
                    shape = new ShadowDecorator(shape, Color.GRAY, 3.0);
                }

                new DrawCommand(this, shape).execute();
                logger.log("Forme dessinée: " + selectedShape + " avec effets");
            }

            previewShape = null;
            redrawCanvas();
        });
    }

    private void clearCanvas() {
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, drawingCanvas.getWidth(), drawingCanvas.getHeight());
        gc.setStroke(Color.BLACK);
        gc.strokeRect(0, 0, drawingCanvas.getWidth(), drawingCanvas.getHeight());
    }

    private void redrawCanvas() {
        clearCanvas();
        for (Shape shape : shapes) {
            shape.draw(gc);
        }
        if (previewShape != null) {
            previewShape.draw(gc);
        }
    }

    public void drawShape(Shape shape) {
        shape.draw(gc);
        shapes.add(shape);
    }

    private void saveDrawing() {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("{\"shapes\":[");
            for (Shape shape : shapes) {
                sb.append(shape.toJson()).append(",");
            }
            if (!shapes.isEmpty()) {
                sb.deleteCharAt(sb.length() - 1);
            }
            sb.append("]}");

            drawingDAO.saveDrawing("MonDessin", sb.toString());
            showAlert("Sauvegarde réussie", "Le dessin a été sauvegardé avec succès.");
        } catch (Exception e) {
            showAlert("Erreur de sauvegarde", "Erreur lors de la sauvegarde: " + e.getMessage());
        }
    }

    private void loadDrawing() {
        try {
            String drawingData = drawingDAO.loadDrawing("MonDessin");
            if (drawingData != null) {
                clearCanvas();
                shapes.clear();

                // Parsing simplifié
                if (drawingData.contains("Rectangle")) {
                    // ... chargement du rectangle ...
                } else if (drawingData.contains("Triangle")) {
                    String[] parts = drawingData.split(",");
                    double x1 = Double.parseDouble(parts[1].split(":")[1]);
                    double y1 = Double.parseDouble(parts[2].split(":")[1]);
                    double x2 = Double.parseDouble(parts[3].split(":")[1]);
                    double y2 = Double.parseDouble(parts[4].split(":")[1]);
                    String colorStr = parts[5].split(":")[1].replaceAll("\"", "").replace("}", "");

                    Shape shape = new Triangle(x1, y1, x2, y2, Color.valueOf(colorStr));
                    drawShape(shape);
                }
                showAlert("Chargement réussi", "Le dessin a été chargé avec succès.");
            } else {
                showAlert("Aucun dessin", "Aucun dessin trouvé avec ce nom.");
            }
        } catch (Exception e) {
            showAlert("Erreur de chargement", "Erreur lors du chargement: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
