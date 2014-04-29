package net.thirdfoot.rto.kernel.exception;

import net.thirdfoot.rto.kernel.i18n.LanguageUtil;

import jodd.util.StringPool;
import jodd.util.StringUtil;

/**
 * @author lcsontos
 */
public class ApplicationException extends Exception {

  public ApplicationException() {
    _messageKey = null;
    _messageParams = null;
  }

  public ApplicationException(String messageKey, Object... messageParams) {
    _messageKey = messageKey;
    _messageParams = messageParams;
  }

  @Override
  public String getMessage() {
    if (StringUtil.isBlank(_messageKey)) {
      return StringPool.EMPTY;
    }

    if (StringUtil.isBlank(_message)) {
      _message = LanguageUtil.format(_messageKey, _messageParams);
    }

    return _message;
  }

  private final transient String _messageKey;
  private final transient Object[] _messageParams;

  private String _message;

}