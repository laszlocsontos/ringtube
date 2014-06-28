package net.thirdfoot.rto.model.dao;

import net.thirdfoot.rto.model.VideoStream;

import org.springframework.data.repository.CrudRepository;

/**
 * @author lcsontos
 */
public interface VideoStreamRepository
  extends CrudRepository<VideoStream, Long> {

}