package net.thirdfoot.rto.kernel.i18n;

import java.text.MessageFormat;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author lcsontos
 */
public class LanguageUtil {

  public static String get(String key) {
    return get(key, LanguageThreadLocal.getCurrentLocale());
  }

  public static String get(String key, Locale locale) {
    ResourceBundle resourceBundle = ResourceBundle.getBundle(_BASE_NAME, locale);

    return resourceBundle.getString(key);
  }

  public static String format(String key, Object... params) {
    return format(key, LanguageThreadLocal.getCurrentLocale(), params);
  }

  public static String format(String key, Locale locale, Object... params) {
    String pattern = get(key, locale);

    return MessageFormat.format(pattern, params);
  }

  private static final String _BASE_NAME = "bundles.Language";

  private LanguageUtil() {
  }

}