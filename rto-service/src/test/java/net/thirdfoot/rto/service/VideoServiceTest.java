package net.thirdfoot.rto.service;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author lcsontos
 */
@ContextConfiguration(locations = {"/META-INF/service-spring.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class VideoServiceTest {

  @Test
  public void testCheckVideo() throws Exception {
    _videoService.checkVideo("test");
  }

  @Autowired
  private VideoService _videoService;

}