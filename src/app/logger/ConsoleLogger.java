package app.logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ConsoleLogger implements Logger {

    @Override
    public void log(String message) {
        System.out.println("[CONSOLE] " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME) + " - " + message);
    }
}
