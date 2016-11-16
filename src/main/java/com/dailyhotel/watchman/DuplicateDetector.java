package com.dailyhotel.watchman;

import com.dailyhotel.watchman.exception.DuplicateDectectedException;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import org.aopalliance.intercept.MethodInvocation;

import java.util.concurrent.TimeUnit;

/**
 * Created by tywin on 11/11/2016.
 */
public class DuplicateDetector {

    private final CacheClient cacheClient;

    public DuplicateDetector(CacheClient cacheClient) {
        this.cacheClient = cacheClient;
    }

    public void detect(MethodInvocation invocation, long ttlInSecond, int threshold) {
        final String signature = invocation.getMethod().toString();

        final Object[] arguments = invocation.getArguments();
        final String argumentsStr = Lists.newArrayList(invocation.getArguments()).toString();

        final String key = MoreObjects.toStringHelper(this)
                .add("signature", signature)
                .add("arguments", argumentsStr)
                .toString();

        final MethodCall oldRecord = cacheClient.get(key);
        final MethodCall newRecord = getMethodCall(oldRecord, signature, arguments, ttlInSecond, threshold);
        try {
            if (newRecord.getCount() > threshold) {
                throw new DuplicateDectectedException(newRecord);
            }
        } finally {
            cacheClient.set(key, newRecord, ttlInSecond, TimeUnit.SECONDS);
        }
    }

    private MethodCall getMethodCall(final MethodCall old, String signature, Object[] arguments, long ttlInSecond, int threshold) {
        if (old == null) {
            return new MethodCall()
                    .setSignature(signature)
                    .setArguments(Lists.newArrayList(arguments))
                    .setTtlInSecond(ttlInSecond)
                    .setThreshold(threshold)
                    .setCount(1)
                    ;
        }

        return new MethodCall()
                .setSignature(signature)
                .setArguments(Lists.newArrayList(arguments))
                .setTtlInSecond(ttlInSecond)
                .setThreshold(threshold)
                .setCount(old.getCount() + 1)
                ;
    }

}
