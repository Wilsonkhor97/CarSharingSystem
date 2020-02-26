package com.example.prototypenew;

import java.io.Serializable;

public class Customer implements Serializable {
    String url;
    String name;
    String icNo;
    String phoneNum;
    String userEmail;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public String getIcNo() {
        return icNo;
    }

    public String getName() {
        return name;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public void setIcNo(String icNo) {
        this.icNo = icNo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
