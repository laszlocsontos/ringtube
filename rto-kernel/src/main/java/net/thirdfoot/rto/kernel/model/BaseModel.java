package net.thirdfoot.rto.kernel.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import jodd.util.HashCode;
import jodd.util.StringPool;

/**
 * @author lcsontos
 */
@EntityListeners(BaseModelListener.class)
@MappedSuperclass
public abstract class BaseModel implements Serializable {

  public Date getDateCreated() {
    return _dateCreated;
  }

  public Date getDateDeleted() {
    return _dateDeleted;
  }

  public Date getDateModified() {
    return _dateModified;
  }

  public long getPrimaryKey() {
    return _primaryKey;
  }

  public long getRevision() {
    return _revision;
  }

  public String getUuid() {
    return _uuid;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null || obj.getClass() != getClass()) {
      return false;
    }

    BaseModel model = (BaseModel)obj;

    return (_primaryKey == model._primaryKey);
  }

  @Override
  public int hashCode() {
    return HashCode.hash(HashCode.SEED, _primaryKey);
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

  public void setPrimaryKey(long primaryKey) {
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
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Id
  private long _primaryKey;

  @Version
  @Column(name = "REVISION")
  private long _revision;

  @Column(name = "UUID", unique = true, updatable = false)
  private String _uuid;

}