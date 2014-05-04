package net.thirdfoot.rto.kernel.media;

import java.io.Serializable;
import java.util.List;

/**
 * @author lcsontos
 */
public interface YoutubeMetadata extends Serializable {

  public String getVideoAuthor();

  public int getVideoLength();

  public double getVideoRating();

  public List<YoutubeStream> getAllStreams();

  public String getVideoTitle();

}