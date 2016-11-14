package com.dailyhotel.watchman.testsupport;

import org.junit.rules.ExternalResource;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * Created by tywin on 14/11/2016.
 */
@Component
public class CacheInvalidationRule extends ExternalResource {

    @Inject
    private CacheManager cacheManager;

    @Override
    protected void before() throws Throwable {
        clean();
    }

    @Override
    protected void after() {
        clean();
    }

    public void clean() {
        cacheManager.getCacheNames().stream().   // gets the name of all caches as a stream
                map(cacheManager::getCache).     // map the cache names to a stream of Cache:s
                forEach(Cache::clear);           // and clear() them
    }

}
