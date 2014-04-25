package net.thirdfoot.rto.kernel.youtube;

import java.util.List;

/**
 * @author lcsontos
 */
public interface YoutubeStreamer {

  public String getVideoAuthor();

  public int getVideoLength();

  public double getVideoRating();

  public List<YoutubeStream> getAllStreams();

  public String getVideoTitle();

}