package net.thirdfoot.rto.kernel.media;

import java.io.Serializable;

import org.python.core.PyObject;

/**
 * @author lcsontos
 */
public class YoutubeStream implements Serializable {

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

  private String _extension;
  private String _mediaType;
  private String _resolution;
  private String _quality;
  private int _size;
  private String _url;

}