package net.thirdfoot.rto.kernel.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextLoader;

/**
 * @author lcsontos
 */
// TODO Make this work
public class RootContextTestLoader implements ContextLoader {

  @Override
  public ApplicationContext loadContext(String... locations) throws Exception {
    ConfigurableApplicationContext context = new RootApplicationContext();

    context.refresh();
    context.start();

    return context;
  }

  @Override
  public String[] processLocations(Class<?> clazz, String... locations) {
    return RootContextConstants.RESOURCES;
  }

}