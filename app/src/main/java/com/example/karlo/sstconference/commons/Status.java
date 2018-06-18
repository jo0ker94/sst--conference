package com.example.karlo.sstconference.commons;

import com.example.karlo.sstconference.models.LoginRequest;

public class Status {

    private Response mResponse;
    private String mMessage;
    private int mInteger;
    private boolean mState;
    private LoginRequest mLoginRequest;

    public Status(Response mResponse, String mMessage) {
        this.mResponse = mResponse;
        this.mMessage = mMessage;
    }

    public Status(Response mResponse, boolean mState) {
        this.mResponse = mResponse;
        this.mState = mState;
    }

    public Status(Response mResponse, LoginRequest loginRequest) {
        this.mResponse = mResponse;
        this.mLoginRequest = loginRequest;
    }

    public Status(Response mResponse, int progress) {
        this.mResponse = mResponse;
        this.mInteger = progress;
    }

    public Response getResponse() {
        return mResponse;
    }

    public String getMessage() {
        return mMessage;
    }

    public boolean getState() {
        return mState;
    }

    public LoginRequest getLoginRequest() {
        return mLoginRequest;
    }

    public int getInteger() {
        return mInteger;
    }

    public static Status loading(boolean loading) {
        return new Status(Response.LOADING, loading);
    }

    public static Status error(String message) {
        return new Status(Response.ERROR, message);
    }

    public static Status error(int message) {
        return new Status(Response.ERROR, message);
    }

    public static Status onSignUp(LoginRequest loginRequest) {
        return new Status(Response.SIGNUP, loginRequest);
    }

    public static Status message(String message) {
        return new Status(Response.MESSAGE, message);
    }

    public static Status progress(int progress) {
        return new Status(Response.PROGRESS, progress);
    }

    public static Status logout() {
        return new Status(Response.LOGOUT, true);
    }

    public static Status delete(int id) {
        return new Status(Response.DELETED, id);
    }

    public static Status noData(boolean b) {
        return new Status(Response.NO_DATA, b);
    }

    public enum Response {
        LOADING, SUCCESS, ERROR, LOGIN, LOGOUT, SIGNUP, MESSAGE, PROGRESS, DELETED, NO_DATA
    }
}

