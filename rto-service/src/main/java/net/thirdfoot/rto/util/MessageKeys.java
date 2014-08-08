package net.thirdfoot.rto.util;

import net.thirdfoot.rto.kernel.config.PropsKey;

/**
 * @author lcsontos
 */
public enum MessageKeys implements PropsKey {

  THE_SPECIFIED_VIDEO_DOES_NOT_EXIST("the-specified-video-does-not-exist"),
  THE_SPECIFIED_VIDEO_URL_IS_INVALID("the-specified-video-url-is-invalid"),
  THE_SPECIFIED_VIDEO_URL_IS_INVALID_REASON_X("the-specified-video-url-is-invalid-reason-x");

  private MessageKeys(String key) {
    _key = key;
  }

  @Override
  public String getKey() {
    return _key;
  }

  private String _key;

}