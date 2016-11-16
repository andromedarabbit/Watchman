package com.dailyhotel.watchman;


import com.dailyhotel.watchman.exception.DuplicateDectectedException;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

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
        final int threshold = annotation.threshold();
        final int ttlInSecond = annotation.ttlInSecond();

        try {
            detector.detect(invocation, ttlInSecond, threshold);
        } catch (DuplicateDectectedException detectionEx) {
            throw detectionEx;
        }

        return invocation.proceed();
    }
}
