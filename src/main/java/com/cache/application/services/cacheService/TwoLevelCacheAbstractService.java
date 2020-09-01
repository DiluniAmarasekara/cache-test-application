package com.cache.application.services.cacheService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Map;

/**
 * Created by Diluni on 26/08/20.
 */
public abstract class TwoLevelCacheAbstractService {
    private static final Logger logger = LoggerFactory.getLogger(TwoLevelCacheAbstractService.class);

    @Autowired
    @Qualifier("Memory")
    private CacheAbstractService firstLevelCache;

    @Autowired
    @Qualifier("FileSystem")
    private CacheAbstractService secondLevelCache;

    public Map<Object, Object> getCacheWithTwoLevel(Object k) {
        logger.info("Enter into getCacheWithTwoLevel method in Cache Two Level Impl. Key: " + k.toString());
        if (firstLevelCache.checkCache(k) || !firstLevelCache.isCacheObjectFull()) {
            if (secondLevelCache.checkCache(k)) {
                secondLevelCache.removeCache(k);
            }
            return firstLevelCache.getCache(k);
        } else if (secondLevelCache.checkCache(k) || !secondLevelCache.isCacheObjectFull()) {
            return secondLevelCache.getCache(k);
        } else {
            return firstLevelCache.putCache(k);
        }
    }

}
