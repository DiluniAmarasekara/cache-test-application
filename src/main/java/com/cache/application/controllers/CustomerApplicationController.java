package com.cache.application.controllers;

import com.cache.application.services.cacheService.CacheAbstractService;
import com.cache.application.services.cacheService.TwoLevelCacheAbstractService;
import com.cache.application.services.customerService.CustomerApplicationService;
import com.cache.application.entities.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by Diluni on 25/08/20.
 */
@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class CustomerApplicationController {
    private static final Logger logger = LoggerFactory.getLogger(CustomerApplicationController.class);

    @Autowired
    @Qualifier("Memory")
    private CacheAbstractService cacheMemoryService;

    @Autowired
    @Qualifier("FileSystem")
    private CacheAbstractService cacheFileSystemService;

    @Autowired
    private TwoLevelCacheAbstractService twoLevelCacheService;

    @Autowired
    private CustomerApplicationService customerApplicationService;

    /**
     * GET rest API for memory cache configuration.
     * Base on the strategy this method accesses memory cache and retrieves data accordingly.
     *
     * @param id
     * @return Map<Object, Object>
     */
    @RequestMapping(value = "/memoryCache", method = RequestMethod.GET)
    public Map<Object, Object> getCustomerByCustomerId(@RequestParam Long id) {
        logger.info("Enter into getCustomerByCustomerId controller method. Example of memory cache.");
        Map<Object, Object> customer = cacheMemoryService.getCache(id);
        return customer;
    }

    /**
     * GET rest API for cache file system configuration.
     * Base on the strategy this method accesses file system cache and retrieves data accordingly.
     *
     * @param id
     * @return Map<Object, Object>
     */
    @RequestMapping(value = "/twoLevelCache", method = RequestMethod.GET)
    public Map<Object, Object> getCustomerWithTwoLevelCache(@RequestParam Long id) {
        logger.info("Enter into getCustomerWithTwoLevelCache controller method. Example of two level cache.");
        return twoLevelCacheService.getCacheWithTwoLevel(id);
    }

    /**
     * DELETE rest API for clear all the cache level 1 and level 2.
     */
    @RequestMapping(value = "/clearAllCache", method = RequestMethod.DELETE)
    public void clearAllCache() {
        cacheMemoryService.clearAllCache();
        cacheFileSystemService.clearAllCache();
    }

    /**
     * POST rest API call for add Customer.
     *
     * @param customer object
     * @return status
     */
    @RequestMapping(value = "/addCustomer", method = RequestMethod.POST)
    public HttpStatus addCustomer(@RequestBody Customer customer) {
        return customerApplicationService.addCustomer(customer);
    }

}
