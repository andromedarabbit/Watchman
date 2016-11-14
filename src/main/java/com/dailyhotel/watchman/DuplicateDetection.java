package com.dailyhotel.watchman;

import java.lang.annotation.*;

/**
 * Created by tywin on 11/11/2016.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
@Documented
public @interface DuplicateDetection {

}