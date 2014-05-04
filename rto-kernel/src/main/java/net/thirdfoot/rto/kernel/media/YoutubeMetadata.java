package net.thirdfoot.rto.kernel.media;

import java.util.List;

/**
 * @author lcsontos
 */
public interface YoutubeMetadata {

  public String getVideoAuthor();

  public int getVideoLength();

  public double getVideoRating();

  public List<YoutubeStream> getAllStreams();

  public String getVideoTitle();

}