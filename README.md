# Download

Gradle(Kotlin):

```kotlin
// `build.gradle.kts` 파일
repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") } // added
}

dependencies {
    implementation("com.github.merge-simpson:letsdev-log-level-api:0.1.0") // added
    // NOTE 이 모듈은 SLF4J 구현체를 제공하지 않습니다. 사용하는 SLF4J 구현체가 있어야 합니다.
}
```

# Features

- \<\<enum\>\> `LogLevel` (independent of other libraries)
- \<\<class\>\> `AdaptiveLogger` (dependent on `SLF4J`)

## LogLevel

`SLF4J` 등 주요 라이브러리가 제공하는 로그 레벨을 독립된 `enum` 타입으로 작성했습니다.

## AdaptiveLogger: 로그 레벨을 추상화하기 위한 로거

`AdaptiveLogger`는 로깅 레벨을 외부 설정으로 주입하거나, 중요도에 따라 로깅 레벨을 동적으로 결정할 때 사용합니다.

```java
public class Demo {
    AdaptiveLogger logger = AdaptiveLogger.getLogger(Demo.class);

    void example() {
        String name = "John";
        log(LogLevel.INFO, "Hello, My name is {}", name);
    }

    void log(LogLevel logLevel, String message, Object... args) {
        // 외부에서 로그 레벨을 주입해 사용합니다.
        logger.with(logLevel)
                .log(message, args);
    }
}
```
