package com.example.karlo.learningapplication.modules.login;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.example.karlo.learningapplication.commons.Constants;
import com.example.karlo.learningapplication.helpers.DatabaseHelper;
import com.example.karlo.learningapplication.models.LoginRequest;
import com.example.karlo.learningapplication.models.User;
import com.example.karlo.learningapplication.servertasks.RetrofitUtil;
import com.example.karlo.learningapplication.servertasks.interfaces.Api;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Karlo on 25.3.2018..
 */

public class LoginPresenter extends MvpBasePresenter<LoginView> implements MvpPresenter<LoginView> {

    public void login(final FirebaseAuth auth, final LoginRequest request) {
        if(request.getEmail().isEmpty() && request.getPassword().isEmpty()) {
            ifViewAttached(view -> view.showError(new Throwable("You need to enter both fields!")));
        } else {
            ifViewAttached(view -> view.loadingData(true));
            auth.signInWithEmailAndPassword(request.getEmail(), request.getPassword())
                    .addOnCompleteListener((Activity) getView(), task -> {
                        if (task.isSuccessful()) {
                            getDisplayNameAndSaveUser(auth.getCurrentUser());
                        } else {
                            // If sign in fails, display a message to the user.
                            ifViewAttached(view -> view.showError(new Throwable(task.getException().getMessage())));
                        }
                        ifViewAttached(view -> view.loadingData(false));
                    });
        }
    }

    public void signup(final FirebaseAuth auth, final LoginRequest request) {
        if (request.getEmail().isEmpty() && request.getPassword().isEmpty()) {
            ifViewAttached(view -> view.showError(new Throwable("You need to enter both fields!")));
        } else {
            ifViewAttached(view -> view.loadingData(true));
            auth.createUserWithEmailAndPassword(request.getEmail(), request.getPassword())
                    .addOnCompleteListener((Activity) getView(), task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            saveDisplayNameOnServer(firebaseUser, request.getDisplayName());
                            request.setDisplayName(null);
                            ifViewAttached(view -> view.onSignUp(request));
                        } else {
                            ifViewAttached(view -> view.showError(new Throwable(task.getException().getMessage())));
                        }
                        ifViewAttached(view -> view.loadingData(false));
                    });
        }
    }

    public void signUpWithGoogle(FirebaseAuth auth, Intent data) {
        ifViewAttached(view -> view.loadingData(true));
        @SuppressLint("RestrictedApi") Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        handleSignInResult(task, auth);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask, FirebaseAuth auth) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
            auth.signInWithCredential(credential)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            saveUserToDatabase(firebaseUser, null);
                            saveDisplayNameOnServer(firebaseUser, null);
                            ifViewAttached(LoginView::onLoggedIn);
                        } else {
                            // If sign in fails, display a message to the user.
                            ifViewAttached(view -> view.showError(new Throwable(task.getException().getMessage())));
                        }
                        ifViewAttached(view -> view.loadingData(false));
                    });
        } catch (ApiException e) {
            Log.w("tagic", "signInResult:failed code=" + e.getStatusCode());
            ifViewAttached(view -> view.showError(new Throwable(e.getMessage())));
        }
    }

    private void getDisplayNameAndSaveUser(FirebaseUser firebaseUser) {
        RetrofitUtil
                .getRetrofit(Constants.FIREBASE_BASE_URL)
                .create(Api.class)
                .getDisplayName(firebaseUser.getUid())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(name -> {
                    saveUserToDatabase(firebaseUser, name);
                    ifViewAttached(LoginView::onLoggedIn);
                });
    }

    private void saveUserToDatabase(FirebaseUser firebaseUser, String displayName) {
        User user = new User(firebaseUser.getUid(), firebaseUser.getEmail(), displayName != null ? displayName : firebaseUser.getDisplayName(), firebaseUser.getPhotoUrl());
        DatabaseHelper.saveRealmObject(user);
    }

    private void saveDisplayNameOnServer(FirebaseUser firebaseUser, String displayName) {
        DatabaseHelper.getUserReference()
                .child(firebaseUser.getUid())
                .setValue(new User(
                        firebaseUser.getUid(),
                        firebaseUser.getEmail(),
                        displayName != null ? displayName : firebaseUser.getDisplayName(),
                        firebaseUser.getPhotoUrl()));
    }
}