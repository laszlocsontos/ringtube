package net.thirdfoot.rto.kernel.i18n;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.MessageSource;

/**
 * @author lcsontos
 */
public final class LanguageUtil {

  public static String get(String key) {
    return get(key, null);
  }

  public static String get(String key, Locale locale) {
    return get(key, locale, (Object[])null);
  }

  public static String get(String key, Locale locale, Object... params) {
    if (!_initialized.get()) {
      if (_log.isWarnEnabled()) {
        _log.warn("No messageSource has been supplied.");
      }

      return key;
    }

    if (locale == null) {
      locale = LanguageThreadLocal.getCurrentLocale();
    }

    return _instance._messageSource.getMessage(key, params, locale);
  }

  public static LanguageUtil getInstance(MessageSource messageSource) {
    if (_initialized.compareAndSet(false, true)) {
      _instance = new LanguageUtil(messageSource);
    }

    return _instance;
  }

  private LanguageUtil(MessageSource messageSource) {
    _messageSource = messageSource;
  }

  private static LanguageUtil _instance;
  private static AtomicBoolean _initialized = new AtomicBoolean(false);

  private static Logger _log = LoggerFactory.getLogger(LanguageUtil.class);

  private final MessageSource _messageSource;

}