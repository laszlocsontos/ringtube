package net.thirdfoot.rto.kernel.spring;

import java.util.HashMap;
import java.util.Map;

import net.thirdfoot.rto.kernel.config.PropsBeanUtil;

import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

/**
 * @author lcsontos
 */
public class HibernateConfigurationAdapter extends HibernateJpaVendorAdapter {

  @Override
  public Map<String, Object> getJpaPropertyMap() {
    Map<String, String> hibernateProperties = PropsBeanUtil.getSection("hibernate");
    Map<String, Object> parentProperties = super.getJpaPropertyMap();

    Map<String, Object> jpaProperties =
      new HashMap<String, Object>(
        hibernateProperties.size() + parentProperties.size(), 1.0f);

    jpaProperties.putAll(parentProperties);
    jpaProperties.putAll(hibernateProperties);

    return jpaProperties;
  }

}