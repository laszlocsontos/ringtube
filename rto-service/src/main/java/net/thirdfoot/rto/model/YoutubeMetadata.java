package net.thirdfoot.rto.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.python.core.PyObject;

/**
 * @author lcsontos
 */
public class YoutubeMetadata implements Serializable {

  public YoutubeMetadata() {
  }

  public YoutubeMetadata(PyObject pyYoutubeMetadata) {
    _author = pyYoutubeMetadata.__getattr__("author").asString();
    _category = pyYoutubeMetadata.__getattr__("category").asString();
    _length = pyYoutubeMetadata.__getattr__("length").asInt();
    _videoId = pyYoutubeMetadata.__getattr__("videoid").asString();
    _published = pyYoutubeMetadata.__getattr__("published").asString();

    Iterable<PyObject> streams = pyYoutubeMetadata.__getattr__(
      "streams").asIterable();

    _streams = new ArrayList<YoutubeStream>();

    for (PyObject pyYoutubeStream : streams) {
      _streams.add(new YoutubeStream(pyYoutubeStream));
    }

    _title = pyYoutubeMetadata.__getattr__("title").asString();
  }

  public String getAuthor() {
    return _author;
  }

  public String getCategory() {
    return _category;
  }

  public YoutubeStream getFirstStream() {
    if (_firstStream != null) {
      return _firstStream;
    }

    int len = _streams.size();

    if (len < 1) {
      return null;
    }

    YoutubeStream[] streams = new YoutubeStream[len];

    streams = _streams.toArray(streams);

    Arrays.sort(streams);

    return (_firstStream = streams[0]);
  }

  public int getLength() {
    return _length;
  }

  public String getVideoId() {
    return _videoId;
  }

  public String getPublished() {
    return _published;
  }

  public List<YoutubeStream> getStreams() {
    return _streams;
  }

  public String getTitle() {
    return _title;
  }

  public void setAuthor(String author) {
    _author = author;
  }

  public void setCategory(String category) {
    _category = category;
  }

  public void setLength(int length) {
    _length = length;
  }

  public void setVideoId(String videoId) {
    _videoId = videoId;
  }

  public void setPublished(String published) {
    _published = published;
  }

  public void setStreams(List<YoutubeStream> streams) {
    _firstStream = null;
    _streams = streams;
  }

  public void setTitle(String title) {
    _title = title;
  }

  private String _author;
  private String _category;
  private YoutubeStream _firstStream;
  private int _length;
  private String _videoId;
  private String _published;
  private List<YoutubeStream> _streams;
  private String _title;

}