package letsdev.common.log;

import org.slf4j.Logger;
import org.slf4j.event.Level;

import java.util.EnumSet;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public final class LevelFixedLogger {
    private final LogLevel logLevel;
    private final Consumer<String> logConsumer;
    private final BiConsumer<String, Object[]> logBiConsumer;

    public LevelFixedLogger(Logger logger, LogLevel logLevel) {
        /* NOTE DO NOT USE `Set.of`.
         *  Because `Set.of` is not compatible with JDK 1.8
         *  See:
         *      Set.of          since 9
         *      EnumSet.of      since 1.5
         *      EnumSet.allOf   since 1.5
         */
        assert EnumSet.of(
                LogLevel.TRACE,
                LogLevel.DEBUG,
                LogLevel.INFO,
                LogLevel.WARN,
                LogLevel.ERROR,
                LogLevel.OFF
        ).equals(EnumSet.allOf(LogLevel.class)) : "추가된 로그 레벨에 대한 적절한 조치가 필요합니다.";
        Objects.requireNonNull(logger);
        Objects.requireNonNull(logLevel);

        this.logLevel = logLevel;

        switch (logLevel) {
            case TRACE:
                logConsumer = logger::trace;
                logBiConsumer = logger::trace;
                break;
            case DEBUG:
                logConsumer = logger::debug;
                logBiConsumer = logger::debug;
                break;
            case INFO:
                logConsumer = logger::info;
                logBiConsumer = logger::info;
                break;
            case WARN:
                logConsumer = logger::warn;
                logBiConsumer = logger::warn;
                break;
            case ERROR:
                logConsumer = logger::error;
                logBiConsumer = logger::error;
                break;
            case OFF:
                logConsumer = (ignore) -> {};
                logBiConsumer = (ignoredMessage, ignoredArgs) -> {};
                break;
            default:
                String message = "Only ALL, TRACE, DEBUG, INFO, WARN, ERROR, OFF can be used in LevelFixedLogger.";
                throw new Error(message);
        }
    }

    public LevelFixedLogger(Logger logger, Level level) {
        this(logger, converLogLevel(level));
    }

    private static LogLevel converLogLevel(Level level) {
        Objects.requireNonNull(level);
        return LogLevel.valueOf(level.name());
    }

    public LogLevel getLogLevel() {
        return logLevel;
    }

    public void log(String message) {
        logConsumer.accept(message);
    }

    public void log(String message, Object... args) {
        logBiConsumer.accept(message, args);
    }
}