package net.thirdfoot.rto.kernel.util;

import java.io.File;
import java.io.IOException;

import jodd.io.FileUtil;

import net.thirdfoot.rto.kernel.config.PropsUtil;

/**
 * @author lcsontos
 */
public class FileSystemUtil {

  public static File createTempFile(
      String owner, String prefix, String suffix)
    throws IOException {

    File tempDir = getTempDir(owner);

    return FileUtil.createTempFile(prefix, suffix, tempDir);
  }

  public static String getBaseDir() {
    return PropsUtil.getString("fs.base.dir");
  }

  public static File getDataDir(String owner) {
    return _getDir("fs.data.dir", owner);
  }

  public static File getTempDir(String owner) {
    return _getDir("fs.temp.dir", owner);
  }

  private static File _getDir(String dirName, String owner) {
    String parentDir = PropsUtil.getString(dirName);

    File dir = new File(parentDir, owner);

    if (!dir.exists()) {
      dir.mkdirs();
    }

    return dir;
  }

  private FileSystemUtil() {
  }

}