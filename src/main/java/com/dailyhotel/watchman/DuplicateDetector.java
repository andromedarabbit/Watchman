package com.dailyhotel.watchman;

import com.dailyhotel.watchman.exception.DuplicateDectectedException;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;

import javax.inject.Inject;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * Created by tywin on 11/11/2016.
 */
public class DuplicateDetector {

    private final RedisTemplate<String, MethodCall> redisTemplate;

    public DuplicateDetector(RedisTemplate<String, MethodCall> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void detect(MethodInvocation invocation, Duration ttl, int thredshold) {
        final String signature = invocation.getMethod().toString();

        final Object[] arguments = invocation.getArguments();
        final String argumentsStr = MoreObjects.toStringHelper(invocation.getArguments()).toString();

        final String key = MoreObjects.toStringHelper(this)
                .add("signature", signature)
                .add("arguments", argumentsStr)
                .toString()
                ;


        ValueOperations<String, MethodCall> ops = redisTemplate.opsForValue();
        final MethodCall oldRecord = ops.get(key);
        final MethodCall newRecord = getMethodCall(oldRecord, signature, arguments, ttl, thredshold);
        try {
            if (oldRecord != null && oldRecord.getThredshold() >= thredshold) {
                throw new DuplicateDectectedException(newRecord);
            }
        } finally {
            ops.set(key, newRecord, ttl.getSeconds(), TimeUnit.SECONDS);
        }
    }

    private MethodCall getMethodCall(final MethodCall old, String signature, Object[] arguments, Duration ttl, int thredshold) {
        if(old == null) {
            return new MethodCall()
                    .setSignature(signature)
                    .setArguments(Lists.newArrayList(arguments))
                    .setTtl(ttl)
                    .setThredshold(thredshold)
                    .setCount(1)
                    ;
        }

        return new MethodCall()
                .setSignature(signature)
                .setArguments(Lists.newArrayList(arguments))
                .setTtl(ttl)
                .setThredshold(thredshold)
                .setCount(old.getCount() + 1)
                ;
    }

}
