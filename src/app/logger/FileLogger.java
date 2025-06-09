package app.logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileLogger implements Logger {

    private static final String LOG_FILE = "drawing_app.log";

    @Override
    public void log(String message) {
        String logEntry = "[FICHIER] " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME) + " - " + message;
        System.out.println(logEntry); // Toujours afficher dans la console

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            writer.write(logEntry);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Erreur d'écriture dans le fichier de log: " + e.getMessage());
        }
    }
}
