package net.thirdfoot.rto.kernel.exception;

import net.thirdfoot.rto.kernel.config.PropsKey;

/**
 * @author lcsontos
 */
public class DuplicateObjectException extends ApplicationException {

  public DuplicateObjectException() {
  }

  public DuplicateObjectException(String message) {
    super(message);
  }

  public DuplicateObjectException(
    PropsKey messageKey, Object... messageParams) {

    super(messageKey, messageParams);
  }

}