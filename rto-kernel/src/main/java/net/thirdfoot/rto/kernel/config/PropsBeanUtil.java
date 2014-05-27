package net.thirdfoot.rto.kernel.config;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.management.JMException;
import javax.management.MBeanServer;
import javax.management.ObjectName;

import jodd.io.FileUtil;
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
      File[] propertyFiles = new File[2];

      propertyFiles[0] = _getDefaultPropertyFile();
      propertyFiles[1] = _getExternalPropertyFile();

      _instance = new PropsBeanImpl(propertyFiles);
    }

    return _instance;
  }

  public static Boolean getBoolean(String key) {
    return getBoolean(key, false);
  }

  public static Boolean getBoolean(String key, boolean defaultValue) {
    Boolean value = Convert.toBoolean(getString(key));

    if (value != null) {
      return value;
    }

    return defaultValue;
  }

  public static boolean[] getBooleanArray(String key) {
    return getBooleanArray(key, null);
  }

  public static boolean[] getBooleanArray(
    String key, boolean[] defaultValue) {

    boolean[] value = Convert.toBooleanArray(getString(key));

    if (value != null) {
      return value;
    }

    return defaultValue;
  }

  public static Integer getInteger(String key) {
    return getInteger(key, 0);
  }

  public static Integer getInteger(String key, int defaultValue) {
    Integer value = Convert.toInteger(getString(key));

    if (value != null) {
      return value;
    }

    return defaultValue;
  }

  public static int[] getIntegerArray(String key) {
    return getIntegerArray(key, null);
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

  public static String getString(String key) {
    return getString(key, null);
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

    try {
      PropsBean instance = getInstance();

      ObjectName objectName = JMXUtil.createObjectName(
        contextName, instance.getClass());
  
      MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();

      mBeanServer.registerMBean(instance, objectName);
    }
    catch (JMException jme) {
      _log.error("MBean registration failed", jme);
    }
  }

  private static File _getDefaultPropertyFile() {
    return ClassLoaderUtil.getResourceFile(_DEFAULT_PROPERTY_FILE);
  }

  private static File _getExternalPropertyFile() {
    String catalinaBase = System.getProperty("catalina.base");

    if (StringUtil.isBlank(catalinaBase)) {
      _log.warn("catalina.base is null or empty");

      return null;
    }

    StringBand sb = new StringBand(4);

    sb.append(FileUtil.getParentFile(new File(catalinaBase)));
    sb.append(File.separator);
    sb.append(_contextName);
    sb.append(".properies");

    File file = new File(sb.toString());

    if (!file.exists() || !file.canRead()) {
      _log.warn(
        "Property file " + file.getAbsolutePath() + 
          " does not exists or cannot be read");

      return null;
    }

    return file;
  }

  private static final String _DEFAULT_PROPERTY_FILE =
    "META-INF/kernel.properties";

  private static PropsBean _instance;

  private static AtomicBoolean _initialized = new AtomicBoolean(false);
  private static AtomicBoolean _mbeanRegistered = new AtomicBoolean(false);

  private static Logger _log = LoggerFactory.getLogger(PropsBeanUtil.class);

  private static String _contextName;
}