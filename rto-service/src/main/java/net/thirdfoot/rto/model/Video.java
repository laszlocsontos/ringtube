package net.thirdfoot.rto.model;

import javax.persistence.Entity;

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

  public String getNativeId() {
    return _nativeId;
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

  public void setNativeId(String nativeId) {
    _nativeId = nativeId;
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

  public void setViewCount(int viewCount) {
    _viewCount = viewCount;
  }

  // TODO Fix column names
  private String _author;
  private int _convertCount;
  private int _length;
  private String _nativeId;
  private double _rating;
  private int _size;
  private VideoStatus _status;
  private int _viewCount;

}
