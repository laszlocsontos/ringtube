package net.thirdfoot.rto.kernel.spring;

import net.thirdfoot.rto.kernel.config.PropsBeanUtil;

import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.web.context.support.XmlWebApplicationContext;

/**
 * @author lcsontos
 */
class RootApplicationContext extends XmlWebApplicationContext {

  @Override
  protected ConfigurableEnvironment createEnvironment() {
    return _ENVIRONMENT;
  }

  @Override
  protected String[] getDefaultConfigLocations() {
    return _RESOURCES;
  }

  private static final ConfigurableEnvironment _ENVIRONMENT =
    new PropsEnvironment();

  private static final String[] _RESOURCES = {
    "classpath:META-INF/kernel-spring.xml", "classpath*:META-INF/*-spring.xml"
  };

  private static class PropsEnvironment extends AbstractEnvironment {

    @Override
    protected void customizePropertySources(
      MutablePropertySources propertySources) {

      PropertySource<?> propertySource = PropsBeanUtil.getPropertySource();

      propertySources.addFirst(propertySource);
    }

  }

}