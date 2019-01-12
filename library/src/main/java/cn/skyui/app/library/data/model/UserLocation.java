package cn.skyui.app.library.data.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class UserLocation implements Serializable {

    private String citycode;

    private String adcode;

    private String country;

    private String province;

    private String city;

    private String district;

    private String road;

    private String street;

    private String number;

    private String poiname;

    private BigDecimal lon;

    private BigDecimal lat;

    private Integer accuracy;

    private Boolean isOffset;

    private Boolean isFixLastLocation;

    public String getCitycode() {
        return citycode;
    }

    public void setCitycode(String citycode) {
        this.citycode = citycode;
    }

    public String getAdcode() {
        return adcode;
    }

    public void setAdcode(String adcode) {
        this.adcode = adcode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getRoad() {
        return road;
    }

    public void setRoad(String road) {
        this.road = road;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPoiname() {
        return poiname;
    }

    public void setPoiname(String poiname) {
        this.poiname = poiname;
    }

    public BigDecimal getLon() {
        return lon;
    }

    public void setLon(BigDecimal lon) {
        this.lon = lon;
    }

    public BigDecimal getLat() {
        return lat;
    }

    public void setLat(BigDecimal lat) {
        this.lat = lat;
    }

    public Integer getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(Integer accuracy) {
        this.accuracy = accuracy;
    }

    public Boolean getOffset() {
        return isOffset;
    }

    public void setOffset(Boolean offset) {
        isOffset = offset;
    }

    public Boolean getFixLastLocation() {
        return isFixLastLocation;
    }

    public void setFixLastLocation(Boolean fixLastLocation) {
        isFixLastLocation = fixLastLocation;
    }
}
