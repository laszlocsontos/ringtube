package net.thirdfoot.rto.kernel.media;

import jodd.util.StringPool;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author lcsontos
 */
public class YoutubeUtilTest {

  @Test
  public void testparseUrl() {
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

}