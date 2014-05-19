package net.thirdfoot.rto.kernel.config;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.util.Map;

import javax.management.JMException;
import javax.management.MBeanServer;
import javax.management.ObjectName;

import jodd.io.FileUtil;
import jodd.typeconverter.Convert;
import jodd.util.ClassLoaderUtil;
import jodd.util.StringBand;
import jodd.util.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lcsontos
 */
public class PropsUtil {

  public static void init() {
    // TODO Add utility to get the actual context name
    _contextName = "rto";

    File[] propertyFiles = new File[2];

    propertyFiles[0] = _getDefaultPropertyFile();
    propertyFiles[1] = _getExternalPropertyFile();

    _propsMBean = new PropsMBeanImpl(propertyFiles);

    try {
      MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();

      ObjectName objectName = new ObjectName(
        _contextName, "type", PropsMBean.class.getName());

      if (_log.isDebugEnabled()) {
        _log.debug("Registering MBean");
      }

      mBeanServer.registerMBean(_propsMBean, objectName);
    }
    catch (JMException jme) {
      _log.error("MBean registration failed", jme);
    }
  }

  public static Boolean getBoolean(String key) {
    return Convert.toBoolean(getString(key));
  }

  public static boolean[] getBooleanArray(String key) {
    return Convert.toBooleanArray(getString(key));
  }

  public static Integer getInteger(String key) {
    return Convert.toInteger(getString(key));
  }

  public static int[] getIntegerArray(String key) {
    return Convert.toIntegerArray(getString(key));
  }

  public static Map<String, String> getSection(String section) {
    return _propsMBean.getPropertiesBySection(section);
  }

  public static String getString(String key) {
    if (StringUtil.isBlank(key)) {
      return null;
    }

    return _propsMBean.getProperty(key);
  }

  public static String[] getStringArray(String key) {
    return Convert.toStringArray(getString(key));
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
    "/META-INF/rto.properties";

  private static Logger _log = LoggerFactory.getLogger(PropsUtil.class);

  private static String _contextName;
  private static PropsMBean _propsMBean;

}