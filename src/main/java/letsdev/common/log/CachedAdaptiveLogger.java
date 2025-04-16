package letsdev.common.log;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class CachedAdaptiveLogger extends AdaptiveLogger {

    private final Map<LogLevel, LevelFixedLogger> cache = new ConcurrentHashMap<>();

    private CachedAdaptiveLogger(Class<?> clazz) {
        super(clazz);
    }

    private CachedAdaptiveLogger(String className) {
        super(className);
    }

    /*
     * 재밌는 발견:
     *   'getLogger(Class<?>)' in 'letsdev. common. log. CachedAdaptiveLogger' clashes with 'getLogger(Class<T>)' in 'letsdev. common. log. AdaptiveLogger'; both methods have same erasure, yet neither hides the other
     *
     * 이 오류는 수퍼타입의 static 메서드가 오히려 동일한 와일드카드 사용 시 발생하지 않으며, 제네릭 사용 시에만 발견되고 있음.
     */
    public static AdaptiveLogger getLogger(Class<?> clazz) {
        Objects.requireNonNull(clazz);
        return getLogger(clazz.getName());
    }

    public static AdaptiveLogger getLogger(String className) {
        // NOTE String::isBlank is since JDK 11
        final String sourceName = isBlank(className) ? "unnamed" : className;
        return Holder.MAP.computeIfAbsent(className, CachedAdaptiveLogger::new);
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

    @Override
    public LevelFixedLogger with(LogLevel level) {
        return cache.computeIfAbsent(level, super::with);
    }

    private static class Holder {
        private static final Map<String, AdaptiveLogger> MAP = new ConcurrentHashMap<>();
    }
}
