package com.cache.application.services.cacheService.impl.cacheImpl;

import com.cache.application.enumValues.enumValues;
import com.cache.application.services.cacheService.CacheAbstractService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Diluni on 25/08/20.
 */
@Component("Memory")
public class CacheMemoryImpl extends CacheAbstractService {
    private static final Logger logger = LoggerFactory.getLogger(CacheMemoryImpl.class);

    //Cache Memory Map
    private Map<Object, Object> cacheMap = new HashMap<>();

    @Override
    public Map<Object, Object> putCache(Object k) {
        logger.info("Enter into putCache method in Cache Memory Impl. Key(k): " + k.toString());
        if (isCacheObjectFull()) {
            removeCache(getKeyToRemove());
        }
        logger.info("Memory cache map is not full.");
        Map newRecord = customerApplicationService.getCustomerByCustomerId((Long) k);
        cacheMap.putAll(newRecord);
        logger.info("Return into putCache in Cache Memory Impl. After adding the record into Memory cache map: " + cacheMap.toString());
        return cacheMap.entrySet().stream()
                .filter(mapElement -> mapElement.getKey() == k)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public Boolean checkCache(Object k) {
        logger.info("Enter into checkCache method in Cache Memory Impl. Is Memory cache map contain Key(k): " + cacheMap.containsKey(k));
        return (cacheMap.containsKey(k) ? Boolean.TRUE : Boolean.FALSE);
    }

    public Map<Object, Object> getCache(Object k) {
        logger.info("Enter into getCache method in Cache Memory Impl. Memory cache map: " + cacheMap.toString());
        Map<Object, Object> cacheElement = (!checkCache(k) ? putCache(k) : cacheMap.entrySet().stream()
                .filter(mapElement -> mapElement.getKey() == k)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));

        if (strategy.equals(enumValues.Strategies.LFU.toString())) {
            leastFrequentlyUsedCacheStrategy.putStrategyObject(k);
        } else {
            leastRecentlyUsedCacheStrategy.putStrategyObject(k);
        }

        logger.info("Return from getCache method in Cache Memory Impl Impl: " + cacheElement.toString());
        return cacheElement;
    }

    public void removeCache(Object k) {
        logger.info("Enter into removeCache method in Cache Memory Impl. Removing key from Memory cache map. Key: " + k.toString());
        cacheMap.remove(k);
    }

    public Boolean isCacheObjectFull() {
        Boolean isFull = (cacheMap.size() == cacheMapMaxSize ? Boolean.TRUE : Boolean.FALSE);
        logger.info("Enter into isCacheObjectFull method in Cache Memory Impl. Is Memory cache map full: " + isFull);
        return isFull;
    }

    public void clearAllCache() {
        logger.info("Enter into clearAllCache method in Cache Memory Impl. Current Memory cache map size: " + cacheMap.size());
        cacheMap.clear();
        logger.info("After clear Cache Memory. Memory cache map size: " + cacheMap.size());
    }

}
