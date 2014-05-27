package net.thirdfoot.rto.kernel.config;

import java.io.IOException;
import java.io.InputStream;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import jodd.io.StreamUtil;
import jodd.props.Props;
import jodd.props.PropsEntries;
import jodd.props.PropsEntry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.core.env.PropertySource;

/**
 * @author lcsontos
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class PropsBeanImpl extends PropertySource implements PropsBean {

  PropsBeanImpl(InputStream[] propertyInputStreams) {
    super(PropsBeanImpl.class.getName(), new Object());

    if (propertyInputStreams == null || propertyInputStreams.length < 1) {
      throw new IllegalArgumentException(
        "propertyInputStreams cannot be null or empty");
    }

    _propertyInputStreams = propertyInputStreams;

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
      _props.extractProps(properties);
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
      propsEntries = _props.entries().section(section);
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
      return _props.getValue(key);
    }
    finally {
      _readLock.unlock();
    }
  }

  @Override
  public void setProperty(String key, String value) {
    _writeLock.lock();

    try {
      _props.setValue(key, value);
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

      _props = props;
    }
    finally {
      _writeLock.unlock();
    }
  }

  protected void doLoad(Props props, InputStream propertyInputStream) {
    try {
      props.load(propertyInputStream);
    }
    catch (IOException ioe) {
      _log.error(ioe.getMessage(), ioe);
    }
    finally {
      StreamUtil.close(propertyInputStream);
    }
  }

  protected Props load() {
    Props props = new Props();

    // Load system properties
    props.loadSystemProperties("env");

    int streamIndex = 0;

    for (InputStream propertyInputStream : _propertyInputStreams) {
      if (propertyInputStream == null) {
        if (_log.isWarnEnabled()) {
          _log.warn("Skipping property input stream #" + (streamIndex++));
        }

        continue;
      }

      doLoad(props, propertyInputStream);
    }

    return props;
  }

  private static Logger _log = LoggerFactory.getLogger(PropsBeanImpl.class);

  private final InputStream[] _propertyInputStreams;

  private final Lock _readLock;
  private final Lock _writeLock;

  private volatile Props _props;

}