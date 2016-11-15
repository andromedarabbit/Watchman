package com.dailyhotel.watchman;

import com.dailyhotel.watchman.exception.DuplicateDectectedException;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import org.aopalliance.intercept.MethodInvocation;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * Created by tywin on 11/11/2016.
 */
public class DuplicateDetector {

    private final CacheClient cacheClient;

    public DuplicateDetector(CacheClient cacheClient) {
        this.cacheClient = cacheClient;
    }

    public void detect(MethodInvocation invocation, Duration ttl, int thredshold) {
        final String signature = invocation.getMethod().toString();

        final Object[] arguments = invocation.getArguments();
        final String argumentsStr = Lists.newArrayList(invocation.getArguments()).toString();

        final String key = MoreObjects.toStringHelper(this)
                .add("signature", signature)
                .add("arguments", argumentsStr)
                .toString();

        final MethodCall oldRecord = cacheClient.get(key);
        final MethodCall newRecord = getMethodCall(oldRecord, signature, arguments, ttl, thredshold);
        try {
            if (newRecord.getCount() > thredshold) {
                throw new DuplicateDectectedException(newRecord);
            }
        } finally {
            cacheClient.set(key, newRecord, ttl.getSeconds(), TimeUnit.SECONDS);
        }
    }

    private MethodCall getMethodCall(final MethodCall old, String signature, Object[] arguments, Duration ttl, int thredshold) {
        if (old == null) {
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
