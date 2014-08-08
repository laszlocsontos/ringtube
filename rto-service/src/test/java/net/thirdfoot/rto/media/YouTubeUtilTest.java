package net.thirdfoot.rto.media;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import jodd.io.FileUtil;
import jodd.util.StringPool;

import net.thirdfoot.rto.kernel.util.FileSystemUtil;
import net.thirdfoot.rto.media.ConversionAttribute;
import net.thirdfoot.rto.media.ConversionContext;
import net.thirdfoot.rto.media.YouTubeException;
import net.thirdfoot.rto.media.YouTubeUtil;
import net.thirdfoot.rto.model.VideoMetadata;
import net.thirdfoot.rto.model.VideoStream;

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
public class YouTubeUtilTest {

  public static final String DOWNLOAD_URL =
    "http://www.youtube.com/watch?v=nGdFHJXciAQ";

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
  public void testCutYouTubeVideo() throws Exception {
    ConversionContext context =
      new ConversionContext()
        .set(ConversionAttribute.START_TIMESTAMP, 450)
        .set(ConversionAttribute.END_TIMESTAMP, 480)
        .set(ConversionAttribute.OUTPUT_FORMAT, "mp3");

    String outFileUrl = YouTubeUtil.cutYouTubeVideo(
      "/home/lcsontos/Music/jodd-1620230518342849577.m4a", context);

    System.out.println(outFileUrl);
  }

  @Test(expected = YouTubeException.class)
  public void testGetYouTubeMetadataWithWrongUrl() throws YouTubeException {
    YouTubeUtil.getYouTubeMetadata(_INVALID_URL);
  }

  @Test
  public void testGetYouTubeMetadata() throws YouTubeException {
    VideoMetadata streamer = YouTubeUtil.getYouTubeMetadata(_VALID_URL);

    Assert.assertNotNull(streamer);

    List<VideoStream> videoStreams = streamer.getStreams();

    Assert.assertNotNull(videoStreams);
    Assert.assertFalse(videoStreams.isEmpty());

    VideoStream stream = null;

    for (Iterator<VideoStream> iterator = videoStreams.iterator();
      iterator.hasNext();) {

      VideoStream audioStream = (VideoStream)iterator.next();

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
  public void testGetYouTubeVideo() throws YouTubeException {
    VideoMetadata videoMetadata = YouTubeUtil.getYouTubeMetadata(DOWNLOAD_URL);

    File youTubeVideo = YouTubeUtil.getYouTubeVideo(videoMetadata);

    Assert.assertNotNull(youTubeVideo);
  }

  @Test
  public void testParseUrl() {
    Assert.assertNull(YouTubeUtil.parseUrl(null));
    Assert.assertNull(YouTubeUtil.parseUrl(StringPool.EMPTY));
    Assert.assertNull(YouTubeUtil.parseUrl("aaaaaaaaaa"));
    Assert.assertNull(YouTubeUtil.parseUrl("http://index.hu"));
    Assert.assertNull(YouTubeUtil.parseUrl("https://www.youtube.com/watch?v="));

    String[] results = new String[] {
      YouTubeUtil.parseUrl("https://www.youtube.com/watch?v=qrx1vyvtRLY"),
      YouTubeUtil.parseUrl("http://www.youtube.com/watch?v=qrx1vyvtRLY"),
      YouTubeUtil.parseUrl("www.youtube.com/watch?v=qrx1vyvtRLY"),
      YouTubeUtil.parseUrl("youtube.com/watch?v=qrx1vyvtRLY")
    };

    Assert.assertArrayEquals(
      new String[] {"qrx1vyvtRLY", "qrx1vyvtRLY", null, null}, results);
  }

  private static Logger _log = LoggerFactory.getLogger(
    YouTubeUtilTest.class);

  private final String _INVALID_URL = "https://www.youtube.com/watch?v=wrongId";
  private final String _VALID_URL =
    "https://www.youtube.com/watch?v=kRrIAqCdjCI";

}