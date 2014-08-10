package net.thirdfoot.rto.kernel.exception;

import net.thirdfoot.rto.kernel.config.PropsKey;

/**
 * @author lcsontos
 */
public class NoSuchObjectException extends ApplicationException {

  public NoSuchObjectException() {
    super();
  }

  public NoSuchObjectException(String message) {
    super(message);
  }

  public NoSuchObjectException(PropsKey messageKey, Object... messageParams) {
    super(messageKey, messageParams);
  }

}