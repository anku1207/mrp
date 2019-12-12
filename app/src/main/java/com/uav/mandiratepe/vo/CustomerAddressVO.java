package com.uav.mandiratepe.vo;

import java.io.Serializable;

public class CustomerAddressVO implements Serializable {




    private Integer customerAddressId;
    private String mobileNumber;
    private String address1;
    private String address2;
    private String landMark;
    private StateVO stateRegion;
    private CityVO city;
    private String pincode;
    private String customerName;

    private String fullAddress;

    private Integer selectaddress;
    private Integer areaId;
    private String areaName;


    public Integer getCustomerAddressId() {
        return customerAddressId;
    }

    public void setCustomerAddressId(Integer customerAddressId) {
        this.customerAddressId = customerAddressId;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getLandMark() {
        return landMark;
    }

    public void setLandMark(String landMark) {
        this.landMark = landMark;
    }

    public StateVO getStateRegion() {
        return stateRegion;
    }

    public void setStateRegion(StateVO stateRegion) {
        this.stateRegion = stateRegion;
    }

    public CityVO getCity() {
        return city;
    }

    public void setCity(CityVO city) {
        this.city = city;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public Integer getSelectaddress() {
        return selectaddress;
    }

    public void setSelectaddress(Integer selectaddress) {
        this.selectaddress = selectaddress;
    }


    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }


}
