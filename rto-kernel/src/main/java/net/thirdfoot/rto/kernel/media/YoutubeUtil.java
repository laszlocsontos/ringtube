package net.thirdfoot.rto.kernel.media;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.thirdfoot.rto.kernel.jython.PyObjectFactory;
import net.thirdfoot.rto.kernel.jython.PyObjectFactoryUtil;

import org.python.core.PyObject;
import org.python.core.PyString;

import jodd.util.StringUtil;

/**
 * @author lcsontos
 */
public class YoutubeUtil {

  public static YoutubeMetadata getYoutubeMetadata(String url) {
    PyObjectFactory pyYoutubeMetadataFactory =
      PyObjectFactoryUtil.getFactory("youtube", "youtube_metadata");

    PyObject pyYoutubeMetadata = pyYoutubeMetadataFactory.create(
      new PyString(url));

    YoutubeMetadata youtubeMetadata = new YoutubeMetadata(pyYoutubeMetadata);

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

  private static final Pattern _YOUTUBE_URL_PATTERN =
    Pattern.compile("http.+youtube\\.com\\/watch\\?v=(\\S+)");

  private YoutubeUtil() {
  }

}