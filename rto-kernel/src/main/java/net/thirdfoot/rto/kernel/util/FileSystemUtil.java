package net.thirdfoot.rto.kernel.util;

import java.io.File;
import java.io.IOException;

import jodd.io.FileUtil;
import jodd.util.StringUtil;
import net.thirdfoot.rto.kernel.config.PropsBeanUtil;

/**
 * @author lcsontos
 */
public class FileSystemUtil {

  public static File createTempFile(
      String owner, String prefix, String suffix)
    throws IOException {

    File tempDir = getTempDir(owner);

    // Fix NPE in File.createTempFile()
    if (StringUtil.isBlank(prefix)) {
      prefix = String.valueOf(System.currentTimeMillis());
    }

    File tempFile = FileUtil.createTempFile(prefix, suffix, tempDir);

    tempFile.deleteOnExit();

    return tempFile;
  }

  public static String getBaseDir() {
    return PropsBeanUtil.getString("fs.base.dir");
  }

  public static File getDataDir(String owner) {
    return _getDir("fs.data.dir", owner);
  }

  public static File getTempDir(String owner) {
    return _getDir("fs.temp.dir", owner);
  }

  private static File _getDir(String dirName, String owner) {
    String parentDir = PropsBeanUtil.getString(dirName);

    File dir = new File(parentDir, owner);

    if (!dir.exists()) {
      dir.mkdirs();
    }

    return dir;
  }

  private FileSystemUtil() {
  }

}