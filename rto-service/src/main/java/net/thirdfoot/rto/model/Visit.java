package net.thirdfoot.rto.model;

import javax.persistence.Entity;

import net.thirdfoot.rto.kernel.model.BaseModel;

/**
 * @author lcsontos
 */
@Entity
public class Visit extends BaseModel {

  public String getCity() {
    return _city;
  }

  public String getCountryCode() {
    return _countryCode;
  }

  public String getCountryName() {
    return _countryName;
  }

  public String getIpAddress() {
    return _ipAddress;
  }

  public Float getLatitude() {
    return _latitude;
  }

  public Float getLongitude() {
    return _longitude;
  }

  public String getRegionCode() {
    return _regionCode;
  }

  public String getRegionName() {
    return _regionName;
  }

  public String userAgent() {
    return _userAgent;
  }

  public Visitor getVisitor() {
    return _visitor;
  }

  public void setIpAddress(String _ipAddress) {
    this._ipAddress = _ipAddress;
  }

  public void setCity(String _city) {
    this._city = _city;
  }

  public void setCountryCode(String _countryCode) {
    this._countryCode = _countryCode;
  }

  public void setCountryName(String _countryName) {
    this._countryName = _countryName;
  }

  public void setLatitude(Float _latitude) {
    this._latitude = _latitude;
  }

  public void setLongitude(Float _longitude) {
    this._longitude = _longitude;
  }

  public void setRegionCode(String regionCode) {
    this._regionCode = regionCode;
  }

  public void setUserAgent(String userAgent) {
    _userAgent = userAgent;
  }

  public void setVisitor(Visitor _visitor) {
    this._visitor = _visitor;
  }

  // TODO Fix column names
  private String _city;
  private String _countryCode;
  private String _countryName;
  private String _ipAddress;
  private Float _latitude;
  private Float _longitude;
  private String _regionCode;
  private String _regionName;
  private String _userAgent;

  private Visitor _visitor;

}