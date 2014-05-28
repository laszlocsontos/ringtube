package net.thirdfoot.rto.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;

import net.thirdfoot.rto.kernel.model.BaseModel;

/**
 * @author lcsontos
 */
@Entity
public class Video extends BaseModel {

  public String getAuthor() {
    return _author;
  }

  public int getConvertCount() {
    return _convertCount;
  }

  public int getLength() {
    return _length;
  }

  public double getRating() {
    return _rating;
  }

  public int getSize() {
    return _size;
  }

  public VideoStatus getStatus() {
    return _status;
  }

  public String getVideoId() {
    return _videoId;
  }

  public int getViewCount() {
    return _viewCount;
  }

  public void setAuthor(String author) {
    _author = author;
  }

  public void setConvertCount(int convertCount) {
    _convertCount = convertCount;
  }

  public void setLength(int length) {
    _length = length;
  }

  public void setRating(double rating) {
    _rating = rating;
  }

  public void setSize(int size) {
    _size = size;
  }

  public void setStatus(VideoStatus status) {
    _status = status;
  }

  public void setVideoId(String videoId) {
    _videoId = videoId;
  }

  public void setViewCount(int viewCount) {
    _viewCount = viewCount;
  }

  @Column(name = "AUTHOR")
  private String _author;

  @Column(name = "CONVERT_COUNT")
  private int _convertCount;

  @Column(name = "LENGTH")
  private int _length;

  @Column(name = "VIDEO_ID")
  private String _videoId;

  @Column(name = "RATING")
  private double _rating;

  @Column(name = "SIZE")
  private int _size;

  @Column(name = "STATUS")
  @Enumerated
  private VideoStatus _status;

  @Column(name = "VIEW_COUNT")
  private int _viewCount;

}