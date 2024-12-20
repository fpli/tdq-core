package com.ebay.sojourner.common.util;

import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteMap<K, V> {

  private final Map<K, V> map;
  private final ReadWriteLock lock = new ReentrantReadWriteLock();
  private final Lock readLock = lock.readLock();
  private final Lock writeLock = lock.writeLock();

  public ReadWriteMap(Map<K, V> map) {
    this.map = map;
  }

  public V put(K key, V value) {
    writeLock.lock();
    try {
      return map.put(key, value);
    } finally {
      writeLock.unlock();
    }
  }

  public void clear() {
    writeLock.lock();
    try {
      this.map.clear();
    } finally {
      writeLock.unlock();
    }
  }

  public V get(Object key) {
    readLock.lock();
    try {
      return map.get(key);
    } finally {
      readLock.unlock();
    }
  }
}
