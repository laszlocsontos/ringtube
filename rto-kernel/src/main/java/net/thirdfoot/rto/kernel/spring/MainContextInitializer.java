package net.thirdfoot.rto.kernel.spring;

import java.io.File;
import java.net.URL;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import jodd.util.ClassLoaderUtil;
import jodd.util.StringPool;
import jodd.util.StringUtil;

import net.thirdfoot.rto.kernel.config.PropsBeanUtil;
import net.thirdfoot.rto.kernel.filter.ThreadLocalFilter;
import net.thirdfoot.rto.kernel.util.FileSystemUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.web.context.AbstractContextLoaderInitializer;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;

/**
 * @author lcsontos
 */
public class MainContextInitializer extends AbstractContextLoaderInitializer {

  @Override
  public void onStartup(ServletContext servletContext) throws ServletException {
    setContext(servletContext);

    initLogger();
    initProps();

    super.onStartup(servletContext);

    _log.info("Application " + _contextName + " has been started.");
  }

  @Override
  protected WebApplicationContext createRootApplicationContext() {
    return new RootApplicationContext();
  }

  protected void initLogger() {
    LoggerContext loggerContext =
      (LoggerContext)LoggerFactory.getILoggerFactory();

    loggerContext.reset();

    loggerContext.setName(_contextName);

    File logDir = FileSystemUtil.getLogDir();

    loggerContext.putProperty("log.dir", logDir.getAbsolutePath());

    try {
      JoranConfigurator loggerConfigurator = new JoranConfigurator();

      loggerConfigurator.setContext(loggerContext);

      URL loggerConfig = ClassLoaderUtil.getResourceUrl(_LOGGER_CONFIG);

      loggerConfigurator.doConfigure(loggerConfig);
    }
    catch (JoranException je) {
      // StatusPrinter will handle this
    }
    catch (Exception e) {
      e.printStackTrace();
    }

    StatusPrinter.printInCaseOfErrorsOrWarnings(loggerContext);
  }

  protected void initProps() {
    PropsBeanUtil.setString("context.name", _contextName);
    PropsBeanUtil.registerMBean(_contextName);
  }

  protected void registerFilters(ServletContext servletContext) {
    for (String filter : _FILTERS) {
      servletContext.addFilter(filter, filter);
    }
  }

  protected void setContext(ServletContext servletContext) {
    String contextName = servletContext.getContextPath();

    if (StringUtil.isBlank(contextName) ||
      contextName.equals(StringPool.SLASH)) {

      _contextName = "ROOT";

      return;
    }

    contextName = StringUtil.cutSuffix(contextName, StringPool.SLASH);

    if (contextName.indexOf(StringPool.SLASH) != -1) {
      contextName = StringUtil.replace(
        contextName, StringPool.SLASH, StringPool.UNDERSCORE);
    }

    _contextName = contextName;
  }

  private static final ConfigurableEnvironment _ENVIRONMENT =
    new PropsEnvironment();

  private static final String[] _FILTERS = new String[] {
    ThreadLocalFilter.class.getName()
  };

  private static final String _LOGGER_CONFIG = "META-INF/kernel-log.xml";

  private static final String[] _RESOURCES = {
    "classpath:META-INF/kernel-spring.xml", "classpath*:META-INF/*-spring.xml"
  };

  private static Logger _log =
    LoggerFactory.getLogger(MainContextInitializer.class);

  private String _contextName;

  private static class PropsEnvironment extends AbstractEnvironment {
    @Override
    protected void customizePropertySources(
      MutablePropertySources propertySources) {

      PropertySource<?> propertySource = PropsBeanUtil.getPropertySource();

      propertySources.addFirst(propertySource);
    }
  }

  private static class RootApplicationContext extends XmlWebApplicationContext {
    @Override
    protected ConfigurableEnvironment createEnvironment() {
      return _ENVIRONMENT;
    }

    @Override
    protected String[] getDefaultConfigLocations() {
      return _RESOURCES;
    }
  }

}