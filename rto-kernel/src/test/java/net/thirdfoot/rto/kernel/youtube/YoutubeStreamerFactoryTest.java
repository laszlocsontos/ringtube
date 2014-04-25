package net.thirdfoot.rto.kernel.youtube;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lcsontos
 */
public class YoutubeStreamerFactoryTest {

    @Test
    public void testCreate() {
        YoutubeStreamer streamer = YoutubeStreamerFactory.create(
            "https://www.youtube.com/watch?v=W2yk1lsi9RM");

        Assert.assertNotNull(streamer);

        List<YoutubeStream> videoStreams = streamer.getVideoStreams();

        Assert.assertNotNull(videoStreams);
        Assert.assertFalse(videoStreams.isEmpty());

        YoutubeStream stream = videoStreams.get(0);

        Assert.assertNotNull(stream);

        String extension = stream.getExtension();
        String resolution = stream.getResolution();
        String url = stream.getUrl();

        Assert.assertNotNull(extension);
        Assert.assertNotNull(resolution);
        Assert.assertNotNull(url);

        _log.info("URL: " + url);
    }

    private static Logger _log = LoggerFactory.getLogger(
        YoutubeStreamerFactoryTest.class);

}
