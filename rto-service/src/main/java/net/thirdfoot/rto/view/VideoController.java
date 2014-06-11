package net.thirdfoot.rto.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * @author lcsontos
 */
@Controller
public class VideoController
  extends AbstractController implements InitializingBean {

  @Override
  public void afterPropertiesSet() throws Exception {
    _log.info("afterPropertiesSet");
  }

  @Override
  protected ModelAndView handleRequestInternal(HttpServletRequest request,
    HttpServletResponse response) throws Exception {

    _log.info(request.getContextPath());

    return null;
  }

  private static Logger _log = LoggerFactory.getLogger(VideoController.class);

}