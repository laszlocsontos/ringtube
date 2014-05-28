package net.thirdfoot.rto.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import jodd.util.StringUtil;
import net.thirdfoot.rto.kernel.model.BaseModel;

import org.python.core.PyObject;

/**
 * @author lcsontos
 */
@Entity
public class VideoStream
  extends BaseModel implements Comparable<VideoStream>, Serializable {

  public VideoStream() {
  }

  public VideoStream(PyObject pyYoutubeStream) {
    _extension = pyYoutubeStream.__getattr__("extension").asString();
    _mediaType = pyYoutubeStream.__getattr__("mediatype").asString();
    _resolution = pyYoutubeStream.__getattr__("resolution").asString();
    _quality = pyYoutubeStream.__getattr__("quality").asString();
    _size = pyYoutubeStream.__getattr__("size").asInt();
    _url = pyYoutubeStream.__getattr__("url").asString();
  }

  @Override
  public int compareTo(VideoStream other) {
    if (other == null) {
      throw new IllegalArgumentException("parameter cannot be null");
    }

    int otherQuality = doGetQuality(other.getQuality());
    int thisQuality = doGetQuality(_quality);

    return (thisQuality - otherQuality);
  }

  public String getExtension() {
    return _extension;
  }

  public String getMediaType() {
    return _mediaType;
  }

  public String getResolution() {
    return _resolution;
  }

  public String getQuality() {
    return _quality;
  }

  public int getSize() {
    return _size;
  }

  public String getUrl() {
    return _url;
  }

  public Video getVideo() {
    return _video;
  }

  public void setExtension(String extension) {
    _extension = extension;
  }

  public void setMediaType(String mediaType) {
    _mediaType = mediaType;
  }

  public void setResolution(String resolution) {
    _resolution = resolution;
  }

  public void setQuality(String quality) {
    _quality = quality;
  }

  public void setSize(int size) {
    _size = size;
  }

  public void setUrl(String url) {
    _url = url;
  }

  public void setVideo(Video video) {
    _video = video;
  }

  protected int doGetQuality(String quality) {
    if (StringUtil.isBlank(quality)) {
      throw new IllegalArgumentException("quality cannot be null or empty");
    }

    if (StringUtil.endsWithChar(quality, 'k')) {
      int len = quality.length();

      quality = quality.substring(0, len - 1);

      return Integer.valueOf(quality);
    }

    String[] tokens = StringUtil.splitc(quality, 'x');

    if (tokens.length != 2) {
      throw new IllegalArgumentException(
        "The value of quality could not be parsed");
    }

    int w = Integer.valueOf(tokens[0]);
    int h = Integer.valueOf(tokens[1]);

    return (w * h);
  }

  @Column(name = "EXTENSION")
  private String _extension;

  @Column(name = "MEDIA_TYPE")
  private String _mediaType;

  @Column(name = "RESOLUTION")
  private String _resolution;

  @Column(name = "QUALITY")
  private String _quality;

  @Column(name = "SIZE")
  private int _size;

  @Transient
  private String _url;

  @JoinColumn(name = "VIDEO_ID")
  @ManyToOne(fetch = FetchType.LAZY)
  private Video _video;

}