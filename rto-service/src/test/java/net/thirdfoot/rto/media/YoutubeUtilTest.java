package net.thirdfoot.rto.media;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import jodd.io.FileUtil;
import jodd.util.StringPool;
import net.thirdfoot.rto.kernel.util.FileSystemUtil;
import net.thirdfoot.rto.media.ConversionAttribute;
import net.thirdfoot.rto.media.ConversionContext;
import net.thirdfoot.rto.media.YoutubeException;
import net.thirdfoot.rto.media.YoutubeUtil;
import net.thirdfoot.rto.model.YoutubeMetadata;
import net.thirdfoot.rto.model.YoutubeStream;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lcsontos
 */
@PowerMockIgnore("javax.management.*")
@PrepareForTest(FileSystemUtil.class)
@RunWith(PowerMockRunner.class)
public class YoutubeUtilTest {

  @BeforeClass
  public static void setUpClass() throws Exception {
    PowerMockito.spy(FileSystemUtil.class);

    File mockDir = FileUtil.createTempDirectory();

    _log.info("mockDir: " + mockDir);

    PowerMockito.doReturn(
      mockDir).when(
        FileSystemUtil.class, "_getDir", Matchers.any(), Matchers.any());
  }

  @Test
  public void testCutYoutubeVideo() throws Exception {
    ConversionContext context =
      new ConversionContext()
        .set(ConversionAttribute.START_TIMESTAMP, 450)
        .set(ConversionAttribute.END_TIMESTAMP, 480)
        .set(ConversionAttribute.OUTPUT_FORMAT, "mp3");

    String outFileUrl = YoutubeUtil.cutYoutubeVideo(
      "/home/lcsontos/Music/jodd-1620230518342849577.m4a", context);

    System.out.println(outFileUrl);
  }

  @Test(expected = YoutubeException.class)
  public void testGetYoutubeMetadataWithWrongUrl() {
    YoutubeUtil.getYoutubeMetadata(_INVALID_URL);
  }

  @Test
  public void testGetYoutubeMetadata() {
    YoutubeMetadata streamer = YoutubeUtil.getYoutubeMetadata(_VALID_URL);

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
  public void testGetYoutubeVideo() {
    File youtubeVideo = YoutubeUtil.getYoutubeVideo(_DOWNLOAD_URL);

    Assert.assertNotNull(youtubeVideo);
  }

  @Test
  public void testParseUrl() {
    Assert.assertNull(YoutubeUtil.parseUrl(null));
    Assert.assertNull(YoutubeUtil.parseUrl(StringPool.EMPTY));
    Assert.assertNull(YoutubeUtil.parseUrl("aaaaaaaaaa"));
    Assert.assertNull(YoutubeUtil.parseUrl("http://index.hu"));
    Assert.assertNull(YoutubeUtil.parseUrl("https://www.youtube.com/watch?v="));

    String[] results = new String[] {
      YoutubeUtil.parseUrl("https://www.youtube.com/watch?v=qrx1vyvtRLY"),
      YoutubeUtil.parseUrl("http://www.youtube.com/watch?v=qrx1vyvtRLY"),
      YoutubeUtil.parseUrl("www.youtube.com/watch?v=qrx1vyvtRLY"),
      YoutubeUtil.parseUrl("youtube.com/watch?v=qrx1vyvtRLY")
    };

    Assert.assertArrayEquals(
      new String[] {"qrx1vyvtRLY", "qrx1vyvtRLY", null, null}, results);
  }

  private static Logger _log = LoggerFactory.getLogger(
    YoutubeUtilTest.class);

  private final String _DOWNLOAD_URL =
    "http://www.youtube.com/watch?v=nGdFHJXciAQ";
  private final String _INVALID_URL = "https://www.youtube.com/watch?v=wrongId";
  private final String _VALID_URL =
    "https://www.youtube.com/watch?v=W2yk1lsi9RM";

}