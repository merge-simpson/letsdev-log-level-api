package letsdev.common.log;

import org.slf4j.event.Level;

import java.util.EnumSet;

public final class Slf4jLevelUtil {
    private Slf4jLevelUtil() {}

    public static LogLevel toLogLevel(Level level) {
        assert EnumSet.of(
                Level.TRACE,
                Level.DEBUG,
                Level.INFO,
                Level.WARN,
                Level.ERROR
        ).equals(EnumSet.allOf(Level.class)) : "org.slf4j.event.Level 열거상수 목록이 업데이트되었습니다. 점검이 필요합니다.";

        switch (level) {
            case TRACE:
                return LogLevel.TRACE;
            case DEBUG:
                return LogLevel.DEBUG;
            case INFO:
                return LogLevel.INFO;
            case WARN:
                return LogLevel.WARN;
            case ERROR:
                return LogLevel.ERROR;
            default:
                return LogLevel.OFF;
        }
    }

    public static Level toSlf4jLevel(LogLevel logLevel) {
        assert EnumSet.of(
                LogLevel.TRACE,
                LogLevel.DEBUG,
                LogLevel.INFO,
                LogLevel.WARN,
                LogLevel.ERROR,
                LogLevel.OFF
        ).equals(EnumSet.allOf(LogLevel.class)) : "LogLevel 열거상수 목록이 업데이트되었습니다. 점검이 필요합니다.";

        switch (logLevel) {
            case TRACE:
                return Level.TRACE;
            case DEBUG:
                return Level.DEBUG;
            case INFO:
                return Level.INFO;
            case WARN:
                return Level.WARN;
            case ERROR:
                return Level.ERROR;
            case OFF:
                return null;
            default:
                throw new AssertionError("LogLevel 열거상수 목록이 업데이트되었습니다. 점검이 필요합니다.");
        }
    }
}