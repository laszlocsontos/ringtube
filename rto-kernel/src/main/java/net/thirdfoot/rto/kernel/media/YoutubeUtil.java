package net.thirdfoot.rto.kernel.media;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.thirdfoot.rto.kernel.jython.PyObjectFactory;
import net.thirdfoot.rto.kernel.jython.PyObjectFactoryUtil;

import org.python.core.PyException;
import org.python.core.PyObject;
import org.python.core.PyString;

import jodd.util.StringUtil;

/**
 * @author lcsontos
 */
public class YoutubeUtil {

  public static YoutubeMetadata getYoutubeMetadata(String url) {
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

  private static final Pattern _YOUTUBE_URL_PATTERN =
    Pattern.compile("http.+youtube\\.com\\/watch\\?v=(\\S+)");

  // TODO Apply a real, distributed LRU cache later !!!

  private static final ConcurrentMap<String, YoutubeMetadata>
    youtubeMetadataCache = new ConcurrentHashMap<String, YoutubeMetadata>();

  private YoutubeUtil() {
  }

}