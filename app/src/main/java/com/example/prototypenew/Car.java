package com.example.prototypenew;

import java.io.Serializable;

public class Car implements Serializable {
    String url;
    String email;
    String plateNum;
    String typeOfCar;
    String nameOfCar;
    String sDate;
    String eDate;
    String sTime;
    String eTime;
    String price;
    String latitude;
    String longitude;
    String phoneNum;
    public Car(){}

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getsDate() {
        return sDate;
    }

    public String getsTime() {
        return sTime;
    }

    public String getTypeOfCar() {
        return typeOfCar;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPrice() {
        return price;
    }

    public void seteDate(String eDate) {
        this.eDate = eDate;
    }

    public String geteDate() {
        return eDate;
    }

    public String geteTime() {
        return eTime;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public String getPlateNum() {
        return plateNum;
    }

    public String getNameOfCar() {
        return nameOfCar;
    }

    public void seteTime(String eTime) {
        this.eTime = eTime;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setPlateNum(String plateNum) {
        this.plateNum = plateNum;
    }

    public void setsDate(String sDate) {
        this.sDate = sDate;
    }

    public void setsTime(String sTime) {
        this.sTime = sTime;
    }

    public void setTypeOfCar(String typeOfCar) {
        this.typeOfCar = typeOfCar;
    }

    public void setNameOfCar(String nameOfCar) {
        this.nameOfCar = nameOfCar;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}

