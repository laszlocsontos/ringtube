package net.thirdfoot.rto.kernel.media;

import java.util.Iterator;
import java.util.List;

import jodd.util.ObjectUtil;
import net.thirdfoot.rto.kernel.jython.YoutubeMetadataFactory;
import net.thirdfoot.rto.kernel.media.YoutubeStream;
import net.thirdfoot.rto.kernel.media.YoutubeMetadata;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lcsontos
 */
public class YoutubeMetadataFactoryTest {

  @Test(expected = YoutubeException.class)
  public void testCreateWithWrongUrl() {
    YoutubeMetadataFactory.create(_INVALID_URL);
  }

  @Test
  public void testCreate() {
    YoutubeMetadata streamer = YoutubeMetadataFactory.create(_VALID_URL);

    Assert.assertNotNull(streamer);

    List<YoutubeStream> videoStreams = streamer.getStreams();

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

  @Test
  public void testSerialize() throws Exception {
    YoutubeMetadata expectedMetadata = YoutubeMetadataFactory.create(
      _VALID_URL);

    byte[] data = ObjectUtil.objectToByteArray(expectedMetadata);
    System.out.println(data.length);
    YoutubeMetadata actualMetadata =
      (YoutubeMetadata)ObjectUtil.byteArrayToObject(data);

    /*Assert.assertEquals(
      expectedMetadata.getStreams(), actualMetadata.getStreams());*/
    Assert.assertEquals(
      expectedMetadata.getAuthor(), actualMetadata.getAuthor());
    Assert.assertEquals(
      expectedMetadata.getLength(), actualMetadata.getLength());
    Assert.assertEquals(
      expectedMetadata.getTitle(), actualMetadata.getTitle());
  }

  private static Logger _log = LoggerFactory.getLogger(
    YoutubeMetadataFactoryTest.class);

  private final String _INVALID_URL = "https://www.youtube.com/watch?v=wrongId";
  private final String _VALID_URL =
    "https://www.youtube.com/watch?v=W2yk1lsi9RM";

}