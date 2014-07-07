package net.thirdfoot.rto.service;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;

import jodd.datetime.JStopWatch;
import jodd.io.FileUtil;
import jodd.io.FileUtilParams;
import jodd.util.StringBand;
import jodd.util.StringPool;
import jodd.util.StringUtil;
import net.thirdfoot.rto.kernel.exception.ApplicationException;
import net.thirdfoot.rto.kernel.spring.AbstractServiceBean;
import net.thirdfoot.rto.kernel.util.FileSystemUtil;
import net.thirdfoot.rto.media.YoutubeException;
import net.thirdfoot.rto.media.YoutubeUtil;
import net.thirdfoot.rto.model.Video;
import net.thirdfoot.rto.model.VideoMetadata;
import net.thirdfoot.rto.model.VideoStatus;
import net.thirdfoot.rto.model.VideoStream;
import net.thirdfoot.rto.model.dao.VideoRepository;
import net.thirdfoot.rto.model.dao.VideoStreamRepository;
import net.thirdfoot.rto.model.exception.InvalidVideoUrlException;
import net.thirdfoot.rto.model.exception.NoSuchVideoException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author lcsontos
 */
public class VideoServiceImpl
  extends AbstractServiceBean<VideoService> implements VideoService {

  @Override
  public void checkVideo(String url)
    throws InvalidVideoUrlException, NoSuchVideoException {

    String youtubeId = YoutubeUtil.parseUrl(url);

    if (StringUtil.isBlank(youtubeId)) {
      throw new InvalidVideoUrlException();
    }

    Video video = _videoRepository.findByYoutubeId(youtubeId);

    VideoMetadata videoMetadata = null;

    if (video != null) {
      VideoStatus videoStatus = video.getStatus();

      switch (videoStatus) {
        case DELETED:
          throw new NoSuchVideoException();

        case EXPIRED:
        case ORPHAN:
          videoMetadata = YoutubeUtil.getYoutubeMetadata(url);

          VideoStatus newStatus =
            (videoMetadata == null) ? VideoStatus.DELETED : VideoStatus.NEW;

          video.setStatus(newStatus);

          _videoRepository.save(video);

          if (VideoStatus.DELETED.equals(newStatus)) {
            throw new NoSuchVideoException();
          }

        case NEW:
        case VALID:
          videoMetadata = video.getVideoMetadata();
      }
    }
    else {
      videoMetadata = YoutubeUtil.getYoutubeMetadata(url);
    }

    if (videoMetadata != null) {
      return;
    }

    throw new NoSuchVideoException();
  }

  @Override
  public Class<VideoService> getServiceClass() {
    return VideoService.class;
  }

  @Override
  public Video getVideo(long id) {
    return _videoRepository.findOne(id);
  }

  @Override
  public Video getVideo(String url) throws ApplicationException {
    checkVideo(url);

    VideoMetadata videoMetadata = YoutubeUtil.getYoutubeMetadata(url);

    String youtubeId = videoMetadata.getYoutubeId();

    // Query the DB

    Video video = _videoRepository.findByYoutubeId(youtubeId);

    if (video != null) {
      return video;
    }

    // Save to DB

    video = new Video(videoMetadata);

    video = _videoRepository.save(video);

    // Download video

    Runnable runnable = new GetYoutubeVideoRunnable(video.getPrimaryKey());

    _executorService.submit(runnable);

    return video;
  }

  @Override
  public VideoStream getVideoStream(long id) {
    return _videoStreamRepository.findOne(id);
  }

  @Override
  public void setVideoFile(long videoId, String videoFile)
      throws ApplicationException {

    Video video = _videoRepository.findOne(videoId);

    if (video == null) {
      throw new NoSuchVideoException();
    }

    VideoStatus videoStatus =
      (videoFile == null) ? VideoStatus.ORPHAN : VideoStatus.VALID;

    video.setStatus(videoStatus);

    VideoMetadata videoMetadata = video.getVideoMetadata();

    VideoStream videoStream = videoMetadata.getFirstStream();

    videoStream.setVideoFile(videoFile);

    _videoStreamRepository.save(videoStream);
  }

  private static Logger _log = LoggerFactory.getLogger(VideoServiceImpl.class);

  @Autowired
  private ExecutorService _executorService;

  @Autowired
  private VideoRepository _videoRepository;

  @Autowired
  private VideoStreamRepository _videoStreamRepository;

  private class GetYoutubeVideoRunnable implements Runnable {

    public GetYoutubeVideoRunnable(long videoId) {
      _videoId = videoId;
    }

    @Override
    public void run() {
      Video video = instance.getVideo(_videoId);

      if (video == null) {
        if (_log.isWarnEnabled()) {
          _log.warn("Video with ID " + _videoId + " doesn't exist");
        }

        return;
      }
      else if (!VideoStatus.NEW.equals(video.getStatus())) {
        if (_log.isDebugEnabled()) {
          _log.debug("Another process updated video with ID " + _videoId);
        }

        return;
      }

      VideoMetadata videoMetadata = video.getVideoMetadata();

      String youtubeId = videoMetadata.getYoutubeId();

      JStopWatch stopWatch = null;

      if (_log.isDebugEnabled()) {
        stopWatch = new JStopWatch();

        stopWatch.start();

        _log.debug("Download of video " + youtubeId + " has started");
      }

      String videoFile = null;

      try {
        File tempFile = YoutubeUtil.getYoutubeVideo(videoMetadata);
  
        if (stopWatch != null) {
          _log.debug("Download completed under " + stopWatch.span() + "ms");
        }
  
        final int grp = 2;
        int len = youtubeId.length();
  
        StringBand sb = new StringBand(8 + len + len / grp);
  
        for (int index = 0; index < len; index++) {
          if (index % grp == 0) {
            sb.append(File.separatorChar);
          }
  
          sb.append(youtubeId.charAt(index));
        }
  
        VideoStream youtubeStream = videoMetadata.getFirstStream();
  
        sb.append(File.separator);
        sb.append(youtubeId);
        sb.append(StringPool.UNDERSCORE);
        sb.append(youtubeStream.getMediaType());
        sb.append(StringPool.UNDERSCORE);
        sb.append(youtubeStream.getQuality());
        sb.append(StringPool.DOT);
        sb.append(youtubeStream.getExtension());
  
        File videoDir = FileSystemUtil.getDataDir(YoutubeUtil.class.getName());
  
        videoFile = sb.toString();
  
        File absoluteVideoFile = new File(videoDir, videoFile);
  
        if (_log.isDebugEnabled()) {
          _log.debug("Moving file to " + absoluteVideoFile);
        }

        FileUtil.moveFile(
          tempFile, absoluteVideoFile,
          new FileUtilParams().setCreateDirs(true));

        if (_log.isDebugEnabled()) {
          _log.debug("Download of " + youtubeId + " has been finished");
        }
      }
      catch (IOException | YoutubeException e) {
        videoFile = null;

        if (_log.isDebugEnabled()) {
          _log.debug(e.getMessage(), e);
        }
        else if (_log.isWarnEnabled()) {
          _log.warn(e.getMessage());
        }
      }

      try {
        instance.setVideoFile(_videoId, videoFile);
      }
      catch (ApplicationException ae) {
        _log.error(ae.getMessage(), ae);
      }
    }

    long _videoId;
  }

}