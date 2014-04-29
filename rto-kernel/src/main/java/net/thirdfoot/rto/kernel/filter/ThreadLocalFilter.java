package net.thirdfoot.rto.kernel.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import net.thirdfoot.rto.kernel.util.ThreadLocalRegistry;

/**
 * @author lcsontos
 */
public class ThreadLocalFilter extends BaseFilter {

  @Override
  public void destroy() {
    ThreadLocalRegistry.clearThreadLocals();
  }

  @Override
  protected void doFilterFinally(
    ServletRequest request, ServletResponse response) {

    ThreadLocalRegistry.resetThreadLocals();
  }

  @Override
  protected void doProcessFilter(
    ServletRequest request, ServletResponse response) {
  }

}