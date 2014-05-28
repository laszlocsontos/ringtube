package net.thirdfoot.rto.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jodd.util.StringUtil;

import net.thirdfoot.rto.media.YoutubeUtil;
import net.thirdfoot.rto.model.Video;
import net.thirdfoot.rto.model.VideoMetadata;
import net.thirdfoot.rto.model.dao.VideoRepository;
import net.thirdfoot.rto.model.exception.InvalidVideoUrlException;
import net.thirdfoot.rto.model.exception.NoSuchVideoException;

/**
 * @author lcsontos
 */
@Service
public class VideoServiceImpl implements VideoService {

  @Override
  public String checkVideo(String url)
    throws InvalidVideoUrlException, NoSuchVideoException {

    String videoId = YoutubeUtil.parseUrl(url);

    if (StringUtil.isBlank(videoId)) {
      throw new InvalidVideoUrlException();
    }

    VideoMetadata youtubeMetadata = YoutubeUtil.getYoutubeMetadata(url);

    if (youtubeMetadata == null) {
      throw new NoSuchVideoException();
    }

    return videoId;
  }

  @Override
  public Video getVideo(String url) {
    // TODO Auto-generated method stub
    return null;
  }

  @Autowired
  private VideoRepository _videoRepository;

}