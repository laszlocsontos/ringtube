package net.thirdfoot.rto.kernel.model;

import java.util.Date;
import java.util.UUID;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import jodd.util.StringUtil;

/**
 * @author lcsontos
 */
public class BaseModelListener {

  @PrePersist
  public void prePersist(BaseModel model) {
    if (StringUtil.isBlank(model.getUuid())) {
      UUID uuid = UUID.randomUUID();

      model.setUuid(uuid.toString());
    }

    if (model.getDateCreated() == null) {
      model.setDateCreated(new Date());
    }
  }

  @PreUpdate
  public void preUpdate(BaseModel model) {
    if (model.getDateModified() == null) {
      model.setDateModified(new Date());
    }
  }

}