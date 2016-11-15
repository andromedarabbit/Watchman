package com.dailyhotel.watchman;


import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.time.Duration;

/**
 * Created by tywin on 14/11/2016.
 */
public class DuplicateDetectionMethodInterceptor implements MethodInterceptor {

    private final DuplicateDetector detector;

    public DuplicateDetectionMethodInterceptor(CacheClient cacheClient) {
        detector = new DuplicateDetector(cacheClient);
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        final DuplicateDetection annotation = invocation.getMethod().getAnnotation(DuplicateDetection.class);
        final int thredshold = annotation.thredshold();
        final int ttlInSecond = annotation.ttlInSecond();

        try {
            return invocation.proceed();
        } finally {
            detector.detect(invocation, Duration.ofSeconds(ttlInSecond), thredshold);
        }
    }
}
