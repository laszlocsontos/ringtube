package net.thirdfoot.rto.kernel.spring;

import java.util.Map;

import jodd.bean.BeanUtil;
import jodd.util.StringUtil;

import net.thirdfoot.rto.kernel.config.PropsBeanUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.config.AbstractFactoryBean;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.PooledDataSource;

/**
 * @author lcsontos
 */
public final class C3P0DataSourceFactoryBean
  extends AbstractFactoryBean<PooledDataSource> {

  @Override
  public Class<?> getObjectType() {
    return _OBJECT_TYPE;
  }

  @Override
  public boolean isSingleton() {
    return _SINGLETON;
  }

  @Override
  protected PooledDataSource createInstance() throws Exception {
    PooledDataSource pooledDataSource = new ComboPooledDataSource();

    String contextName = PropsBeanUtil.getString("context.name");

    pooledDataSource.setDataSourceName(contextName);

    Map<String, String> properties = PropsBeanUtil.getSection("jdbc");

    for (Map.Entry<String, String> property : properties.entrySet()) {
      String key = property.getKey();

      String name = StringUtil.cutPrefix(key, "jdbc.");
      String value = property.getValue();

      try {
        if (_log.isDebugEnabled()) {
          _log.debug("Setting up property: " + name + "=" + value);
        }

        BeanUtil.setProperty(pooledDataSource, name, value);
      }
      catch (RuntimeException re) {
        if (_log.isWarnEnabled()) {
          _log.warn("Ignoring invalid property: " + name, re);
        }
      }
    }

    if (_log.isInfoEnabled()) {
      _log.info(
        "Data source " + pooledDataSource.getDataSourceName() +
          " has been created.");
    }

    return pooledDataSource;
  }

  @Override
  protected void destroyInstance(PooledDataSource pooledDataSource)
    throws Exception {

    if (_log.isInfoEnabled()) {
      _log.info("Closing data source " + pooledDataSource.getDataSourceName());
    }

    pooledDataSource.close();
  }

  private static final Class<PooledDataSource> _OBJECT_TYPE =
    PooledDataSource.class;

  private static final boolean _SINGLETON = true;

  private static Logger _log = LoggerFactory.getLogger(
    C3P0DataSourceFactoryBean.class);

}