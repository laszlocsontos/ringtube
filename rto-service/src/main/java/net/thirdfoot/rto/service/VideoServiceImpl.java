package net.thirdfoot.rto.service;

import jodd.util.StringUtil;
import net.thirdfoot.rto.kernel.jython.PyObjectFactory;
import net.thirdfoot.rto.kernel.media.YoutubeMetadata;
import net.thirdfoot.rto.kernel.media.YoutubeUtil;
import net.thirdfoot.rto.model.exception.InvalidVideoUrlException;
import net.thirdfoot.rto.model.exception.NoSuchVideoException;

/**
 * @author lcsontos
 */
public class VideoServiceImpl implements VideoService {

  @Override
  public String checkVideo(String url)
    throws InvalidVideoUrlException, NoSuchVideoException {

    String nativeId = YoutubeUtil.parseUrl(url);

    if (StringUtil.isBlank(nativeId)) {
      throw new InvalidVideoUrlException();
    }

    YoutubeMetadata youtubeStreamer = PyObjectFactory.create(url);

    return null;
  }

}