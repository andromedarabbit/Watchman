package com.dailyhotel.watchman;

import com.dailyhotel.watchman.exception.DuplicateDectectedException;
import com.dailyhotel.watchman.testsupport.CacheInvalidationRule;
import com.dailyhotel.watchman.testsupport.MyData;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.time.Duration;

/**
 * Created by tywin on 11/11/2016.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DuplicateDetectorTest {
    @Inject
    @Rule
    public CacheInvalidationRule cacheInvalidationRule;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Inject
    private CacheClient cacheClient;

    private MethodInvocation getInvocation() {
        return getInvocation(new Object[0]);
    }

    private MethodInvocation getInvocation(Object... arguments) {
        return new MethodInvocation() {
            @Override
            public Method getMethod() {
                return this.getClass().getDeclaredMethods()[0];
            }

            @Override
            public Object[] getArguments() {
                return arguments;
            }

            @Override
            public Object proceed() throws Throwable {
                return null;
            }

            @Override
            public Object getThis() {
                return null;
            }

            @Override
            public AccessibleObject getStaticPart() {
                return null;
            }
        };
    }

    @Test
    public void allowedUnderThreshold() throws Exception {

        final MethodInvocation invocation = getInvocation("allowedUnderThreshold");
        final int threshold = 2;
        final Duration ttl = Duration.ofSeconds(10);

        DuplicateDetector detector = new DuplicateDetector(cacheClient);

        for (int i = 0; i < threshold - 1; i++) {
            detector.detect(invocation, ttl.getSeconds(), threshold);
        }
    }


    @Test
    public void duplicateDetected() throws Exception {
        thrown.expect(DuplicateDectectedException.class);

        final MethodInvocation invocation = getInvocation();
        final int threshold = 1;
        final Duration ttl = Duration.ofSeconds(10);

        DuplicateDetector detector = new DuplicateDetector(cacheClient);

        for (int i = 0; i < threshold + 1; i++) {
            detector.detect(invocation, ttl.getSeconds(), threshold);
        }
    }

    @Inject
    private MyData data;

    @Test
    public void annotationSupport() {
        thrown.expect(DuplicateDectectedException.class);

        final int threshold = 1;

        for (int i = 0; i < threshold + 1; i++) {
            data.setName("test");
        }
    }

}