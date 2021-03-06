package net.thirdfoot.rto.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.thirdfoot.rto.kernel.exception.ApplicationException;
import net.thirdfoot.rto.kernel.spring.ServiceBean;
import net.thirdfoot.rto.model.Video;
import net.thirdfoot.rto.model.VideoStream;
import net.thirdfoot.rto.model.exception.InvalidVideoUrlException;
import net.thirdfoot.rto.model.exception.NoSuchVideoException;

/**
 * @author lcsontos
 */
@Service
@Transactional
public interface VideoService extends ServiceBean {

  public void checkVideo(String url)
    throws InvalidVideoUrlException, NoSuchVideoException;

  public Video getVideo(long id);

  public Video getVideo(String url) throws ApplicationException;

  public VideoStream getVideoStream(long id);

  public void setVideoFile(long videoId, String videoFile)
    throws ApplicationException;

}