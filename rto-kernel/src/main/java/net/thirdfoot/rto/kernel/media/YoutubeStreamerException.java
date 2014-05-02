package net.thirdfoot.rto.kernel.media;

import org.python.core.PyException;

import net.thirdfoot.rto.kernel.exception.SystemException;

/**
 * @author lcsontos
 */
public class YoutubeStreamerException extends SystemException {

  public YoutubeStreamerException(PyException cause) {
    super(cause);
  }

}