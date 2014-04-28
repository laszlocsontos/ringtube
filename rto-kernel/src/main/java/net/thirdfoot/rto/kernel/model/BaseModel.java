package net.thirdfoot.rto.kernel.model;

import java.io.Serializable;
import java.util.Date;

import jodd.util.StringPool;

/**
 * @author lcsontos
 */
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

  @Override
  public String toString() {
    return getClass() + StringPool.AT + getPrimaryKey();
  }

  private Date _dateCreated;
  private Date _dateDeleted;
  private Date _dateModified;

  private K _primaryKey;

  private long _revision;

}