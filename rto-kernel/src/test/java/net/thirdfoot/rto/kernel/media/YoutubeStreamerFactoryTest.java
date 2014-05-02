package net.thirdfoot.rto.kernel.media;

import java.util.Iterator;
import java.util.List;

import net.thirdfoot.rto.kernel.media.YoutubeStream;
import net.thirdfoot.rto.kernel.media.YoutubeStreamer;
import net.thirdfoot.rto.kernel.media.YoutubeStreamerFactory;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lcsontos
 */
public class YoutubeStreamerFactoryTest {

  @Test(expected = YoutubeStreamerException.class)
  public void testCreateWithWrongUrl() {
    YoutubeStreamerFactory.create(_INVALID_URL);
  }

  @Test
  public void testCreate() {
    YoutubeStreamer streamer = YoutubeStreamerFactory.create(_VALID_URL);

    Assert.assertNotNull(streamer);

    List<YoutubeStream> videoStreams = streamer.getAllStreams();

    Assert.assertNotNull(videoStreams);
    Assert.assertFalse(videoStreams.isEmpty());

    YoutubeStream stream = null;

    for (Iterator<YoutubeStream> iterator = videoStreams.iterator();
      iterator.hasNext();) {

      YoutubeStream audioStream = (YoutubeStream)iterator.next();

      if (audioStream.getMediaType().equals("audio")) {
        stream = audioStream;

        break;
      }
    }

    Assert.assertNotNull(stream);

    Assert.assertNotNull(stream.getExtension());
    Assert.assertNotNull(stream.getMediaType());
    Assert.assertNotNull(stream.getQuality());
    Assert.assertNotNull(stream.getResolution());
    Assert.assertTrue(stream.getSize() > 0);
    Assert.assertNotNull(stream.getUrl());

    _log.info("URL: " + stream.getUrl());
  }

  private static Logger _log = LoggerFactory.getLogger(
    YoutubeStreamerFactoryTest.class);

  private final String _INVALID_URL = "https://www.youtube.com/watch?v=wrongId";
  private final String _VALID_URL =
    "https://www.youtube.com/watch?v=W2yk1lsi9RM";

}
