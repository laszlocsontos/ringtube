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

import jodd.io.StreamUtil;
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
public class YouTubeUtil {

  public static String cutYouTubeVideo(String inFile, ConversionContext context)
    throws Exception {

    File outFile = FileSystemUtil.createTempFile(
      YouTubeUtil.class.getName(), null, null);

    Converter converter = new AudioConverter(
      inFile, outFile.getPath(), context);

    converter.convert();

    return outFile.getPath();
  }

  public static VideoMetadata getYouTubeMetadata(String url) {
    if (StringUtil.isBlank(url)) {
      throw new NullPointerException("url is null or empty");
    }

    String youTubeId = parseUrl(url);

    if (StringUtil.isBlank(youTubeId)) {
      return null;
    }

    VideoMetadata youTubeMetadata = youTubeMetadataCache.get(youTubeId);

    if (youTubeMetadata != null) {
      return youTubeMetadata;
    }

    youTubeMetadata = _getYouTubeMetadata(url);

    youTubeMetadataCache.putIfAbsent(youTubeId, youTubeMetadata);

    return youTubeMetadata;
  }

  public static File getYouTubeVideo(VideoMetadata youTubeMetadata) {
    if (youTubeMetadata == null) {
      throw new IllegalArgumentException("youTubeMetadata is null");
    }

    VideoStream youTubeStream = youTubeMetadata.getFirstStream();

    URL youTubeUrl = null;

    try {
      youTubeUrl = new URL(youTubeStream.getUrl());
    }
    catch (MalformedURLException mue) {
      _log.error(mue.getMessage(), mue);
    }

    // Download video

    File tempFile = null;
    FileOutputStream fileOutputStream = null;

    String youTubeId = youTubeMetadata.getYouTubeId();

    try {
      InputStream inputStream = youTubeUrl.openStream();
      ReadableByteChannel in = Channels.newChannel(inputStream);

      tempFile = FileSystemUtil.createTempFile(
        YouTubeUtil.class.getName(), youTubeId, youTubeStream.getExtension());

      fileOutputStream = new FileOutputStream(tempFile);

      fileOutputStream.getChannel().transferFrom(
        in, 0, youTubeStream.getSize());
    }
    catch (IOException ioe) {
      throw new YouTubeException(ioe);
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

  private static VideoMetadata _getYouTubeMetadata(String url) {
    try {
      PyObjectFactory pyYouTubeMetadataFactory =
        PyObjectFactoryUtil.getFactory("youTube", "youTube_metadata");

      PyObject pyYouTubeMetadata = pyYouTubeMetadataFactory.create(
        new PyString(url));

      VideoMetadata youTubeMetadata = new VideoMetadata(pyYouTubeMetadata);

      return youTubeMetadata;
    }
    catch (PyException pye) {
      throw new YouTubeException(pye);
    }
  }

  private static final Pattern _YOUTUBE_URL_PATTERN =
    Pattern.compile("http.+youtube\\.com\\/watch\\?v=(\\S+)");

  private static Logger _log = LoggerFactory.getLogger(YouTubeUtil.class);

  // TODO Apply a real, distributed LRU cache later !!!
  private static final ConcurrentMap<String, VideoMetadata>
    youTubeMetadataCache = new ConcurrentHashMap<String, VideoMetadata>();

  private YouTubeUtil() {
  }

}