package net.thirdfoot.rto.model.exception;

import net.thirdfoot.rto.kernel.exception.ApplicationException;

/**
 * @author lcsontos
 */
public class InvalidVideoUrlException extends ApplicationException {

  public static final String MESSAGE_KEY = "the-specified-video-url-is-invalid";

  public InvalidVideoUrlException() {
    super(MESSAGE_KEY);
  }

}