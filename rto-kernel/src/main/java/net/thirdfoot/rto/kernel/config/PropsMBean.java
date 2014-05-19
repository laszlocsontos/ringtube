package net.thirdfoot.rto.kernel.config;

import java.util.Map;

import javax.management.MXBean;

/**
 * @author lcsontos
 */
@MXBean
public interface PropsMBean {

  public Map<String, String> getProperties();

  public Map<String, String> getPropertiesBySection(String section);

  public String getProperty(String key);

  public void setProperty(String key, String value);

  public void reload();

}