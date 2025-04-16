package letsdev.common.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class AdaptiveLogger {
    private final Logger logger;
    private final Map<LogLevel, LevelFixedLogger> cachedLoggers = new ConcurrentHashMap<>();

    protected <T> AdaptiveLogger(Class<T> targetClass) {
        this(targetClass.getName());
    }

    protected <T> AdaptiveLogger(String name) {
        Objects.requireNonNull(name);
        this.logger = LoggerFactory.getLogger(name);
    }

    public static <T> AdaptiveLogger getLogger(Class<T> targetClass) {
        Objects.requireNonNull(targetClass);
        return getLogger(targetClass.getName());
    }

    public static <T> AdaptiveLogger getLogger(String name) {
        final String sourceName = isBlank(name) ? "unnamed" : name;
        return AdaptiveLoggerHolder.ADAPTIVE_LOGGER_MAP.computeIfAbsent(
                sourceName, (ignore) -> new AdaptiveLogger(sourceName)
        );
    }

    private static boolean isBlank(String str) {
        if (str == null || str.isEmpty()) {
            return true;
        }

        for (char ch: str.toCharArray()) {
            if (!Character.isWhitespace(ch)) {
                return false;
            }
        }

        return true;
    }

    public LevelFixedLogger with(LogLevel logLevel) {
        Objects.requireNonNull(logLevel);
        return cachedLoggers.computeIfAbsent(
                logLevel,
                (level) -> new LevelFixedLogger(logger, level)
        );
    }

    public LevelFixedLogger with(Level logLevel) {
        Objects.requireNonNull(logLevel);
        return with(LogLevel.valueOf(logLevel.name()));
    }

    private static class AdaptiveLoggerHolder {
        private static final Map<String, AdaptiveLogger> ADAPTIVE_LOGGER_MAP = new ConcurrentHashMap<>();
    }
}
