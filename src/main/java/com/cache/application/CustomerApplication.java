package com.cache.application;

import com.cache.application.services.cacheService.CacheAbstractService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
public class CustomerApplication implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(CustomerApplication.class);

    @Autowired
    @Qualifier("Memory")
    private CacheAbstractService cacheMemoryService;

    @Autowired
    @Qualifier("FileSystem")
    private CacheAbstractService cacheFileSystemService;

    @Override
    public void run(String... args) throws Exception {
        Scanner myObj = new Scanner(System.in);

        System.out.println("Enter cache map max size and eviction strategy(LFU or LRU) -> format: maxSize,strategy -> Ex: 2,LRU :");
        String input = myObj.nextLine();

        Integer maxsize = Integer.valueOf(input.substring(0, input.indexOf(",")));
        String strategy = input.substring(input.indexOf(",") + 1);

        cacheMemoryService.configureCache(maxsize, strategy);
        cacheFileSystemService.configureCache(maxsize, strategy);

        logger.info("Cache map max size: " + maxsize.toString());
        logger.info("Eviction strategy: " + strategy);
    }

    public static void main(String[] args) {
        SpringApplication.run(CustomerApplication.class, args);
    }

}
