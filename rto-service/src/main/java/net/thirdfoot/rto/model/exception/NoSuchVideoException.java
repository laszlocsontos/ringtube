package net.thirdfoot.rto.model.exception;

import net.thirdfoot.rto.kernel.exception.ApplicationException;

public class NoSuchVideoException extends ApplicationException {

  public NoSuchVideoException() {
  }

  public NoSuchVideoException(String messageTemplate, String url) {
    super(messageTemplate, url);
  }

}
