package com.example.karlo.sstconference.models;

/**
 * Created by Karlo on 25.3.2018..
 */

public class LoginRequest {

    private int mId;
    private String mEmail;
    private String mPassword;
    private String mDisplayName;

    public LoginRequest() {}

    public LoginRequest(String email, String password) {
        this(email, password, null);
    }

    public LoginRequest(String email, String password, String displayName) {
        this.mEmail = email;
        this.mPassword = password;
        this.mDisplayName = displayName;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        this.mEmail = email;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        this.mPassword = password;
    }

    public String getDisplayName() {
        return mDisplayName;
    }

    public void setDisplayName(String displayName) {
        this.mDisplayName = displayName;
    }

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }
}
