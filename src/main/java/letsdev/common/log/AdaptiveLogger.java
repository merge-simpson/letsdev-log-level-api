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
        return AdaptiveLoggerHolder.ADAPTIVE_LOGGER_MAP.computeIfAbsent(
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

            // source & target compatibility: 1.8
            switch (logLevel) {
                case ALL:
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
        private static final Map<Class<?>, AdaptiveLogger> ADAPTIVE_LOGGER_MAP = new ConcurrentHashMap<>();
    }
}
