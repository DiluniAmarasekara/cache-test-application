package com.cache.application.services.cacheService.impl.strategies;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Diluni on 25/08/20.
 */
public abstract class CacheStrategyAbstractService {
    private static final Logger logger = LoggerFactory.getLogger(CacheStrategyAbstractService.class);

    public CacheStrategyAbstractService() {
    }

    public abstract void putStrategyObject(Object key);

    public abstract Object getEvictionKey();

    public Map<Object, Long> sortMap(Map<Object, Long> map) {
        logger.info("Enter into sortMap method in Cache Strategy Abstract Service. Map: " + map.toString());
        return map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.naturalOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }

}
