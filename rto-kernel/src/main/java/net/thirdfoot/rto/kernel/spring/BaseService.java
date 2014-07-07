package net.thirdfoot.rto.kernel.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author lcsontos
 */
public class BaseService<S>
  implements InitializingBean, ApplicationContextAware {

  @Override
  @SuppressWarnings("unchecked")
  public void afterPropertiesSet() {
    try {
      instance = (S)applicationContext.getBean(this.getClass());
    }
    catch (BeansException be) {
      _log.error(be.getMessage(), be);
    }
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  private static Logger _log = LoggerFactory.getLogger(BaseService.class);

  protected ApplicationContext applicationContext;

  protected S instance;
}