package net.thirdfoot.rto.kernel.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * @author lcsontos
 */
public abstract class BaseFilter implements Filter {

  @Override
  public void destroy() {
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
  }

  protected void doFilterFinally(
      ServletRequest request, ServletResponse response) {
  }

  protected abstract void doProcessFilter(
    ServletRequest request, ServletResponse response);

}