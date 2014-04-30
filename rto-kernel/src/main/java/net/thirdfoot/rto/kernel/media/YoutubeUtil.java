package net.thirdfoot.rto.kernel.media;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jodd.util.StringUtil;

/**
 * @author lcsontos
 */
public class YoutubeUtil {

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