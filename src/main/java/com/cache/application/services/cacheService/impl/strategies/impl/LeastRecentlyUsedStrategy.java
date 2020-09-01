package com.cache.application.services.cacheService.impl.strategies.impl;

import com.cache.application.services.cacheService.impl.strategies.CacheStrategyAbstractService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Diluni on 25/08/20.
 */
@Component("LRU")
public class LeastRecentlyUsedStrategy extends CacheStrategyAbstractService {
    private static final Logger logger = LoggerFactory.getLogger(LeastRecentlyUsedStrategy.class);

    //Least Recently Used Strategy Map
    public Map<Object, Long> lruMap = new HashMap<>();

    @Override
    public void putStrategyObject(Object k) {
        logger.info("Enter into putStrategyObject method in Least Recently Used Strategy Impl. LRU map: " + lruMap.toString());
        lruMap.put(k, System.nanoTime());
        logger.info("After updating or adding record into LRU map: " + lruMap.toString());
    }

    @Override
    public Object getEvictionKey() {
        logger.info("Enter into getEvictionKey method in Least Recently Used Strategy Impl. LRU map: " + lruMap.toString());
        Object key = sortMap(lruMap).entrySet().stream().findFirst().get().getKey();
        logger.info("Removing key based on Least Recently Used Strategy. Key: " + key.toString());
        lruMap.remove(key);
        logger.info("After removing record from LRU map: " + lruMap.toString());
        return key;
    }

}
