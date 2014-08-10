package net.thirdfoot.rto.kernel.exception;

import net.thirdfoot.rto.kernel.config.PropsKey;

/**
 * @author lcsontos
 */
public class InvalidObjectException extends ApplicationException {

  public InvalidObjectException() {
  }

  public InvalidObjectException(String message) {
    super(message);
  }

  public InvalidObjectException(PropsKey messageKey, Object... messageParams) {
    super(messageKey, messageParams);
  }

}