package com.search.wiki.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class Cache {
    private final Map<String, CacheEntry> cacheMap;
    private final int maxSize;
    private final ReentrantReadWriteLock lock;
    private static final Logger logger = Logger.getLogger(Cache.class.getName());

    private static final int DEFAULT_MAX_SIZE = 1000;
    private static final int MAX_FREQUENCY_VALUE = Integer.MAX_VALUE;

    @Autowired
    public Cache() {
        this(DEFAULT_MAX_SIZE);
    }

    public Cache(int maxSize) {
        this.cacheMap = new LinkedHashMap<>();
        this.maxSize = maxSize;
        this.lock = new ReentrantReadWriteLock();
    }

    public void put(String key, Object value) {
        lock.writeLock().lock();
        try {
            if (cacheMap.size() >= maxSize) {
                String lfuKey = findLFUKey();
                if (lfuKey != null) {
                    cacheMap.remove(lfuKey);
                    logger.log(Level.INFO, () -> String.format("Cache evicted item with key: %s", lfuKey));
                }
            }
            cacheMap.put(key, new CacheEntry(value));
            logger.log(Level.INFO, () -> String.format("Added item to cache with key: %s", key));
        } finally {
            lock.writeLock().unlock();
        }
    }


    public Object get(String key) {
        lock.readLock().lock();
        try {
            CacheEntry entry = cacheMap.get(key);
            if (entry != null) {
                entry.frequency++;
                logger.log(Level.INFO, () -> String.format("Cache hit for key: %s", key));
                return entry.value;
            }
            logger.log(Level.INFO, () -> String.format("Cache miss for key: %s", key));
            return null;
        } finally {
            lock.readLock().unlock();
        }
    }

    public void remove(String key) {
        lock.writeLock().lock();
        try {
            cacheMap.remove(key);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public boolean containsKey(String key) {
        lock.readLock().lock();
        try {
            return cacheMap.containsKey(key);
        } finally {
            lock.readLock().unlock();
        }
    }

    private String findLFUKey() {
        String lfuKey = null;
        int minFrequency = MAX_FREQUENCY_VALUE;
        for (Map.Entry<String, CacheEntry> entry : cacheMap.entrySet()) {
            if (entry.getValue().frequency < minFrequency) {
                lfuKey = entry.getKey();
                minFrequency = entry.getValue().frequency;
            }
        }
        return lfuKey;
    }

    private static class CacheEntry {
        private final Object value;
        private int frequency;

        private CacheEntry(Object value) {
            this.value = value;
            this.frequency = 1;
        }
    }

    public Set<String> getCacheKeysStartingWith(String prefix) {
        lock.readLock().lock();
        try {
            Set<String> keysStartingWithPrefix = new HashSet<>();
            for (String key : cacheMap.keySet()) {
                if (key.startsWith(prefix)) {
                    keysStartingWithPrefix.add(key);
                }
            }
            return keysStartingWithPrefix;
        } finally {
            lock.readLock().unlock();
        }
    }

}
