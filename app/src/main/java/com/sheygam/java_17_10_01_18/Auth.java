package com.sheygam.java_17_10_01_18;

/**
 * Created by gregorysheygam on 10/01/2018.
 */

public class Auth {
    private String email, password;

    public Auth() {
    }

    public Auth(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
