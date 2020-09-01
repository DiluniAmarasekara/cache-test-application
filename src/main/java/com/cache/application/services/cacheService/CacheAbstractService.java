package com.cache.application.services.cacheService;

import com.cache.application.services.customerService.CustomerApplicationService;
import com.cache.application.enumValues.enumValues;
import com.cache.application.services.cacheService.impl.strategies.CacheStrategyAbstractService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Map;

/**
 * Created by Diluni on 25/08/20.
 */
public abstract class CacheAbstractService {
    private static final Logger logger = LoggerFactory.getLogger(CacheAbstractService.class);

    //Max size of cache map
    protected Integer cacheMapMaxSize;

    //Eviction strategy type
    protected String strategy;

    @Autowired
    @Qualifier("LFU")
    protected CacheStrategyAbstractService leastFrequentlyUsedCacheStrategy;

    @Autowired
    @Qualifier("LRU")
    protected CacheStrategyAbstractService leastRecentlyUsedCacheStrategy;

    @Autowired
    protected CustomerApplicationService customerApplicationService;

    public CacheAbstractService() {
    }

    public void configureCache(Integer cacheMapMaxSize, String strategy) {
        this.cacheMapMaxSize = cacheMapMaxSize;
        this.strategy = strategy;
    }

    public abstract Map<Object, Object> putCache(Object k);

    public abstract Boolean checkCache(Object k);

    public abstract Map<Object, Object> getCache(Object k);

    public abstract void removeCache(Object k);

    public Object getKeyToRemove() {
        logger.info("Enter into getKeyToRemove method in cache abstract service. Strategy: " + strategy);
        return (strategy.equals(enumValues.Strategies.LFU.toString()) ? leastFrequentlyUsedCacheStrategy.getEvictionKey() : leastRecentlyUsedCacheStrategy.getEvictionKey());
    }

    public abstract Boolean isCacheObjectFull();

    public abstract void clearAllCache();

}
