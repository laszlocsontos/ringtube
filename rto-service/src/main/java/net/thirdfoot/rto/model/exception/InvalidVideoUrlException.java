package net.thirdfoot.rto.model.exception;

import net.thirdfoot.rto.kernel.exception.ApplicationException;
import net.thirdfoot.rto.util.MessageKeys;

/**
 * @author lcsontos
 */
public class InvalidVideoUrlException extends ApplicationException {

  public InvalidVideoUrlException() {
    super(MessageKeys.THE_SPECIFIED_VIDEO_URL_IS_INVALID);
  }

  public InvalidVideoUrlException(String reason) {
    super(MessageKeys.THE_SPECIFIED_VIDEO_URL_IS_INVALID_REASON_X, reason);
  }

  public InvalidVideoUrlException(Throwable cause) {
    super(
      MessageKeys.THE_SPECIFIED_VIDEO_URL_IS_INVALID_REASON_X,
      cause.getMessage());
  }

}