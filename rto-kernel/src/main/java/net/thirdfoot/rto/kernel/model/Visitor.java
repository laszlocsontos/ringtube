package net.thirdfoot.rto.kernel.model;

import javax.persistence.Entity;

/**
 * @author lcsontos
 */
@Entity
public class Visitor extends BaseModel {

  public String getCountryCode() {
    return _countryCode;
  }

  public String getCountryName() {
    return _countryName;
  }

  public int getNumOfVisits() {
    return _numOfVisits;
  }

  public void setCountryCode(String countryCode) {
    _countryCode = countryCode;
  }

  public void setCountryName(String countryName) {
    _countryName = countryName;
  }

  public void setNumOfVisits(int numOfVisits) {
    _numOfVisits = numOfVisits;
  }

  // TODO Fix column names
  private String _countryCode;
  private String _countryName;
  private int _numOfVisits;

}