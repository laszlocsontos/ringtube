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
    String owner, String prefix, String suffix) throws IOException {

    File dataDir = getDataDir(owner);

    return FileUtil.createTempFile(prefix, suffix, dataDir);
  }

  public static String getBaseDir() {
    return PropsUtil.getString("fs.base.dir");
  }

  public static File getDataDir(String owner) {
    String dataParentDir = PropsUtil.getString("fs.data.dir");

    File dataDir = new File(dataParentDir + File.separator + owner);

    if (!dataDir.exists()) {
      dataDir.mkdirs();
    }

    return dataDir;
  }

  private FileSystemUtil() {
  }

}