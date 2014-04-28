package net.thirdfoot.rto.kernel.model;

import java.util.UUID;

import jodd.util.StringUtil;

/**
 * @author lcsontos
 */
public class UUIDModel extends BaseModel<String> {

  @Override
  public String getPrimaryKey() {
    String primaryKey = super.getPrimaryKey();

    if (StringUtil.isBlank(primaryKey)) {
      UUID uuid = UUID.randomUUID();

      setPrimaryKey(primaryKey = uuid.toString());
    }

    return primaryKey;
  }

}
