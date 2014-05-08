package net.thirdfoot.rto.service;

import jodd.util.StringUtil;

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

    String videoId = YoutubeUtil.parseUrl(url);

    if (StringUtil.isBlank(videoId)) {
      throw new InvalidVideoUrlException();
    }

    YoutubeMetadata youtubeMetadata = YoutubeUtil.getYoutubeMetadata(url);

    return null;
  }

}