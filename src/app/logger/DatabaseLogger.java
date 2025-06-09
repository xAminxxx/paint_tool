package app.logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DatabaseLogger implements Logger {

    private static final List<String> logDatabase = new ArrayList<>();

    @Override
    public void log(String message) {
        String logEntry = "[BDD] " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME) + " - " + message;
        System.out.println(logEntry); // Toujours afficher dans la console
        logDatabase.add(logEntry);
    }

    // Méthode pour récupérer les logs (pour affichage ou sauvegarde)
    public static List<String> getLogs() {
        return new ArrayList<>(logDatabase);
    }
}
