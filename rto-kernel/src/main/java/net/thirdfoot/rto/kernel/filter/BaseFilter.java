package net.thirdfoot.rto.kernel.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lcsontos
 */
public abstract class BaseFilter implements Filter {

  @Override
  public void destroy() {
    if (_log.isDebugEnabled()) {
      _log.debug(this.getClass() + " destroyed.");
    }
  }

  @Override
  public final void doFilter(
      ServletRequest request, ServletResponse response, FilterChain chain)
    throws IOException, ServletException {

    try {
      doProcessFilter(request, response);

      chain.doFilter(request, response);
    }
    finally {
      doFilterFinally(request, response);
    }
  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    if (_log.isDebugEnabled()) {
      _log.debug(this.getClass() + " initialized.");
    }
  }

  protected void doFilterFinally(
      ServletRequest request, ServletResponse response) {
  }

  protected abstract void doProcessFilter(
    ServletRequest request, ServletResponse response);

  private static Logger _log = LoggerFactory.getLogger(BaseFilter.class);

}