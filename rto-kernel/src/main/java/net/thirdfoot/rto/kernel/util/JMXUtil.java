package net.thirdfoot.rto.kernel.util;

import java.util.Hashtable;

import javax.management.JMException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import org.springframework.jmx.support.JmxUtils;

/**
 * @author lcsontos
 */
public final class JMXUtil extends JmxUtils {

  public static final String DOMAIN = "net.thirdfoot";

  public static ObjectName createObjectName(String type, Class<?> clazz)
    throws MalformedObjectNameException {

    Hashtable<String, String> properties = new Hashtable<String, String>(2);

    properties.put("name", clazz.getSimpleName());
    properties.put("type", type);

    return new ObjectName(DOMAIN, properties);
  }

  public static void registerMBean(Object instance, ObjectName objectName)
    throws JMException {

    MBeanServer mbeanServer = locateMBeanServer();

    mbeanServer.registerMBean(instance, objectName);
  }

  public static void unregisterMBean(ObjectName objectName)
    throws JMException {

    MBeanServer mbeanServer = locateMBeanServer();

    mbeanServer.unregisterMBean(objectName);
  }

  private JMXUtil() {
  }

}