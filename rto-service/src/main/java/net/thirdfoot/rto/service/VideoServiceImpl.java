package net.thirdfoot.rto.service;

import java.io.File;
import java.io.IOException;

import jodd.io.FileUtil;
import jodd.io.FileUtilParams;
import jodd.util.StringBand;
import jodd.util.StringPool;
import jodd.util.StringUtil;

import net.thirdfoot.rto.kernel.exception.ApplicationException;
import net.thirdfoot.rto.kernel.util.FileSystemUtil;
import net.thirdfoot.rto.media.YoutubeException;
import net.thirdfoot.rto.media.YoutubeUtil;
import net.thirdfoot.rto.model.Video;
import net.thirdfoot.rto.model.VideoMetadata;
import net.thirdfoot.rto.model.VideoStream;
import net.thirdfoot.rto.model.dao.VideoRepository;
import net.thirdfoot.rto.model.exception.InvalidVideoUrlException;
import net.thirdfoot.rto.model.exception.NoSuchVideoException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lcsontos
 */
@Service
public class VideoServiceImpl implements VideoService {

  @Override
  public void checkVideo(String url)
    throws InvalidVideoUrlException, NoSuchVideoException {

    String videoId = YoutubeUtil.parseUrl(url);

    if (StringUtil.isBlank(videoId)) {
      throw new InvalidVideoUrlException();
    }

    VideoMetadata youtubeMetadata = YoutubeUtil.getYoutubeMetadata(url);

    if (youtubeMetadata == null) {
      throw new NoSuchVideoException();
    }
  }

  @Override
  public Video getVideo(String url) throws ApplicationException {
    checkVideo(url);

    VideoMetadata videoMetadata = YoutubeUtil.getYoutubeMetadata(url);

    String videoId = videoMetadata.getVideoId();

    // Query the DB

    Video video = _videoRepository.findByVideoId(videoId);

    if (video != null) {
      return video;
    }

    // Download video

    File tempFile = YoutubeUtil.getYoutubeVideo(videoMetadata);

    // Move to video repository

    final int grp = 2;
    int len = videoId.length();

    StringBand sb = new StringBand(8 + len + len / grp);

    for (int index = 0; index < len; index++) {
      if (index % grp == 0) {
        sb.append(File.separatorChar);
      }

      sb.append(videoId.charAt(index));
    }

    VideoStream youtubeStream = videoMetadata.getFirstStream();

    sb.append(File.separator);
    sb.append(videoId);
    sb.append(StringPool.UNDERSCORE);
    sb.append(youtubeStream.getMediaType());
    sb.append(StringPool.UNDERSCORE);
    sb.append(youtubeStream.getQuality());
    sb.append(StringPool.DOT);
    sb.append(youtubeStream.getExtension());

    File videoDir = FileSystemUtil.getDataDir(YoutubeUtil.class.getName());

    String videoFile = sb.toString();

    youtubeStream.setVideoFile(videoFile);

    File absoluteVideoFile = new File(videoDir, videoFile);

    try {
      FileUtil.moveFile(
        tempFile, absoluteVideoFile, new FileUtilParams().setCreateDirs(true));
    }
    catch (IOException ioe) {
      throw new YoutubeException(ioe);
    }

    // Save to DB

    video = new Video(videoMetadata);

    return _videoRepository.save(video);
  }

  @Autowired
  private VideoRepository _videoRepository;

}