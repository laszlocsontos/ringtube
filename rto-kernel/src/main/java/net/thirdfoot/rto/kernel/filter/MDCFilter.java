package net.thirdfoot.rto.kernel.filter;

import java.util.UUID;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import jodd.util.StringPool;
import jodd.util.StringUtil;

import net.thirdfoot.rto.kernel.log.MDCKeys;

import org.slf4j.MDC;

/**
 * @author lcsontos
 */
public class MDCFilter extends BaseFilter {

  @Override
  public void destroy() {
    _cleanUp();

    super.destroy();
  }

  @Override
  protected void doFilterFinally(
      ServletRequest request, ServletResponse response) {

    _cleanUp();
  }

  @Override
  protected void doProcessFilter(
    ServletRequest request, ServletResponse response) {

    HttpServletRequest httpServletRequest = (HttpServletRequest)request;

    String remoteAddr = httpServletRequest.getRemoteAddr();

    MDC.put(MDCKeys.REMOTE_ADDR.toString(), remoteAddr);

    StringBuffer requestURLBuffer = httpServletRequest.getRequestURL();

    String queryString = httpServletRequest.getQueryString();

    if (StringUtil.isNotBlank(queryString)) {
      requestURLBuffer.append(StringPool.QUESTION_MARK);
      requestURLBuffer.append(queryString);
    }

    String requestURL = requestURLBuffer.toString();

    MDC.put(MDCKeys.REQUEST_URL.toString(), requestURL);

    // TODO Calculate this with Hash(remoteAddr, requestURL, userAgent, time)
    String requestId = UUID.randomUUID().toString();

    MDC.put(MDCKeys.REQUEST_ID.toString(), requestId);
}

  private void _cleanUp() {
    MDC.clear();
  }

}