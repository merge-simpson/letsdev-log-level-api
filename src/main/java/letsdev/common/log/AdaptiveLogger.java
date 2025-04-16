package letsdev.common.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import java.util.Objects;

public class AdaptiveLogger {
    private final Logger logger;

    protected AdaptiveLogger(Class<?> targetClass) {
        this(targetClass.getName());
    }

    protected AdaptiveLogger(String name) {
        Objects.requireNonNull(name);
        this.logger = LoggerFactory.getLogger(name);
    }

    public static AdaptiveLogger getLogger(Class<?> targetClass) {
        return CachedAdaptiveLogger.getLogger(targetClass.getName());
    }

    public static AdaptiveLogger getLogger(String name) {
        return CachedAdaptiveLogger.getLogger(name);
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
        return new LevelFixedLogger(logger, logLevel);
    }

    public LevelFixedLogger with(Level logLevel) {
        Objects.requireNonNull(logLevel);
        return with(LogLevel.valueOf(logLevel.name()));
    }
}
