package net.thirdfoot.rto.kernel.spring;

import java.io.File;
import java.net.URL;

import javax.servlet.ServletContextEvent;

import jodd.util.ClassLoaderUtil;
import jodd.util.StringPool;
import jodd.util.StringUtil;

import net.thirdfoot.rto.kernel.config.PropsBeanUtil;
import net.thirdfoot.rto.kernel.util.FileSystemUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;

/**
 * @author lcsontos
 */
class RootContextLoaderListener extends ContextLoaderListener {

  RootContextLoaderListener(WebApplicationContext context) {
    super(context);
  }

  @Override
  public void contextDestroyed(ServletContextEvent event) {
    super.contextDestroyed(event);

    PropsBeanUtil.unregisterMBean();

    _loggerContext.stop();
  }

  @Override
  public void contextInitialized(ServletContextEvent event) {
    super.contextInitialized(event);

    initContextName(event);
    initLogger();
    initProps();

    _log.info("Application " + _contextName + " has been started.");
  }

  protected void initLogger() {
    _loggerContext = (LoggerContext)LoggerFactory.getILoggerFactory();

    _loggerContext.reset();

    _loggerContext.setName(_contextName);

    File logDir = FileSystemUtil.getLogDir();

    _loggerContext.putProperty("log.dir", logDir.getAbsolutePath());

    try {
      JoranConfigurator loggerConfigurator = new JoranConfigurator();

      loggerConfigurator.setContext(_loggerContext);

      URL loggerConfig = ClassLoaderUtil.getResourceUrl(_LOGGER_CONFIG);

      loggerConfigurator.doConfigure(loggerConfig);
    }
    catch (JoranException je) {
      // StatusPrinter will handle this
    }
    catch (Exception e) {
      e.printStackTrace();
    }

    StatusPrinter.printInCaseOfErrorsOrWarnings(_loggerContext);
  }

  protected void initProps() {
    PropsBeanUtil.registerMBean(_contextName);

    PropsBeanUtil.setString("context.name", _contextName);
  }

  protected void initContextName(ServletContextEvent event) {
    String contextName = event.getServletContext().getContextPath();

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

  private static final String _LOGGER_CONFIG = "META-INF/kernel-log.xml";

  private static Logger _log =
    LoggerFactory.getLogger(RootContextLoaderListener.class);

  private String _contextName;
  private LoggerContext _loggerContext;

}