package com.cache.application.services.cacheService.impl.cacheImpl;

import com.cache.application.enumValues.enumValues;
import com.cache.application.services.cacheService.CacheAbstractService;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Diluni on 26/08/20.
 */
@Component("FileSystem")
public class CacheFileSystemImpl extends CacheAbstractService {
    private static final Logger logger = LoggerFactory.getLogger(CacheFileSystemImpl.class);

    @Override
    public Map<Object, Object> putCache(Object k) {
        Map<Object, Object> tempMap = new HashMap<>();

        logger.info("Enter into putCache method in Cache File System Impl. Key(k): " + k.toString());
        if (isCacheObjectFull()) {
            removeCache(getKeyToRemove());
        }
        logger.info("FileRepository folder is not full.");

        File tmpFile = null;
        try {
            tmpFile = Files.createTempFile(Paths.get("FileRepository"), k.toString() + "-", ".txt").toFile();
            logger.info("Successfully created temp file: " + tmpFile.toString());
        } catch (IOException e) {
            logger.info("Create temp file is failed: " + e.toString());
        }
        tempMap.putAll(customerApplicationService.getCustomerByCustomerId((Long) k));

        try {
            OutputStream fileOutputStream = new FileOutputStream(tmpFile);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            outputStreamWriter.write(tempMap.entrySet().stream()
                    .filter(mapElement -> mapElement.getKey() == k)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)).toString());
            outputStreamWriter.close();
            logger.info("Successfully wrote in temp file: " + tmpFile.toString());
        } catch (IOException e) {
            logger.info("Write into temp file is failed: " + e.toString());
        }
        return tempMap;
    }

    public Boolean checkCache(Object k) {
        logger.info("Enter into checkCache method in Cache File System Impl");
        return (getFile(k) != null ? Boolean.TRUE : Boolean.FALSE);
    }

    public Map<Object, Object> getCache(Object k) {
        logger.info("Enter into getCache method in Cache File System Impl");
        Map<Object, Object> cacheElement = new HashMap<>();

        if (!checkCache(k)) {
            cacheElement = putCache(k);
        } else {
            List<String> fileData = null;
            try {
                fileData = Files.readAllLines(Paths.get(getFile(k).toString()));
            } catch (IOException e) {
                logger.info("Read Lines of the file is failed: " + e.toString());
            }
            cacheElement.put(k, fileData.stream().map(Object::toString).collect(Collectors.joining("\n")));
        }

        if (strategy.equals(enumValues.Strategies.LFU.toString())) {
            leastFrequentlyUsedCacheStrategy.putStrategyObject(k);
        } else {
            leastRecentlyUsedCacheStrategy.putStrategyObject(k);
        }

        logger.info("Return from getCache method in Cache File System Impl: " + cacheElement.toString());
        return cacheElement;
    }

    public void removeCache(Object k) {
        logger.info("Enter into removeCache method in Cache File System Impl. Removing key from FileRepository folder. Key: " + k.toString());
        getFile(k).delete();
        logger.info("Successfully removed second level cache object");
    }

    public Boolean isCacheObjectFull() {
        Boolean isFull = (getDirectory().list().length == cacheMapMaxSize ? Boolean.TRUE : Boolean.FALSE);
        logger.info("Enter into isCacheObjectFull method in Cache File System Impl. Is FileRepository full: " + isFull);
        return isFull;
    }

    public void clearAllCache() {
        logger.info("Enter into clearAllCache method in Cache File System Impl");
        try {
            FileUtils.cleanDirectory(getDirectory());
            logger.info("Successfully cleaned second level cache folder");
        } catch (IOException e) {
            logger.info("Clear the directory is failed: " + e.toString());
        }
    }

    public File getDirectory() {
        logger.info("Enter into getDirectory method in Cache File System Impl");
        File dir = new File("FileRepository");
        if (!dir.isDirectory()) {
            dir.mkdir();
        }
        logger.info("Directory: " + dir.toString());
        return dir;
    }

    public File getFile(Object k) {
        logger.info("Enter into getFile method in Cache File System Impl");
        for (File file : getDirectory().listFiles()) {
            if (file.getName().startsWith(k.toString() + "-")) {
                logger.info("File is available: " + file.toString());
                return file;
            }
        }
        logger.info("File is not available");
        return null;
    }

}
