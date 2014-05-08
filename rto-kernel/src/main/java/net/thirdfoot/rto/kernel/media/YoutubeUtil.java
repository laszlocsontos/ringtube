package net.thirdfoot.rto.kernel.media;

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
import jodd.io.StreamUtil;
import jodd.util.StringUtil;

import net.thirdfoot.rto.kernel.jython.PyObjectFactory;
import net.thirdfoot.rto.kernel.jython.PyObjectFactoryUtil;

import org.python.core.PyException;
import org.python.core.PyObject;
import org.python.core.PyString;

/**
 * @author lcsontos
 */
public class YoutubeUtil {

  public static YoutubeMetadata getYoutubeMetadata(String url) {
    if (StringUtil.isBlank(url)) {
      throw new NullPointerException("url is null or empty");
    }

    String videoId = parseUrl(url);

    if (StringUtil.isBlank(url)) {
      return null;
    }

    YoutubeMetadata youtubeMetadata = youtubeMetadataCache.get(videoId);

    if (youtubeMetadata != null) {
      return youtubeMetadata;
    }

    youtubeMetadata = _getYoutubeMetadata(url);

    youtubeMetadataCache.putIfAbsent(videoId, youtubeMetadata);

    return youtubeMetadata;
  }

  public static File getYoutubeVideo(String url) {
    YoutubeMetadata youtubeMetadata = getYoutubeMetadata(url);

    if (youtubeMetadata == null) {
      throw new IllegalArgumentException("youtubeMetadata is null");
    }

    YoutubeStream youtubeStream = _selectYoutubeStream(youtubeMetadata);

    URL youtubeUrl = null;

    try {
      youtubeUrl = new URL(youtubeStream.getUrl());
    } catch (MalformedURLException e) {
      // TODO log here
    }

    File tempFile = null;
    FileOutputStream fileOutputStream = null;

    try {
      InputStream inputStream = youtubeUrl.openStream();
      ReadableByteChannel in = Channels.newChannel(inputStream);

      // TODO refine temp file creation here!
      tempFile = FileUtil.createTempFile();

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

  private static YoutubeMetadata _getYoutubeMetadata(String url) {
    try {
      PyObjectFactory pyYoutubeMetadataFactory =
        PyObjectFactoryUtil.getFactory("youtube", "youtube_metadata");

      PyObject pyYoutubeMetadata = pyYoutubeMetadataFactory.create(
        new PyString(url));

      YoutubeMetadata youtubeMetadata = new YoutubeMetadata(pyYoutubeMetadata);

      return youtubeMetadata;
    }
    catch (PyException pye) {
      throw new YoutubeException(pye);
    }
  }

  private static YoutubeStream _selectYoutubeStream(
    YoutubeMetadata youtubeMetadata) {

    // TODO Implement stream selection here
    return youtubeMetadata.getStreams().get(0);
  }

  private static final Pattern _YOUTUBE_URL_PATTERN =
    Pattern.compile("http.+youtube\\.com\\/watch\\?v=(\\S+)");

  // TODO Apply a real, distributed LRU cache later !!!

  private static final ConcurrentMap<String, YoutubeMetadata>
    youtubeMetadataCache = new ConcurrentHashMap<String, YoutubeMetadata>();

  private YoutubeUtil() {
  }

}