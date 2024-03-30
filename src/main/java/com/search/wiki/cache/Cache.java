package com.search.wiki.cache;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** The type Cache. */
@Component
public class Cache {
  private final Map<String, CacheEntry> cacheMap;
  private final int maxSize;
  private final ReentrantReadWriteLock lock;
  private static final Logger LOGGER = Logger.getLogger(Cache.class.getName());

  private static final int DEFAULT_MAX_SIZE = 1000;
  private static final int MAX_FREQUENCY_VALUE = Integer.MAX_VALUE;

  /** Instantiates a new Cache. */
  @Autowired
  public Cache() {
    this(DEFAULT_MAX_SIZE);
  }

  /**
   * Constructor for cache.
   *
   * @param maxSize The maximum size of the cache.
   */
  public Cache(final int maxSize) {
    this.cacheMap = new ConcurrentHashMap<>();
    this.maxSize = DEFAULT_MAX_SIZE;
    this.lock = new ReentrantReadWriteLock();
  }

  /**
   * Puts an item into the cache.
   *
   * @param key The key associated with the item.
   * @param value The value to be cached.
   */
  public void put(final String key, final Object value) {
    lock.writeLock().lock();
    try {
      if (cacheMap.size() >= maxSize) {
        String lfuKey = findLfuKey();
        if (lfuKey != null) {
          cacheMap.remove(lfuKey);
          LOGGER.log(Level.INFO, () -> String.format("Cache evicted item with key: %s", lfuKey));
        }
      }
      cacheMap.put(key, new CacheEntry(value));
      LOGGER.log(Level.INFO, () -> String.format("Added item to cache with key: %s", key));
    } finally {
      lock.writeLock().unlock();
    }
  }

  /**
   * Retrieves an item from the cache.
   *
   * @param key The key associated with the item.
   * @return The cached item or null if not found.
   */
  public Object get(final String key) {
    lock.readLock().lock();
    try {
      CacheEntry entry = cacheMap.get(key);
      if (entry != null) {
        entry.frequency++;
        LOGGER.log(Level.INFO, () -> String.format("Cache hit for key: %s", key));
        return entry.value;
      }
      LOGGER.log(Level.INFO, () -> String.format("Cache miss for key: %s", key));
      return null;
    } finally {
      lock.readLock().unlock();
    }
  }

  /**
   * Removes an item from the cache.
   *
   * @param key The key associated with the item to be removed.
   */
  public void remove(final String key) {
    lock.writeLock().lock();
    try {
      cacheMap.remove(key);
    } finally {
      lock.writeLock().unlock();
    }
  }

  /**
   * Checks if the cache contains an item with the given key.
   *
   * @param key The key to be checked.
   * @return true if the cache contains the key, otherwise false.
   */
  public boolean containsKey(final String key) {
    lock.readLock().lock();
    try {
      return cacheMap.containsKey(key);
    } finally {
      lock.readLock().unlock();
    }
  }

  private String findLfuKey() {
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

  private static final class CacheEntry {
    private final Object value;
    private int frequency;

    private CacheEntry(final Object value) {
      this.value = value;
      this.frequency = 1;
    }
  }

  /**
   * Retrieves all cache keys that start with the given prefix.
   *
   * @param prefix The prefix to search for.
   * @return A set of cache keys starting with the given prefix.
   */
  public Set<String> getCacheKeysStartingWith(final String prefix) {
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
