package app.logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DatabaseLogger implements Logger {

    @Override
    public void log(String message) {
        System.out.println("[BDD] " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME) + " - " + message);
    }
}
