package net.thirdfoot.rto.kernel.spring;

/**
 * @author lcsontos
 */
public interface RootContextConstants {

  public static final String[] RESOURCES = new String[] {
    "classpath:META-INF/kernel-spring.xml",
    "classpath*:META-INF/service-spring.xml"
  };

}