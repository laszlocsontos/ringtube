package net.thirdfoot.rto.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

import org.python.core.PyObject;

/**
 * @author lcsontos
 */
@Embeddable
public class VideoMetadata implements Serializable {

  public VideoMetadata() {
  }

  public VideoMetadata(PyObject pyYoutubeMetadata) {
    _author = pyYoutubeMetadata.__getattr__("author").asString();
    _category = pyYoutubeMetadata.__getattr__("category").asString();
    _length = pyYoutubeMetadata.__getattr__("length").asInt();
    _videoId = pyYoutubeMetadata.__getattr__("videoid").asString();
    _published = pyYoutubeMetadata.__getattr__("published").asString();

    Iterable<PyObject> streams = pyYoutubeMetadata.__getattr__(
      "streams").asIterable();

    _streams = new ArrayList<VideoStream>();

    for (PyObject pyYoutubeStream : streams) {
      _streams.add(new VideoStream(pyYoutubeStream));
    }

    _title = pyYoutubeMetadata.__getattr__("title").asString();
  }

  public String getAuthor() {
    return _author;
  }

  public String getCategory() {
    return _category;
  }

  public VideoStream getFirstStream() {
    if (_firstStream != null) {
      return _firstStream;
    }

    int len = _streams.size();

    if (len < 1) {
      return null;
    }

    VideoStream[] streams = new VideoStream[len];

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

  public List<VideoStream> getStreams() {
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

  public void setStreams(List<VideoStream> streams) {
    _firstStream = null;
    _streams = streams;
  }

  public void setTitle(String title) {
    _title = title;
  }

  @Column(name = "AUTHOR")
  private String _author;

  @Column(name = "CATEGORY")
  private String _category;

  @Transient
  private VideoStream _firstStream;

  @Column(name = "LENGTH")
  private int _length;

  @Column(name = "VIDEO_ID")
  private String _videoId;

  @Column(name = "PUBLISHED")
  private String _published;

  @Transient
  private List<VideoStream> _streams;

  @Column(name = "TITLE")
  private String _title;

}