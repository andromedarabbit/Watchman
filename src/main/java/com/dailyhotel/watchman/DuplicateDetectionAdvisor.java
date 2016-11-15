package com.dailyhotel.watchman;

import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.lang.reflect.Method;

/**
 * Created by tywin on 14/11/2016.
 */
@Component
public class DuplicateDetectionAdvisor extends AbstractPointcutAdvisor {

    private static final long serialVersionUID = 1L;

    @Inject
    private CacheClient redisTemplate;

    private final StaticMethodMatcherPointcut pointcut = new
            StaticMethodMatcherPointcut() {
                @Override
                public boolean matches(Method method, Class<?> targetClass) {
                    return method.isAnnotationPresent(DuplicateDetection.class);
                }
            };

    @Override
    public Pointcut getPointcut() {
        return this.pointcut;
    }

    @Override
    public Advice getAdvice() {
        return new DuplicateDetectionMethodInterceptor(redisTemplate);
    }
}
