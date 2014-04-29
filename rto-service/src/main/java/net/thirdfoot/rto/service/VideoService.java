package net.thirdfoot.rto.service;

import net.thirdfoot.rto.model.exception.NoSuchVideoException;

/**
 * @author lcsontos
 */
public interface VideoService {

  public String checkVideo(String url) throws NoSuchVideoException;

}