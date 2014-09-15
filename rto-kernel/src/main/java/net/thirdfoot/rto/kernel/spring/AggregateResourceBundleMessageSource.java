package net.thirdfoot.rto.kernel.spring;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Queue;
import java.util.Set;

import javax.servlet.ServletContext;

import jodd.util.LocaleUtil;
import jodd.util.StringPool;
import jodd.util.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.context.ServletContextAware;

/**
 * @author lcsontos
 */
public class AggregateResourceBundleMessageSource
  extends ReloadableResourceBundleMessageSource
  implements InitializingBean, ServletContextAware {

  public static final String DEFAULT_BASE_DIR = "/WEB-INF/i18n";

  public AggregateResourceBundleMessageSource() {
    Locale[] availableLocales = Locale.getAvailableLocales();

    _availableLocales = new HashSet<Locale>(availableLocales.length);

    for (Locale locale : availableLocales) {
      _availableLocales.add(locale);
    }
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    if (StringUtil.isBlank(_baseDir)) {
      _baseDir = DEFAULT_BASE_DIR;
    }

    if (_log.isInfoEnabled()) {
      _log.info("Using base dir: " + _baseDir);
    }

    Set<String> baseNames = new HashSet<String>();

    Queue<String> paths = new LinkedList<String>();

    paths.addAll(_servletContext.getResourcePaths(_baseDir));

    while (!paths.isEmpty()) {
      String path = paths.poll();

      if (path.endsWith(StringPool.SLASH)) {
        paths.addAll(_servletContext.getResourcePaths(path));

        continue;
      }

      int extPos = path.indexOf(".properties");

      if (extPos == -1) {
        continue;
      }

      int endPos = path.indexOf('_');

      if (endPos == -1) {
        endPos = extPos;
      }
      else {
        Locale locale = LocaleUtil.getLocale(
          path.substring(endPos + 1, extPos));
  
        if (!_availableLocales.contains(locale)) {
          continue;
        }
      }

      String baseName = path.substring(0, endPos);

      if (baseNames.contains(baseName)) {
        continue;
      }

      if (_log.isInfoEnabled()) {
        _log.info("Adding base name: " + baseName);
      }

      baseNames.add(baseName);
    }

    String[] baseNameArray = new String[baseNames.size()];

    int index = 0;

    for (String baseName : baseNames) {
      baseNameArray[index++] = baseName;
    }

    setBasenames(baseNameArray);
  }

  public void setBaseDir(String baseDir) {
    _baseDir = baseDir;
  }

  @Override
  public void setServletContext(ServletContext servletContext) {
    _servletContext = servletContext;
  }

  private static Logger _log = LoggerFactory.getLogger(
    AggregateResourceBundleMessageSource.class);

  private final Set<Locale> _availableLocales;

  private String _baseDir;
  private ServletContext _servletContext;

}