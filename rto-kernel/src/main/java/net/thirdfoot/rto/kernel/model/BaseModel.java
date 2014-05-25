package net.thirdfoot.rto.kernel.model;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import jodd.util.StringPool;
import jodd.util.StringUtil;

/**
 * @author lcsontos
 */
@MappedSuperclass
public abstract class BaseModel<K> implements Serializable {

  public Date getDateCreated() {
    return _dateCreated;
  }

  public Date getDateDeleted() {
    return _dateDeleted;
  }

  public Date getDateModified() {
    return _dateModified;
  }

  public K getPrimaryKey() {
    return _primaryKey;
  }

  public long getRevision() {
    return _revision;
  }

  public String getUuid() {
    if (StringUtil.isBlank(_uuid)) {
      UUID uuid = UUID.randomUUID();

      _uuid = uuid.toString();
    }

    return _uuid;
  }

  @SuppressWarnings("unchecked")
  @Override
  public boolean equals(Object obj) {
    if ((_primaryKey == null) || (obj == null) ||
        obj.getClass() != getClass()) {

      return false;
    }

    BaseModel<K> model = (BaseModel<K>)obj;

    if (model._primaryKey == null) {
      return false;
    }

    return _primaryKey.equals(model._primaryKey);
  }

  @Override
  public int hashCode() {
    return _primaryKey.hashCode();
  }

  public void setDateCreated(Date dateCreated) {
    _dateCreated = dateCreated;
  }

  public void setDateDeleted(Date dateDeleted) {
    _dateDeleted = dateDeleted;
  }

  public void setDateModified(Date dateModified) {
    _dateModified = dateModified;
  }

  public void setPrimaryKey(K primaryKey) {
    _primaryKey = primaryKey;
  }

  public void setRevision(long revision) {
    _revision = revision;
  }

  public void setUuid(String uuid) {
    _uuid = uuid;
  }

  @Override
  public String toString() {
    return getClass() + StringPool.AT + getPrimaryKey();
  }

  @Column(name = "DATE_CREATED")
  @Temporal(TemporalType.TIMESTAMP)
  private Date _dateCreated;

  @Column(name = "DATE_DELETED")
  @Temporal(TemporalType.TIMESTAMP)
  private Date _dateDeleted;

  @Column(name = "DATE_MODIFIED")
  @Temporal(TemporalType.TIMESTAMP)
  private Date _dateModified;

  @Column(name = "ID")
  @Id
  private K _primaryKey;

  @Version
  @Column(name = "REVISION")
  private long _revision;

  @Column(name = "UUID")
  @Id
  private String _uuid;

}