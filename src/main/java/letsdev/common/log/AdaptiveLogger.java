package letsdev.common.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public final class AdaptiveLogger {
    private final Logger logger;
    private final Map<LogLevel, LevelFixedLogger> cachedLoggers = new ConcurrentHashMap<>();

    private <T> AdaptiveLogger(Class<T> targetClass) {
        Objects.requireNonNull(targetClass);
        this.logger = LoggerFactory.getLogger(targetClass);
    }

    public static <T> AdaptiveLogger getLogger(Class<T> targetClass) {
        return AdaptiveLoggerHolder.ADAPTIVE_LOGGERS.computeIfAbsent(
                targetClass, AdaptiveLogger::new
        );
    }

    public LevelFixedLogger with(LogLevel logLevel) {
        return cachedLoggers.computeIfAbsent(
                logLevel,
                (level) -> new LevelFixedLogger(logger, level)
        );
    }

    public static final class LevelFixedLogger {
        private final LogLevel logLevel;
        private final Consumer<String> logConsumer;
        private final BiConsumer<String, Object[]> logBiConsumer;

        public LevelFixedLogger(Logger logger, LogLevel logLevel) {
            Objects.requireNonNull(logger);
            Objects.requireNonNull(logLevel);
            assert LogLevel.values().length == 7 : "추가된 로그 레벨에 대한 적절한 조치가 필요합니다.";
            this.logLevel = logLevel;
            this.logConsumer = switch (logLevel) {
                case ALL, TRACE -> logger::trace;
                case DEBUG -> logger::debug;
                case INFO -> logger::info;
                case WARN -> logger::warn;
                case ERROR -> logger::error;
                case OFF -> (ignore) -> {};
            };
            this.logBiConsumer = switch (logLevel) {
                case ALL, TRACE -> logger::trace;
                case DEBUG -> logger::debug;
                case INFO -> logger::info;
                case WARN -> logger::warn;
                case ERROR -> logger::error;
                case OFF -> (ignoreMessage, ignoreArgs) -> {};
            };
        }

        public LevelFixedLogger(Logger logger, Level level) {
            this(logger, converLogLevel(level));
        }

        private static LogLevel converLogLevel(Level level) {
            Objects.requireNonNull(level);
            return LogLevel.valueOf(level.name());
        }

        public void log(String message) {
            if (logLevel == LogLevel.OFF) {
                return;
            }
            logConsumer.accept(message);
        }

        public void log(String message, Object... args) {
            if (logLevel == LogLevel.OFF) {
                return;
            }
            logBiConsumer.accept(message, args);
        }
    }

    private static class AdaptiveLoggerHolder {
        private static final Map<Class<?>, AdaptiveLogger> ADAPTIVE_LOGGERS = new ConcurrentHashMap<>();
    }
}
