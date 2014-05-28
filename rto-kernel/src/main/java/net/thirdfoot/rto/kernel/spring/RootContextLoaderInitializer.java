package net.thirdfoot.rto.kernel.spring;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import net.thirdfoot.rto.kernel.filter.ThreadLocalFilter;

import org.springframework.web.context.AbstractContextLoaderInitializer;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author lcsontos
 */
public class RootContextLoaderInitializer
  extends AbstractContextLoaderInitializer {

  @Override
  public void onStartup(ServletContext servletContext) throws ServletException {
    super.onStartup(servletContext);

    registerFilters(servletContext);
  }

  @Override
  protected WebApplicationContext createRootApplicationContext() {
    return new RootApplicationContext();
  }

  @Override
  protected void registerContextLoaderListener(ServletContext servletContext) {
    WebApplicationContext rootContext = createRootApplicationContext();

    servletContext.addListener(new RootContextLoaderListener(rootContext));
  }

  protected void registerFilters(ServletContext servletContext) {
    for (String filter : _FILTERS) {
      servletContext.addFilter(filter, filter);
    }
  }

  private static final String[] _FILTERS = new String[] {
    ThreadLocalFilter.class.getName()
  };

}