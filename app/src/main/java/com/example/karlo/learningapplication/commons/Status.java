package com.example.karlo.learningapplication.commons;

import com.example.karlo.learningapplication.models.LoginRequest;
import com.example.karlo.learningapplication.models.User;

public class Status {

    private Response mResponse;
    private String mMessage;
    private int mProgress;
    private boolean mState;
    private LoginRequest mLoginRequest;
    private User mUser;

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

    public Status(Response mResponse, User user) {
        this.mResponse = mResponse;
        this.mUser = user;
    }

    public Status(Response mResponse, int progress) {
        this.mResponse = mResponse;
        this.mProgress = progress;
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

    public static Status loading(boolean loading) {
        return new Status(Response.LOADING, loading);
    }

    public static Status error(String message) {
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

    public static Status login(User user) {
        return new Status(Response.LOGIN, user);
    }

    public int getProgress() {
        return mProgress;
    }

    public enum Response {
        LOADING, SUCCESS, ERROR, LOGIN, SIGNUP, MESSAGE, PROGRESS
    }
}

