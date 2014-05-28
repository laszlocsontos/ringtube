package net.thirdfoot.rto.kernel.config;

/**
 * @author lcsontos
 */
public enum KernelKeys implements PropsKey {

  // FS

  FS_BASE_DIR("fs.base.dir"),
  FS_DATA_DIR("fs.data.dir"),
  FS_LOG_DIR("fs.log.dir"),
  FS_TEMP_DIR("fs.temp.dir");

  KernelKeys(String key) {
    _key = key;
  }

  @Override
  public String getKey() {
    return _key;
  }

  private String _key;

}