package net.thirdfoot.rto.kernel.exception;

import java.util.Arrays;

import net.thirdfoot.rto.kernel.i18n.LanguageUtil;
import jodd.util.ArraysUtil;
import jodd.util.StringPool;
import jodd.util.StringUtil;

/**
 * @author lcsontos
 */
public class ApplicationException extends Exception {

  public ApplicationException() {
    this(null, (Object)null);
  }

  public ApplicationException(String messageKey) {
    this(messageKey, (Object)null);
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
      if (_messageParams != null && _messageParams.length > 0) {
        _message = LanguageUtil.format(_messageKey, _messageParams);
      }
      else {
        _message = LanguageUtil.get(_messageKey);
      }
    }

    return _message;
  }

  private final transient String _messageKey;
  private final transient Object[] _messageParams;

  private String _message;

}