package net.thirdfoot.rto.kernel.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.thirdfoot.rto.kernel.util.ThreadLocalRegistry;

/**
 * @author lcsontos
 */
public class ThreadLocalFilter extends BaseFilter {

  @Override
  public void destroy() {
    ThreadLocalRegistry.clearThreadLocals();

    super.destroy();
  }

  @Override
  protected void doFilterFinally(
    ServletRequest request, ServletResponse response) {

    if (_log.isDebugEnabled()) {
      _log.debug("resetting thread locals");
    }

    ThreadLocalRegistry.resetThreadLocals();
  }

  @Override
  protected void doProcessFilter(
    ServletRequest request, ServletResponse response) {

    if (_log.isTraceEnabled()) {
      _log.trace("doProcessFilter");
    }
  }

  private static Logger _log = LoggerFactory.getLogger(ThreadLocalFilter.class);

}