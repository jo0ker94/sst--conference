package com.example.karlo.learningapplication.modules.login;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Intent;
import android.util.Log;

import com.example.karlo.learningapplication.R;
import com.example.karlo.learningapplication.commons.BaseViewModel;
import com.example.karlo.learningapplication.commons.Constants;
import com.example.karlo.learningapplication.commons.Status;
import com.example.karlo.learningapplication.database.user.UserDataSource;
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

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LoginViewModel extends BaseViewModel {

    private static final String TAG = "LoginViewModel";
    private final MutableLiveData<User> mUser = new MutableLiveData<>();

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private UserDataSource mDataSource;

    @Inject
    public LoginViewModel(UserDataSource userDataSource) {
        this.mDataSource = userDataSource;
        checkIfLoggedIn();
    }

    public void checkIfLoggedIn() {
        mCompositeDisposable.add(mDataSource.getUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mUser::setValue));
    }

    public LiveData<User> getUser() {
        return mUser;
    }

    public Completable deleteUser(User user) {
        return Completable.fromAction(() -> mDataSource.deleteUser(user));
    }

    public Completable insertUser(User user) {
        return Completable.fromAction(() -> mDataSource.insertOrUpdateUser(user));
    }

    public void login(final LoginRequest request) {
        if(request.getEmail().isEmpty() && request.getPassword().isEmpty()) {
            mStatus.setValue(Status.error(R.string.enter_all_fields));
        } else {
            mStatus.setValue(Status.loading(true));
            mAuth.signInWithEmailAndPassword(request.getEmail(), request.getPassword())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            getDisplayNameAndSaveUser(mAuth.getCurrentUser());
                        } else {
                            mStatus.setValue(Status.error(task.getException().getMessage()));
                        }
                        mStatus.setValue(Status.loading(false));
                    });
        }
    }

    public void signup(final LoginRequest request) {
        if (request.getEmail().isEmpty() && request.getPassword().isEmpty() && request.getDisplayName().isEmpty()) {
            mStatus.setValue(Status.error(R.string.enter_all_fields));
        } else {
            mStatus.setValue(Status.loading(true));
            mAuth.createUserWithEmailAndPassword(request.getEmail(), request.getPassword())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            saveDisplayNameOnServer(firebaseUser, request.getDisplayName());
                            request.setDisplayName(null);
                            mStatus.setValue(Status.onSignUp(request));
                        } else {
                            mStatus.setValue(Status.error(task.getException().getMessage()));
                        }
                        mStatus.setValue(Status.loading(false));
                    });
        }
    }

    public void signUpWithGoogle(Intent data) {
        mStatus.setValue(Status.loading(true));
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
                            mStatus.setValue(new Status(Status.Response.LOGIN, true));
                        } else {
                            mStatus.setValue(Status.error(task.getException().getMessage()));
                        }
                        mStatus.setValue(Status.loading(false));
                    });
        } catch (ApiException e) {
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            mStatus.setValue(Status.error(e.getMessage()));
        }
    }

    private void getDisplayNameAndSaveUser(FirebaseUser firebaseUser) {
        mCompositeDisposable.add(RetrofitUtil
                .getRetrofit(Constants.FIREBASE_BASE_URL)
                .create(Api.class)
                .getDisplayName(firebaseUser.getUid())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(name -> {
                    saveUserToDatabase(firebaseUser, name);
                }));
    }

    private void saveUserToDatabase(FirebaseUser firebaseUser, String displayName) {
        User user = new User(firebaseUser.getUid(), firebaseUser.getEmail(), displayName != null ? displayName : firebaseUser.getDisplayName(), firebaseUser.getPhotoUrl());
        mCompositeDisposable.add(insertUser(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> mStatus.setValue(Status.login(user))));
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
