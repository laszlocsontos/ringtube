package net.thirdfoot.rto.model.exception;

import net.thirdfoot.rto.kernel.exception.NoSuchObjectException;
import net.thirdfoot.rto.util.MessageKeys;

/**
 * @author lcsontos
 */
public class NoSuchVideoException extends NoSuchObjectException {

  public NoSuchVideoException() {
    super(MessageKeys.THE_SPECIFIED_VIDEO_DOES_NOT_EXIST);
  }

}