package com.example.karlo.learningapplication.modules.login;

import android.content.Intent;
import android.util.Log;

import com.example.karlo.learningapplication.R;
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
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby3.mvp.MvpPresenter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Karlo on 25.3.2018..
 */

public class LoginPresenter extends MvpBasePresenter<LoginView> implements MvpPresenter<LoginView> {

    private static final String TAG = "LoginActivity";

    public FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public void login(final LoginRequest request) {
        if(request.getEmail().isEmpty() && request.getPassword().isEmpty()) {
            ifViewAttached(view -> view.showError(new Throwable(((LoginActivity) getView()).getString(R.string.enter_all_fields))));
        } else {
            ifViewAttached(view -> view.loadingData(true));
            mAuth.signInWithEmailAndPassword(request.getEmail(), request.getPassword())
                    .addOnCompleteListener(((LoginActivity) getView()), task -> {
                        if (task.isSuccessful()) {
                            getDisplayNameAndSaveUser(mAuth.getCurrentUser());
                        } else {
                            ifViewAttached(view -> view.showError(new Throwable(task.getException().getMessage())));
                        }
                        ifViewAttached(view -> view.loadingData(false));
                    });
        }
    }

    public void signup(final LoginRequest request) {
        if (request.getEmail().isEmpty() && request.getPassword().isEmpty() && request.getDisplayName().isEmpty()) {
            ifViewAttached(view -> view.showError(new Throwable(((LoginActivity) getView()).getString(R.string.enter_all_fields))));
        } else {
            ifViewAttached(view -> view.loadingData(true));
            mAuth.createUserWithEmailAndPassword(request.getEmail(), request.getPassword())
                    .addOnCompleteListener(((LoginActivity) getView()), task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
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

    public void signUpWithGoogle(Intent data) {
        ifViewAttached(view -> view.loadingData(true));
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        handleSignInResult(task);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            saveUserToDatabase(firebaseUser, null);
                            saveDisplayNameOnServer(firebaseUser, null);
                            ifViewAttached(LoginView::onLoggedIn);
                        } else {
                            ifViewAttached(view -> view.showError(new Throwable(task.getException().getMessage())));
                        }
                        ifViewAttached(view -> view.loadingData(false));
                    });
        } catch (ApiException e) {
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            ifViewAttached(view -> view.showError(new Throwable(e.getMessage())));
        }
    }

    private void getDisplayNameAndSaveUser(FirebaseUser firebaseUser) {
        compositeDisposable.add(RetrofitUtil
                .getRetrofit(Constants.FIREBASE_BASE_URL)
                .create(Api.class)
                .getDisplayName(firebaseUser.getUid())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(name -> {
                    saveUserToDatabase(firebaseUser, name);
                    ifViewAttached(LoginView::onLoggedIn);
                }));
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

    @Override
    public void destroy() {
        super.destroy();
        compositeDisposable.clear();
    }
}