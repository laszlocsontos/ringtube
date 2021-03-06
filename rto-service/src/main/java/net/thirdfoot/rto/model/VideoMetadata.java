package net.thirdfoot.rto.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.python.core.PyObject;

/**
 * @author lcsontos
 */
@Embeddable
public class VideoMetadata implements Cloneable, Serializable {

  public VideoMetadata() {
  }

  public VideoMetadata(PyObject pyYouTubeMetadata) {
    _author = pyYouTubeMetadata.__getattr__("author").asString();
    _category = pyYouTubeMetadata.__getattr__("category").asString();
    _length = pyYouTubeMetadata.__getattr__("length").asInt();
    _published = pyYouTubeMetadata.__getattr__("published").asString();

    Iterable<PyObject> streams = pyYouTubeMetadata.__getattr__(
      "streams").asIterable();

    _streams = new ArrayList<VideoStream>();

    for (PyObject pyYouTubeStream : streams) {
      _streams.add(new VideoStream(pyYouTubeStream));
    }

    _title = pyYouTubeMetadata.__getattr__("title").asString();

    _youTubeId = pyYouTubeMetadata.__getattr__("videoid").asString();
  }

  @Override
  public VideoMetadata clone() {
    VideoMetadata videoMetadata = null;

    try {
      videoMetadata = (VideoMetadata)super.clone();
    }
    catch (CloneNotSupportedException cnse) {
      // This isn't possible
    }

    // Do not copy streams

    videoMetadata.setStreams(null);

    return videoMetadata;
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

  public String getPublished() {
    return _published;
  }

  public List<VideoStream> getStreams() {
    return _streams;
  }

  public String getTitle() {
    return _title;
  }

  public String getYouTubeId() {
    return _youTubeId;
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

  public void setPublished(String published) {
    _published = published;
  }

  public void setStreams(List<VideoStream> streams) {
    _firstStream = null;

    if (streams == null) {
      streams = Collections.emptyList();
    }

    _streams = streams;
  }

  public void setTitle(String title) {
    _title = title;
  }

  public void setYouTubeId(String youTubeId) {
    _youTubeId = youTubeId;
  }

  @Column(name = "AUTHOR")
  private String _author;

  @Column(name = "CATEGORY")
  private String _category;

  @Transient
  private VideoStream _firstStream;

  @Column(name = "LENGTH")
  private int _length;

  @Column(name = "PUBLISHED")
  private String _published;

  @OneToMany(mappedBy = "_video", cascade = CascadeType.MERGE)
  private List<VideoStream> _streams;

  @Column(name = "TITLE")
  private String _title;

  @Column(name = "YOUTUBE_ID")
  private String _youTubeId;

}