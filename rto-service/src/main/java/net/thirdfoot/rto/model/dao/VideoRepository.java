package net.thirdfoot.rto.model.dao;

import net.thirdfoot.rto.model.Video;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author lcsontos
 */
@Repository
public interface VideoRepository extends CrudRepository<Video, Long> {

  @Query("SELECT v FROM Video v WHERE v._videoMetadata._youTubeId = :youTubeId")
  public Video findByYouTubeId(@Param("youTubeId") String youTubeId);

}