package com.dailyhotel.watchman;

import java.util.concurrent.TimeUnit;

/**
 * Created by tywin on 15/11/2016.
 */
public interface CacheClient {
    MethodCall get(String key);
    void set(String key, MethodCall value, long timeout, TimeUnit unit);
}
