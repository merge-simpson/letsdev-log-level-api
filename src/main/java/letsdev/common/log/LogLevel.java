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

        switch (level) {
            case TRACE:
                return TRACE;
            case DEBUG:
                return DEBUG;
            case INFO:
                return INFO;
            case WARN:
                return WARN;
            case ERROR:
                return ERROR;
            default:
                return OFF;
        }
    }

    public Level toSlf4jLevel() {
        return level;
    }
}