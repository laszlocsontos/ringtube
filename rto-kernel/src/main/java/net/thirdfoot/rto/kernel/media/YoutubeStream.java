package net.thirdfoot.rto.kernel.media;

import java.io.Serializable;

/**
 * @author lcsontos
 */
public interface YoutubeStream extends Serializable {

  public String getExtension();

  public String getMediaType();

  public String getResolution();

  public String getQuality();

  public int getSize();

  public String getUrl();

}