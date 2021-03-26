package com.xplore24.courier.Model;

public class User {
   private String token;
    private String phone;

    public User(String token, String phone) {
        this.token = token;
        this.phone = phone;
    }

    public User() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
