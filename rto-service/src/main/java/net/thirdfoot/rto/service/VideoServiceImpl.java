package net.thirdfoot.rto.service;

import jodd.util.StringUtil;
import net.thirdfoot.rto.kernel.media.YoutubeStreamer;
import net.thirdfoot.rto.kernel.media.YoutubeStreamerFactory;
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

    YoutubeStreamer youtubeStreamer = YoutubeStreamerFactory.create(url);

    return null;
  }

}