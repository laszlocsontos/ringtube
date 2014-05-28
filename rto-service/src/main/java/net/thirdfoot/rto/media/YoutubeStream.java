package net.thirdfoot.rto.media;

import java.io.Serializable;

import jodd.util.StringUtil;

import org.python.core.PyObject;

/**
 * @author lcsontos
 */
public class YoutubeStream implements Comparable<YoutubeStream>, Serializable {

  public YoutubeStream() {
  }

  public YoutubeStream(PyObject pyYoutubeStream) {
    _extension = pyYoutubeStream.__getattr__("extension").asString();
    _mediaType = pyYoutubeStream.__getattr__("mediatype").asString();
    _resolution = pyYoutubeStream.__getattr__("resolution").asString();
    _quality = pyYoutubeStream.__getattr__("quality").asString();
    _size = pyYoutubeStream.__getattr__("size").asInt();
    _url = pyYoutubeStream.__getattr__("url").asString();
  }

  @Override
  public int compareTo(YoutubeStream other) {
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

  private String _extension;
  private String _mediaType;
  private String _resolution;
  private String _quality;
  private int _size;
  private String _url;

}