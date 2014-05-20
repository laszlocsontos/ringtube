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
import jodd.io.FileUtilParams;
import jodd.io.StreamUtil;
import jodd.util.StringBand;
import jodd.util.StringPool;
import jodd.util.StringUtil;

import net.thirdfoot.rto.kernel.jython.PyObjectFactory;
import net.thirdfoot.rto.kernel.jython.PyObjectFactoryUtil;
import net.thirdfoot.rto.kernel.util.FileSystemUtil;

import org.python.core.PyException;
import org.python.core.PyObject;
import org.python.core.PyString;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lcsontos
 */
public class YoutubeUtil {

  public static String cutYoutubeVideo(
      String fileName, int startTimestamp, int endTimestamp)
    throws Exception {

    // TODO proper file creation

    File outFile = new File("/tmp/test.mp3");

    ConversionContext context = new ConversionContext();

    context.set(ConversionAttribute.START_TIMESTAMP, startTimestamp);
    context.set(ConversionAttribute.END_TIMESTAMP, endTimestamp);

    Converter converter = new AudioConverter(
      fileName, outFile.getPath(), context);

    converter.convert();

    return outFile.getPath();
  }

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

    // Move to video repository

    final int grp = 2;
    int len = videoId.length();

    StringBand sb = new StringBand(8 + len + len / grp);

    for (int index = 0; index < len; index++) {
      if (index % grp == 0) {
        sb.append(File.separatorChar);
      }

      sb.append(videoId.charAt(index));
    }

    sb.append(File.separator);
    sb.append(videoId);
    sb.append(StringPool.UNDERSCORE);
    sb.append(youtubeStream.getMediaType());
    sb.append(StringPool.UNDERSCORE);
    sb.append(youtubeStream.getQuality());
    sb.append(StringPool.DOT);
    sb.append(youtubeStream.getExtension());

    File videoDir = FileSystemUtil.getDataDir(YoutubeUtil.class.getName());
    File videoFile = new File(videoDir, sb.toString());

    FileUtilParams params = new FileUtilParams();

    try {
      FileUtil.moveFile(tempFile, videoFile, params.setCreateDirs(true));
    }
    catch (IOException ioe) {
      throw new YoutubeException(ioe);
    }

    return videoFile;
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

  private static Logger _log = LoggerFactory.getLogger(YoutubeUtil.class);

  private static final ConcurrentMap<String, YoutubeMetadata>
    youtubeMetadataCache = new ConcurrentHashMap<String, YoutubeMetadata>();

  private YoutubeUtil() {
  }

}