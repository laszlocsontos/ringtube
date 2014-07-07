package net.thirdfoot.rto.view;

import net.thirdfoot.rto.kernel.exception.ApplicationException;
import net.thirdfoot.rto.model.Video;
import net.thirdfoot.rto.model.VideoMetadata;
import net.thirdfoot.rto.service.VideoService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author lcsontos
 */
@Controller
@RequestMapping("/video")
public class VideoController {

  @RequestMapping(
      value="/test", method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public String test() {
    return "test";
  }

  @RequestMapping(
    value="/get/{youtubeId}", method = RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE,
    consumes = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public VideoMetadata get(@PathVariable String youtubeId)
    throws ApplicationException {

    String url = "http://youtube.com/watch?v=" + youtubeId;

    Video video = _videoService.getVideo(url);

    VideoMetadata videoMetadata = video.getVideoMetadata();

    return videoMetadata.clone();
  }

  private static Logger _log = LoggerFactory.getLogger(VideoController.class);

  @Autowired
  private VideoService _videoService;

}