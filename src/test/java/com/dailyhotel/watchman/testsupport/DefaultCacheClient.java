package com.dailyhotel.watchman.testsupport;

import com.dailyhotel.watchman.CacheClient;
import com.dailyhotel.watchman.MethodCall;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.concurrent.TimeUnit;

/**
 * Created by tywin on 15/11/2016.
 */
@Component
public class DefaultCacheClient implements CacheClient {
    private RedisTemplate<String, MethodCall> redisTemplate;

    @Inject
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
