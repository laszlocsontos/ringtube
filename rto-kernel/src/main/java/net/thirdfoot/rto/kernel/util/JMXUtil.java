package net.thirdfoot.rto.kernel.util;

import java.util.Hashtable;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

/**
 * @author lcsontos
 */
public final class JMXUtil {

  public static final String DOMAIN = "net.thirdfoot";

  public static ObjectName createObjectName(String name, Class<?> type)
    throws MalformedObjectNameException {

    Hashtable<String, String> properties = new Hashtable<String, String>(2);

    properties.put("name", name);
    properties.put("type", type.getName());

    return new ObjectName(DOMAIN, properties);
  }

  private JMXUtil() {
  }

}