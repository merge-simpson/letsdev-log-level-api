# Download

Gradle(Kotlin):

`build.gradle.kts`에 다음 항목을 작성합니다.

1. `repositories`에 다음을 추가합니다. (`mavenCentral()`을 대신하지 않습니다.)
    ```kotlin
    maven { url = uri("https://jitpack.io") }
    ```

2. `dependencies`에 다음을 추가합니다.
    ```kotlin
    // NOTE 이 모듈은 SLF4J 구현체를 제공하지 않습니다. 사용하는 SLF4J 구현체가 있어야 합니다.
    implementation("com.github.merge-simpson:letsdev-log-level-api:0.1.1")
    ```

# Prerequisites

- Java Version: 1.8+

# Features

- \<\<enum\>\> `LogLevel` (independent of other libraries)
- \<\<class\>\> `AdaptiveLogger` (dependent on `SLF4J`)

## LogLevel

`SLF4J` 등 주요 라이브러리가 제공하는 로그 레벨을 독립된 `enum` 타입으로 작성했습니다.

## AdaptiveLogger: 로그 레벨을 추상화하기 위한 로거

`AdaptiveLogger`는 로깅 레벨을 외부 설정으로 주입하거나, 중요도에 따라 로깅 레벨을 동적으로 결정할 때 사용합니다.

```java
public class Demo {
    LevelFixedLogger logger;
    public Demo(LogLevel logLevel) {
        // 외부에서 로그 레벨을 주입해 사용합니다.
        logger = AdaptiveLogger
                .getLogger(Demo.class)
                .with(logLevel); // e.g. LogLevel.INFO
    }
    
    void example() {
        String name = "John";
        logger.log("Hello, My name is {}", name);
    }
}
```