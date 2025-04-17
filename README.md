# Download

Gradle(Kotlin):

`build.gradle.kts`에 다음 항목을 작성합니다.

1. `repositories`에 다음을 추가합니다. (`mavenCentral()`을 대신하지 않습니다.)
    ```kotlin
    maven { url = uri("https://jitpack.io") }
    ```

2. `dependencies`에 다음을 추가합니다.
    ```kotlin
    // NOTE 이 모듈은 SLF4J 구현체를 제공하지 않습니다.
    // 추가적인 SLF4J 구현체와 함께 작성해 주세요.
    implementation("com.github.merge-simpson:letsdev-log-level-api:0.1.2-rc2")
    ```

<br />

# Prerequisites

- Compatible Java Version: 1.8+
- An additional `SLF4J` implementation library is required.

<br />

# Features

**주요 제공 기능**

- (enum) `LogLevel` is independent of external libraries.
- (class) `AdaptiveLogger` is dependent on `SLF4J`. [see details](#adaptive-logger)

**함께 사용되는 기능**

- (class) `CachedAdaptiveLogger`는 자기 자신 및 자신의 로깅 레벨별 함수를 캐싱합니다.
- (class) `LevelFixedLogger`는 주로 `AdaptiveLogger` 인스턴스에 의해 추가로 생성되며, 동적으로 지정된 로깅 레벨의 로그 함수를 제공합니다. 
- (class) `Slf4jLevelUtil`은 `org.slf4j.event.Level` 타입과 `LogLevel` 타입의 열거상수 간 매핑을 제공합니다.

<a id="adaptive-logger"></a>

## AdaptiveLogger: 로그 레벨을 추상화하기 위한 로거

`AdaptiveLogger`는 사용할 로깅 함수의 레벨을 외부 설정으로 주입하거나, 필요에 따라 로깅 레벨을 동적으로 결정할 때 사용합니다.

<details open>
<summary>
   <strong>기본 사용 예시</strong> (캐싱되는 로깅 객체)
</summary>

```java
LevelFixedLogger logger = AdaptiveLogger.getLogger(CurrentClass.class)
    .with(LogLevel.INFO);

logger.log("Hello, world!");
```

</details>

<br />

<details open>
<summary>캐싱 되지 않는 로깅 객체 생성 예시 (<code>getLoggerNonCached</code>)</summary>

```java
LevelFixedLogger logger = AdaptiveLogger.getLoggerNonCached(CurrentClass.class)
    .with(LogLevel.INFO);

logger.log("Hello, world!");
```

</details>

<details open>
<summary>구체적인 용례</summary>

```java
public class Demo {
    
    LevelFixedLogger logger;
    
    public Demo(LogLevel logLevel) {
        // 로그 레벨을 주입해 사용합니다.
        logger = AdaptiveLogger.getLogger(Demo.class)
                .with(logLevel); // e.g. LogLevel.INFO
    }
    
    void example() {
        String name = "John";
        logger.log("Hello, My name is {}", name);
    }
}
```

</details>

<br />

# Releases

## Recent

[0.1.2 (rc2)](https://github.com/merge-simpson/letsdev-log-level-api/releases/tag/0.1.2-rc1)

- Compatibility: JDK 1.8+
- `CachedAdaptiveLogger` will always be cached when created.
- `AdaptiveLogger` can now create an uncached logger using `getLoggerNonCached`.
- `Slf4jLevelUtil` now transforms between `org.slf4j.event.Level` and `LogLevel`.
- `AdaptiveLogger` is no longer a final class.
- `AdaptiveLogger` no longer caches itself or its `LevelFixedLogger` instances.

## Old Releases

[0.1.1](https://github.com/merge-simpson/letsdev-log-level-api/releases/tag/0.1.1)

- Compatibility: 1.8+
- `AdaptiveLogger.getLogger(String name)`: `AdaptiveLogger` 추가 (supports unnamed class)
- `adaptiveLogger.with(org.slf4j.event.Level level)`: `LevelFixedLogger` 추가

[0.1.0](https://github.com/merge-simpson/letsdev-log-level-api/releases/tag/0.1.0)

- Compatibility: 17+
