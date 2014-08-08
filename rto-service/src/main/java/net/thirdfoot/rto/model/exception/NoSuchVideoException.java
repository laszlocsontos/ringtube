package net.thirdfoot.rto.model.exception;

import net.thirdfoot.rto.kernel.exception.ApplicationException;
import net.thirdfoot.rto.util.MessageKeys;

/**
 * @author lcsontos
 */
public class NoSuchVideoException extends ApplicationException {

  public NoSuchVideoException() {
    super(MessageKeys.THE_SPECIFIED_VIDEO_DOES_NOT_EXIST);
  }

}