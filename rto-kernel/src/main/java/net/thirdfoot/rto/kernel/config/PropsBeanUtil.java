package net.thirdfoot.rto.kernel.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.management.JMException;
import javax.management.ObjectName;

import jodd.typeconverter.Convert;
import jodd.util.ClassLoaderUtil;
import jodd.util.StringBand;
import jodd.util.StringUtil;

import net.thirdfoot.rto.kernel.util.JMXUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.core.env.PropertySource;

/**
 * @author lcsontos
 */
public class PropsBeanUtil {

  public static PropertySource<?> getPropertySource() {
    return (PropertySource<?>)getInstance();
  }

  public static PropsBean getInstance() {
    if (_initialized.compareAndSet(false, true)) {
      InputStream[] propertyInputStreams = new InputStream[2];

      propertyInputStreams[0] = _getDefaultPropertyInputStream();
      propertyInputStreams[1] = _getExternalPropertyInputStream();

      _instance = new PropsBeanImpl(propertyInputStreams);
    }

    return _instance;
  }

  public static Boolean getBoolean(PropsKey key) {
    return getBoolean(key, false);
  }

  public static Boolean getBoolean(String key) {
    return getBoolean(key, false);
  }

  public static Boolean getBoolean(PropsKey key, boolean defaultValue) {
    return getBoolean(key.getKey(), defaultValue);
  }

  public static Boolean getBoolean(String key, boolean defaultValue) {
    Boolean value = Convert.toBoolean(getString(key));

    if (value != null) {
      return value;
    }

    return defaultValue;
  }
  public static boolean[] getBooleanArray(PropsKey key) {
    return getBooleanArray(key, null);
  }

  public static boolean[] getBooleanArray(String key) {
    return getBooleanArray(key, null);
  }

  public static boolean[] getBooleanArray(
    PropsKey key, boolean[] defaultValue) {

    return getBooleanArray(key.getKey(), defaultValue);
  }

  public static boolean[] getBooleanArray(
    String key, boolean[] defaultValue) {

    boolean[] value = Convert.toBooleanArray(getString(key));

    if (value != null) {
      return value;
    }

    return defaultValue;
  }

  public static Integer getInteger(PropsKey key) {
    return getInteger(key, 0);
  }

  public static Integer getInteger(String key) {
    return getInteger(key, 0);
  }

  public static Integer getInteger(PropsKey key, int defaultValue) {
    return getInteger(key.getKey(), defaultValue);
  }

  public static Integer getInteger(String key, int defaultValue) {
    Integer value = Convert.toInteger(getString(key));

    if (value != null) {
      return value;
    }

    return defaultValue;
  }

  public static int[] getIntegerArray(PropsKey key) {
    return getIntegerArray(key, null);
  }

  public static int[] getIntegerArray(String key) {
    return getIntegerArray(key, null);
  }

  public static int[] getIntegerArray(PropsKey key, int[] defaultValue) {
    return getIntegerArray(key.getKey(), defaultValue);
  }

  public static int[] getIntegerArray(String key, int[] defaultValue) {
    int[] value = Convert.toIntegerArray(getString(key));

    if (value != null) {
      return value;
    }

    return defaultValue;
  }

  public static Map<String, String> getSection(String section) {
    return getInstance().getPropertiesBySection(section);
  }

  public static String getString(PropsKey key) {
    return getString(key, null);
  }

  public static String getString(String key) {
    return getString(key, null);
  }

  public static String getString(PropsKey key, String defaultValue) {
    return getString(key.getKey(), defaultValue);
  }

  public static String getString(String key, String defaultValue) {
    if (StringUtil.isBlank(key)) {
      return null;
    }

    String value = getInstance().getProperty(key);

    if (StringUtil.isNotBlank(value)) {
      return value;
    }

    return defaultValue;
  }

  public static String[] getStringArray(PropsKey key) {
    return getStringArray(key.getKey());
  }

  public static String[] getStringArray(String key) {
    return Convert.toStringArray(getString(key));
  }

  public static void registerMBean(String contextName) {
    if (!_mbeanRegistered.compareAndSet(false, true)) {
      if (_log.isWarnEnabled()) {
        _log.warn("MBean has already registered");
      }

      return;
    }

    if (_log.isDebugEnabled()) {
      _log.debug("Registering MBean");
    }

    PropsBean instance = getInstance();

      try {
      _mbeanObjectName = JMXUtil.createObjectName(
        contextName, instance.getClass());

      JMXUtil.registerMBean(instance, _mbeanObjectName);
    }
    catch (JMException jme) {
      _log.error("MBean registration failed", jme);
    }
  }

  public static void setString(String key, String value) {
    getInstance().setProperty(key, value);
  }

  public static void unregisterMBean() {
    try {
      JMXUtil.unregisterMBean(_mbeanObjectName);
    }
    catch (JMException jme) {
      _log.error("MBean unregistration failed", jme);
    }
  }

  private static InputStream _getDefaultPropertyInputStream() {
    URL resourceUrl = ClassLoaderUtil.getResourceUrl(_DEFAULT_PROPERTY_FILE);

    return _openStream(resourceUrl);
  }

  private static InputStream _getExternalPropertyInputStream() {
    String catalinaBase = System.getProperty("catalina.base");

    if (StringUtil.isBlank(catalinaBase)) {
      _log.warn("catalina.base is null or empty");

      return null;
    }

    StringBand sb = new StringBand(5);

    sb.append("file://");
    sb.append(catalinaBase);
    sb.append(File.separator);

    // TODO This his questionable here
    // sb.append(_contextName);

    sb.append("app.properies");

    URL url = null;

    try {
      url = new URL(sb.toString());
    }
    catch (MalformedURLException mue) {
      _log.error(mue.getMessage(), mue);
    }

    return _openStream(url);
  }

  private static InputStream _openStream(URL url) {
    if (url == null) {
      return null;
    }

    InputStream inputStream = null;

    
    try {
      inputStream = url.openStream();
    }
    catch (FileNotFoundException fnfe) {
      if (_log.isWarnEnabled()) {
        _log.warn(
          "The following resource could not be found: " + url.toString());
      }
    }
    catch (IOException ioe) {
      _log.error(ioe.getMessage(), ioe);
    }

    return inputStream;
  }

  private static final String _DEFAULT_PROPERTY_FILE =
    "META-INF/kernel.properties";

  private static PropsBean _instance;

  private static AtomicBoolean _initialized = new AtomicBoolean(false);

  private static ObjectName _mbeanObjectName;
  private static AtomicBoolean _mbeanRegistered = new AtomicBoolean(false);

  private static Logger _log = LoggerFactory.getLogger(PropsBeanUtil.class);

}