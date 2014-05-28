package net.thirdfoot.rto.service;

import net.thirdfoot.rto.kernel.spring.RootContextConstants;
import net.thirdfoot.rto.kernel.spring.RootContextTestLoader;
import net.thirdfoot.rto.media.YoutubeUtilTest;
import net.thirdfoot.rto.model.Video;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author lcsontos
 */
@ContextConfiguration(
    locations = {
      "classpath:META-INF/kernel-spring.xml",
      "classpath*:META-INF/service-spring.xml"
    })
@RunWith(SpringJUnit4ClassRunner.class)
public class VideoServiceTest {

  @Test
  public void testCheckVideo() throws Exception {
    _videoService.checkVideo("test");
  }

  @Test
  public void testGetVideo() throws Exception {
    Video video = _videoService.getVideo(YoutubeUtilTest.DOWNLOAD_URL);

    Assert.assertNotNull(video);
  }

  @Autowired
  private VideoService _videoService;

}