`master`: [![Build Status](https://travis-ci.org/DailyHotel/Watchman.svg?branch=master)](https://travis-ci.org/DailyHotel/Watchman)
`develop`: [![Build Status](https://travis-ci.org/DailyHotel/Watchman.svg?branch=develop)](https://travis-ci.org/DailyHotel/Watchman)

# Watchman
Simple java library to detect duplicate method calls. The following scenarios can be covered:

* To prevent the system from sending texts or e-mails with the same messages over and over again to the same user
* To prevent the system from approving the same orders multiple times

## Prerequisites

* [Spring Boot](https://projects.spring.io/spring-boot/) v1.4.2

## How to use Watchman

```java
@DuplicateDetection(ttlInSecond = 120, threshold = 1)
@KafkaListener(topicPattern = "texts")
public void send(String smsEvent) {
    this.sendText(smsEvent);
}
```

Very easy to use once you provide `CacheClient` implementation. For instance,

``` java
@Component
public class DefaultCacheClient implements CacheClient {
    private RedisTemplate<String, MethodCall> redisTemplate;

    public DefaultCacheClient(RedisTemplate<String, MethodCall> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public MethodCall get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void set(String key, MethodCall value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }
}
```

### How to get Watchman into your build

If you are using Gradle as your build tool,

1. Add the JitPack repository to your build file:
    ```gradle
    allprojects {
		repositories {
			...
			maven { url "https://jitpack.io" }
		}
	}
    ```
2. Add the dependency
    ```gradle
    dependencies {
		compile 'com.github.User:Repo:Tag'
	}
    ```

[Maven](https://maven.apache.org/), [SBT](http://www.scala-sbt.org/), and [Leiningen](http://leiningen.org/) are [supported as well](https://jitpack.io/#DailyHotel/Watchman).

## How to run tests

``` bash
docker run -p6379:6379 redis

gradle build
```
