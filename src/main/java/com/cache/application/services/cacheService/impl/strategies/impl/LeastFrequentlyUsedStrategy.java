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
@Component("LFU")
public class LeastFrequentlyUsedStrategy extends CacheStrategyAbstractService {
    private static final Logger logger = LoggerFactory.getLogger(LeastFrequentlyUsedStrategy.class);

    //Least Frequently Used Strategy Map
    public Map<Object, Long> lfuMap = new HashMap<>();

    @Override
    public void putStrategyObject(Object k) {
        logger.info("Enter into putStrategyObject method in Least Frequently Used Strategy Impl. LFU map: " + lfuMap.toString());
        Long frequency = Long.valueOf(1);
        if (lfuMap.containsKey(k)) {
            frequency = lfuMap.get(k) + 1;
        }
        lfuMap.put(k, frequency);
        logger.info("After increasing or adding record into LFU map: " + lfuMap.toString());
    }

    @Override
    public Object getEvictionKey() {
        logger.info("Enter into getEvictionKey method in Least Frequently Used Strategy Impl. LFU map: " + lfuMap.toString());
        Object key = sortMap(lfuMap).entrySet().stream().findFirst().get().getKey();
        logger.info("Removing key based on Least Frequently Used Strategy. Key: " + key.toString());
        lfuMap.remove(key);
        logger.info("After removing record from LFU map: " + lfuMap.toString());
        return key;
    }

}
