package net.thirdfoot.rto.kernel.spring;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import net.thirdfoot.rto.kernel.filter.ThreadLocalFilter;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.support.AbstractDispatcherServletInitializer;

/**
 * @author lcsontos
 */
public class RootContextLoaderInitializer
  extends AbstractDispatcherServletInitializer {

  @Override
  protected WebApplicationContext createRootApplicationContext() {
    return getRootApplicationContext();
  }

  @Override
  protected WebApplicationContext createServletApplicationContext() {
    return getRootApplicationContext();
  }

  protected WebApplicationContext getRootApplicationContext() {
    if (_rootApplicationContext == null) {
      _rootApplicationContext = new RootApplicationContext();
    }

    return _rootApplicationContext;
  }

  @Override
  protected String[] getServletMappings() {
    return _DISPATCHER_SERVLET_MAPPINGS;
  }

  @Override
  protected void registerContextLoaderListener(ServletContext servletContext) {
    WebApplicationContext rootContext = createRootApplicationContext();

    servletContext.addListener(new RootContextLoaderListener(rootContext));
  }

  private static final String[] _DISPATCHER_SERVLET_MAPPINGS = {"*.htm"};

  private WebApplicationContext _rootApplicationContext;

}