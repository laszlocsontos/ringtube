package net.thirdfoot.rto.media;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jodd.io.FileUtil;
import jodd.io.FileUtilParams;
import jodd.io.StreamUtil;
import jodd.util.StringBand;
import jodd.util.StringPool;
import jodd.util.StringUtil;

import net.thirdfoot.rto.kernel.jython.PyObjectFactory;
import net.thirdfoot.rto.kernel.jython.PyObjectFactoryUtil;
import net.thirdfoot.rto.kernel.util.FileSystemUtil;
import net.thirdfoot.rto.model.VideoMetadata;
import net.thirdfoot.rto.model.VideoStream;

import org.python.core.PyException;
import org.python.core.PyObject;
import org.python.core.PyString;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lcsontos
 */
public class YoutubeUtil {

  public static String cutYoutubeVideo(String inFile, ConversionContext context)
    throws Exception {

    File outFile = FileSystemUtil.createTempFile(
      YoutubeUtil.class.getName(), null, null);

    Converter converter = new AudioConverter(
      inFile, outFile.getPath(), context);

    converter.convert();

    return outFile.getPath();
  }

  public static VideoMetadata getYoutubeMetadata(String url) {
    if (StringUtil.isBlank(url)) {
      throw new NullPointerException("url is null or empty");
    }

    String videoId = parseUrl(url);

    if (StringUtil.isBlank(videoId)) {
      return null;
    }

    VideoMetadata youtubeMetadata = youtubeMetadataCache.get(videoId);

    if (youtubeMetadata != null) {
      return youtubeMetadata;
    }

    youtubeMetadata = _getYoutubeMetadata(url);

    youtubeMetadataCache.putIfAbsent(videoId, youtubeMetadata);

    return youtubeMetadata;
  }

  public static File getYoutubeVideo(VideoMetadata youtubeMetadata) {
    if (youtubeMetadata == null) {
      throw new IllegalArgumentException("youtubeMetadata is null");
    }

    VideoStream youtubeStream = youtubeMetadata.getFirstStream();

    URL youtubeUrl = null;

    try {
      youtubeUrl = new URL(youtubeStream.getUrl());
    }
    catch (MalformedURLException mue) {
      _log.error(mue.getMessage(), mue);
    }

    // Download video

    File tempFile = null;
    FileOutputStream fileOutputStream = null;

    String videoId = youtubeMetadata.getVideoId();

    try {
      InputStream inputStream = youtubeUrl.openStream();
      ReadableByteChannel in = Channels.newChannel(inputStream);

      tempFile = FileSystemUtil.createTempFile(
        YoutubeUtil.class.getName(), videoId, youtubeStream.getExtension());

      fileOutputStream = new FileOutputStream(tempFile);

      fileOutputStream.getChannel().transferFrom(
        in, 0, youtubeStream.getSize());
    }
    catch (IOException ioe) {
      throw new YoutubeException(ioe);
    }
    finally {
      StreamUtil.close(fileOutputStream);
    }

    return tempFile;
  }

  public static String parseUrl(String url) {
    if (StringUtil.isBlank(url)) {
      return null;
    }

    Matcher matcher = _YOUTUBE_URL_PATTERN.matcher(url);

    if (!matcher.matches()) {
      return null;
    }

    return matcher.group(1);
  }

  private static VideoMetadata _getYoutubeMetadata(String url) {
    try {
      PyObjectFactory pyYoutubeMetadataFactory =
        PyObjectFactoryUtil.getFactory("youtube", "youtube_metadata");

      PyObject pyYoutubeMetadata = pyYoutubeMetadataFactory.create(
        new PyString(url));

      VideoMetadata youtubeMetadata = new VideoMetadata(pyYoutubeMetadata);

      return youtubeMetadata;
    }
    catch (PyException pye) {
      throw new YoutubeException(pye);
    }
  }

  private static final Pattern _YOUTUBE_URL_PATTERN =
    Pattern.compile("http.+youtube\\.com\\/watch\\?v=(\\S+)");

  private static Logger _log = LoggerFactory.getLogger(YoutubeUtil.class);

  // TODO Apply a real, distributed LRU cache later !!!
  private static final ConcurrentMap<String, VideoMetadata>
    youtubeMetadataCache = new ConcurrentHashMap<String, VideoMetadata>();

  private YoutubeUtil() {
  }

}