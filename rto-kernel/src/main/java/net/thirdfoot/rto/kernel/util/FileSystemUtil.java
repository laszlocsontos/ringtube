package net.thirdfoot.rto.kernel.util;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jodd.io.FileUtil;
import jodd.util.StringPool;
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

  public static File getBaseDir() {
    String baseDir = PropsBeanUtil.getString("fs.base.dir");

    File dir = new File(baseDir);

    if (!dir.exists()) {
      if (_log.isWarnEnabled()) {
        _log.warn("Specified base directory does not exist: " + baseDir);
      }

      baseDir = StringPool.EMPTY;

      try {
        dir = FileUtil.createTempDirectory();

        baseDir = dir.getAbsolutePath();
      }
      catch (IOException ioe) {
        _log.error(ioe.getMessage(), ioe);
      }

      if (_log.isWarnEnabled()) {
        _log.warn(
          "Using the following temporary directory instead: " + baseDir);
      }
    }

    return dir;
  }

  public static File getDataDir(String owner) {
    return _getDir("fs.data.dir", owner);
  }

  public static File getLogDir() {
    return _getDir("fs.log.dir", null);
  }

  public static File getTempDir(String owner) {
    return _getDir("fs.temp.dir", owner);
  }

  private static File _getDir(String dirName) {
    File dir = new File(PropsBeanUtil.getString(dirName));

    File parentDir = dir.getParentFile();

    if (!parentDir.exists()) {
      parentDir = getBaseDir();

      String name = dir.getName();

      dir = new File(parentDir, name);
    }

    if (!dir.exists()) {
      dir.mkdirs();
    }

    return dir;
  }

  private static File _getDir(String dirName, String owner) {
    File parentDir = _getDir(dirName);

    if (StringUtil.isBlank(owner)) {
      return parentDir;
    }

    File dir = new File(parentDir, owner);

    if (!dir.exists()) {
      dir.mkdirs();
    }

    return dir;
  }

  private static final Logger _log = LoggerFactory.getLogger(
    FileSystemUtil.class);

  private FileSystemUtil() {
  }

}