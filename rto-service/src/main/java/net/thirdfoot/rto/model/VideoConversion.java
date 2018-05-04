package net.thirdfoot.rto.model;

import java.io.Serializable;

/**
 * @author lcsontos
 */
public class VideoConversion implements Serializable {

  public VideoConversion() {
    // TODO Auto-generated constructor stub
  }

  public String getYouTubeUrl() {
    return _youTubeUrl;
  }
  public int getStartTime() {
    return _startTime;
  }
  public int getEndTime() {
    return _endTime;
  }
  public void setYouTubeUrl(String youTubeUrl) {
    this._youTubeUrl = youTubeUrl;
  }
  public void setStartTime(int startTime) {
    this._startTime = startTime;
  }
  public void setEndTime(int endTime) {
    this._endTime = endTime;
  }
  private String _youTubeUrl;
  private int _startTime;
  private int _endTime;

}