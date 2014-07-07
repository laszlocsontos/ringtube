package net.thirdfoot.rto.kernel.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

/**
 * @author lcsontos
 */
public abstract class AbstractServiceBean<S extends ServiceBean>
  implements ServiceBean{

  @Override
  public void afterPropertiesSet() {
    try {
      instance = applicationContext.getBean(getServiceClass());
    }
    catch (BeansException be) {
      _log.error(be.getMessage(), be);
    }
  }

  public abstract Class<S> getServiceClass();

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  private static Logger _log = LoggerFactory.getLogger(AbstractServiceBean.class);

  protected ApplicationContext applicationContext;

  protected S instance;
}