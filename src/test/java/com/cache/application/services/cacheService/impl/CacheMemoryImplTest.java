package com.cache.application.services.cacheService.impl;

import com.cache.application.entities.Customer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class CacheMemoryImplTest {

    @MockBean
    private Map<Object, Object> cacheMap = new HashMap<>();

    private Integer cacheMapSize;

    private String strategy;

    private static final Long key1 = Long.valueOf(1);
    private static final Long key2 = Long.valueOf(2);
    private static final Long key3 = Long.valueOf(3);
    private static final Long key4 = Long.valueOf(4);
    private static final Customer value1 = new Customer((long) 1, "Diluni", "923234300V", "Kurunegala", "female");
    private static final Customer value2 = new Customer((long) 2, "Chathurangi", "913234300V", "Kadawatha", "female");
    private static final Customer value3 = new Customer((long) 3, "Shanika", "933234300V", "Nugegoda", "female");
    private static final Customer value4 = new Customer((long) 4, "Mayumi", "903234300V", "Colombo", "female");

    @Before
    public void setUp() throws Exception {
        cacheMapSize = 4;
        strategy = "LRU";
        cacheMap.put(key1, value1);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testPutCache() {
        Map<Object, Object> testMap = new HashMap<>();
        List<Customer> customers = new ArrayList<>();

        customers.add(value1);
        customers.add(value2);
        customers.add(value3);
        customers.add(value4);

        Map<Object, Object> dataMap = customers.stream()
                .collect(Collectors.toMap(Customer::getId, customer -> customer));

        testMap.put(key1, dataMap.entrySet().stream()
                .filter(mapElement -> mapElement.getKey() == key1)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
        assertEquals(cacheMap, testMap);
    }

    @Test
    public void testGetCache() {
        Map<Object, Object> map = new HashMap();
        map.put(key1, value1);

        testCheckCache();

        assertEquals(map, cacheMap.entrySet().stream()
                .filter(mapElement -> mapElement.getKey() == key1)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
    }

    @Test
    public void testCheckCache() {
        if (!cacheMap.containsKey(key1)) testPutCache();
    }

    @Test
    public void testIsCacheMapFull() {
        assertEquals(false, ((cacheMap.size() == cacheMapSize) ? true : false));
    }

    @Test
    public void testClearAllCache() {
        cacheMap.clear();
        assertEquals(0, cacheMap.size());
    }

    @Test
    public void testRemoveCache() {
        cacheMap.remove(key1);
    }

    @Test
    public void testGetKeyToRemove() {
    }

}