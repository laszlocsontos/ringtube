package net.thirdfoot.rto.model;

import net.thirdfoot.rto.kernel.model.UUIDModel;

/**
 * @author lcsontos
 */
public class Visitor extends UUIDModel {

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

  private String _countryCode;
  private String _countryName;
  private int _numOfVisits;

}