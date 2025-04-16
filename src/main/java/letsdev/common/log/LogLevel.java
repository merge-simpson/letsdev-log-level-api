package letsdev.common.log;

import org.slf4j.event.Level;

import java.util.EnumSet;

public enum LogLevel {
    TRACE(Level.TRACE),
    DEBUG(Level.DEBUG),
    INFO(Level.INFO),
    WARN(Level.WARN),
    ERROR(Level.ERROR),
    OFF(null);

    private final Level level;

    LogLevel(Level level) {
        this.level = level;
    }

    public static LogLevel valueOf(Level level) {
        assert EnumSet.of(Level.TRACE, Level.DEBUG, Level.INFO, Level.WARN, Level.ERROR).contains(level);

        return switch (level) {
            case TRACE -> TRACE;
            case DEBUG -> DEBUG;
            case INFO -> INFO;
            case WARN -> WARN;
            case ERROR -> ERROR;
            case null -> OFF;
        };
    }

    public Level toSlf4jLevel() {
        return level;
    }
}