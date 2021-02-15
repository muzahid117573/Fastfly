package com.xplore24.courier.Model;

public class User {
   private String token;

    public User(String token) {
        this.token = token;
    }

    public User() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
