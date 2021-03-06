package net.thirdfoot.rto.kernel.spring;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jodd.util.StringUtil;
import net.thirdfoot.rto.kernel.exception.ApplicationException;
import net.thirdfoot.rto.kernel.exception.NoSuchObjectException;
import net.thirdfoot.rto.kernel.exception.DuplicateObjectException;
import net.thirdfoot.rto.kernel.log.MDCKeys;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

/**
 * @author lcsontos
 */
public class HandlerExceptionResolver
  extends DefaultHandlerExceptionResolver implements InitializingBean {

  @Override
  public void afterPropertiesSet() throws Exception {
    if (StringUtil.isBlank(_errorViewName)) {
      _errorViewName = _DEFAULT_ERROR_VIEW_NAME;
    }
  }

  public String getErrorViewName() {
    return _errorViewName;
  }

  public HttpMessageConverter<Object> getHttpMessageConverter() {
    return _httpMessageConverter;
  }

  public void setErrorViewName(String errorViewName) {
    _errorViewName = errorViewName;
  }

  public void setHttpMessageConverter(
    HttpMessageConverter<Object> httpMessageConverter) {

    _httpMessageConverter = httpMessageConverter;
  }

  @Override
  protected ModelAndView doResolveException(HttpServletRequest request,
    HttpServletResponse response, Object handler, Exception e) {

    ModelAndView modelAndView = super.doResolveException(
      request, response, handler, e);

    if (modelAndView != null) {
      return modelAndView;
    }

    if (_isApplicationException(e)) {
      _handleApplicationException(response, e);

      return null;
    }

    return _handleSystemException(request, e);
  }

  @Override
  protected void logException(Exception e, HttpServletRequest request) {
    if (!_isApplicationException(e)) {
      _log.error(e.getMessage(), e);
    }
    else if (_log.isDebugEnabled()) {
      _log.debug(e.getMessage(), e);
    }
  }

  @Override
  protected void prepareResponse(Exception e, HttpServletResponse response) {
    if (_isApplicationException(e)) {
      return;
    }

    preventCaching(response);
  }

  private void _handleApplicationException(
    HttpServletResponse response, Exception e) {

    Map<String, String> errorInfo = new HashMap<String, String>(2);

    errorInfo.put("exception", e.getClass().getSimpleName());
    errorInfo.put("message", e.getMessage());

    response.setStatus(_SC_UNPROCESSABLE_ENTITY);

    HttpOutputMessage outputMessage = new ServletServerHttpResponse(response);

    try {
      _httpMessageConverter.write(
        errorInfo, MediaType.APPLICATION_JSON, outputMessage);
    }
    catch (Exception e1) {
      _log.error(e.getMessage(), e1);
    }
  }

  private ModelAndView _handleSystemException(
    HttpServletRequest request, Exception e) {

    ModelAndView modelAndView = new ModelAndView(_errorViewName);

    String requestId = MDC.get(MDCKeys.REQUEST_ID.toString());

    modelAndView.addObject(MDCKeys.REQUEST_ID.toString(), requestId);

    return modelAndView;
  }

  private boolean _isApplicationException(Exception e) {
    return (e instanceof ApplicationException);
  }

  private static final String _DEFAULT_ERROR_VIEW_NAME = "error";

  private static final int _SC_UNPROCESSABLE_ENTITY = 422;

  private static Logger _log = LoggerFactory.getLogger(
    HandlerExceptionResolver.class);

  private String _errorViewName;
  private HttpMessageConverter<Object> _httpMessageConverter;

}