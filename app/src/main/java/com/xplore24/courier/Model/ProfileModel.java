package com.xplore24.courier.Model;

public class ProfileModel {
    private String name,companyName,address,companyFbLink,email;

    public ProfileModel(String name, String companyName, String address, String companyFbLink, String email) {
        this.name = name;
        this.companyName = companyName;
        this.address = address;
        this.companyFbLink = companyFbLink;
        this.email = email;
    }

    public ProfileModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCompanyFbLink() {
        return companyFbLink;
    }

    public void setCompanyFbLink(String companyFbLink) {
        this.companyFbLink = companyFbLink;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
