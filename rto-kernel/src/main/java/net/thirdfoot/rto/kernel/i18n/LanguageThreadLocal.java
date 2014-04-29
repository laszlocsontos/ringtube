package net.thirdfoot.rto.kernel.i18n;

import java.util.Locale;

import net.thirdfoot.rto.kernel.util.ThreadLocalRegistry;

/**
 * @author lcsontos
 */
public class LanguageThreadLocal {

  public static Locale getCurrentLocale() {
    return _localeThreadLocal.get();
  }

  public static void setCurrentLocale(Locale locale) {
    _localeThreadLocal.set(locale);
  }

  private static ThreadLocal<Locale> _localeThreadLocal =
    ThreadLocalRegistry.registerThreadLocal(
      LanguageThreadLocal.class + "._locale", Locale.getDefault());

  private LanguageThreadLocal() {
  }

}