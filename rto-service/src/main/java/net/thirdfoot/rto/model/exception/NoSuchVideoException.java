package net.thirdfoot.rto.model.exception;

import net.thirdfoot.rto.kernel.exception.ApplicationException;

/**
 * @author lcsontos
 */
public class NoSuchVideoException extends ApplicationException {

  public static final String MESSAGE_KEY = "the-specified-video-does-not-exist";

  public NoSuchVideoException() {
    super(MESSAGE_KEY);
  }

}