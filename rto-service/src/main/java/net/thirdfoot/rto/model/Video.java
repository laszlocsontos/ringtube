package net.thirdfoot.rto.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;

import net.thirdfoot.rto.kernel.model.BaseModel;

/**
 * @author lcsontos
 */
@Entity
public class Video extends BaseModel {

  public Video() {
  }

  public Video(VideoMetadata videoMetadata) {
    setVideoMetadata(videoMetadata);
    setVideoStreams(videoMetadata.getStreams());
  }

  public int getConvertCount() {
    return _convertCount;
  }

  public VideoStatus getStatus() {
    return _status;
  }

  public VideoMetadata getVideoMetadata() {
    return _videoMetadata;
  }

  public List<VideoStream> getVideoStreams() {
    return _videoStreams;
  }

  public int getViewCount() {
    return _viewCount;
  }

  public void setConvertCount(int convertCount) {
    _convertCount = convertCount;
  }

  public void setStatus(VideoStatus status) {
    _status = status;
  }

  public void setVideoMetadata(VideoMetadata videoMetadata) {
    _videoMetadata = videoMetadata;
  }

  public void setVideoStreams(List<VideoStream> videoStreams) {
    _videoStreams = videoStreams;
  }

  public void setViewCount(int viewCount) {
    _viewCount = viewCount;
  }

  @Column(name = "CONVERT_COUNT")
  private int _convertCount;

  @Column(name = "STATUS")
  @Enumerated(EnumType.STRING)
  private VideoStatus _status;

  @Embedded
  private VideoMetadata _videoMetadata;

  @OneToMany(mappedBy = "_video", cascade = CascadeType.MERGE)
  private List<VideoStream> _videoStreams;

  @Column(name = "VIEW_COUNT")
  private int _viewCount;

}