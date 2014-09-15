package net.thirdfoot.rto.kernel.exception;

import net.thirdfoot.rto.kernel.config.PropsKey;
import net.thirdfoot.rto.kernel.i18n.LanguageUtil;
import jodd.util.StringPool;
import jodd.util.StringUtil;

/**
 * @author lcsontos
 */
public class ApplicationException extends Exception {

  public static final Object[] EMPTY_MESSAGE_PARAMS = new Object[0];

  public ApplicationException() {
    this(null, EMPTY_MESSAGE_PARAMS);
  }

  public ApplicationException(String message) {
    this(null, EMPTY_MESSAGE_PARAMS);

    _message = message;
  }

  public ApplicationException(PropsKey messageKey, Object... messageParams) {
    _messageKey = messageKey;

    if (messageParams != null) {
      _messageParams = messageParams;
    }
    else {
      _messageParams = EMPTY_MESSAGE_PARAMS;
    }
  }

  @Override
  public String getMessage() {
    if (StringUtil.isNotBlank(_message)) {
      return _message;
    }

    if (_messageKey == null) {
      return StringPool.EMPTY;
    }

    String messageKey = _messageKey.getKey();

    if (_messageParams.length > 0) {
      _message = LanguageUtil.get(messageKey, null, _messageParams);
    }
    else {
      _message = LanguageUtil.get(messageKey);
    }

    return _message;
  }

  private final transient PropsKey _messageKey;
  private final transient Object[] _messageParams;

  private String _message;

}