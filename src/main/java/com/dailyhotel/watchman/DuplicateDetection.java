package com.dailyhotel.watchman;

import java.lang.annotation.*;
import java.time.Duration;

/**
 * Created by tywin on 11/11/2016.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface DuplicateDetection {
    int ttlInSecond() default 60;
    int threshold() default 1;
}