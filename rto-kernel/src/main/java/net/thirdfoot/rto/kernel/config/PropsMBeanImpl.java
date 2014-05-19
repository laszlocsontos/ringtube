package net.thirdfoot.rto.kernel.config;

import java.io.File;
import java.io.IOException;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import jodd.props.Props;
import jodd.props.PropsEntries;
import jodd.props.PropsEntry;

import net.thirdfoot.rto.kernel.exception.SystemException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lcsontos
 */
public class PropsMBeanImpl implements PropsMBean {

  public PropsMBeanImpl(File[] propertyFiles) {
    if (propertyFiles == null || propertyFiles.length < 1) {
      throw new IllegalArgumentException(
        "propertyFiles cannot be null or empty");
    }

    _propertyFiles = propertyFiles;

    ReadWriteLock readWriteLock = new ReentrantReadWriteLock(true);

    _readLock = readWriteLock.readLock();
    _writeLock = readWriteLock.writeLock();

    reload();
  }

  @Override
  public Map<String, String> getProperties() {
    Map<String, String> properties = new LinkedHashMap<String, String>();

    _readLock.lock();

    try {
      _props.get().extractProps(properties);
    }
    finally {
      _readLock.unlock();
    }

    return Collections.unmodifiableMap(properties);
  }

  @Override
  public Map<String, String> getPropertiesBySection(String section) {
    Map<String, String> properties = new LinkedHashMap<String, String>();

    PropsEntries propsEntries = null;

    _readLock.lock();

    try {
      propsEntries = _props.get().entries().section(section);
    }
    finally {
      _readLock.unlock();
    }

    Iterator<PropsEntry> propsIterator = propsEntries.iterator();

    while(propsIterator.hasNext()) {
      PropsEntry propsEntry = propsIterator.next();

      properties.put(propsEntry.getKey(), propsEntry.getValue());
    }

    return Collections.unmodifiableMap(properties);
  }

  @Override
  public String getProperty(String key) {
    _readLock.lock();

    try {
      return _props.get().getValue(key);
    }
    finally {
      _readLock.unlock();
    }
  }

  @Override
  public void setProperty(String key, String value) {
    _writeLock.lock();

    try {
      _props.get().setValue(key, value);
    }
    finally {
      _writeLock.unlock();
    }
  }

  @Override
  public void reload() {
    _writeLock.lock();

    try {
      Props props = load();

      props.entries();

      _props.set(props);
    }
    finally {
      _writeLock.unlock();
    }
  }

  protected Props load() {
    Props props = new Props();

    try {
      for (File propertyFile : _propertyFiles) {
        if (propertyFile == null) {
          continue;
        }

        _log.info("Loading property file " + propertyFile.getAbsolutePath());

        props.load(propertyFile);
      }
    }
    catch (IOException ioe) {
      throw new SystemException(ioe);
    }

    return props;
  }

  private static Logger _log = LoggerFactory.getLogger(PropsMBeanImpl.class);

  private final File[] _propertyFiles;

  private final AtomicReference<Props> _props = new AtomicReference<Props>();

  private final Lock _readLock;
  private final Lock _writeLock;
}