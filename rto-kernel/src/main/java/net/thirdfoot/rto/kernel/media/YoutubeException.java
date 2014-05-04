package net.thirdfoot.rto.kernel.media;

import org.python.core.PyException;

import net.thirdfoot.rto.kernel.exception.SystemException;

/**
 * @author lcsontos
 */
public class YoutubeException extends SystemException {

  public YoutubeException(PyException cause) {
    super(cause);
  }

}