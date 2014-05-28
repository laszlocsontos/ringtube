package net.thirdfoot.rto.model.dao;

import net.thirdfoot.rto.model.Video;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author lcsontos
 */
@Repository
public interface VideoRepository extends CrudRepository<Video, Long> {
}