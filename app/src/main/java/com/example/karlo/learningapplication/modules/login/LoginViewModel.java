package com.example.karlo.learningapplication.modules.login;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.karlo.learningapplication.R;
import com.example.karlo.learningapplication.commons.Constants;
import com.example.karlo.learningapplication.commons.Status;
import com.example.karlo.learningapplication.database.UserDataSource;
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

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class LoginViewModel extends AndroidViewModel {

    private static final String TAG = "LoginViewModel";
    private final MutableLiveData<Status> status = new MutableLiveData<>();
    private final MutableLiveData<User> mUser = new MutableLiveData<>();

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private UserDataSource mDataSource;

    public LoginViewModel(@NonNull Application application) {
        super(application);
    }

    public void setDataSource(UserDataSource userDataSource) {
        mDataSource = userDataSource;
    }

    public void checkIfLoggedIn() {
        compositeDisposable.add(mDataSource.getUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mUser::setValue));
    }

    public LiveData<Status> getStatus() {
        return status;
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
            status.setValue(Status.error(getApplication().getApplicationContext().getString(R.string.enter_all_fields)));
        } else {
            status.setValue(Status.loading(true));
            mAuth.signInWithEmailAndPassword(request.getEmail(), request.getPassword())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            getDisplayNameAndSaveUser(mAuth.getCurrentUser());
                        } else {
                            status.setValue(Status.error(task.getException().getMessage()));
                        }
                        status.setValue(Status.loading(false));
                    });
        }
    }

    public void signup(final LoginRequest request) {
        if (request.getEmail().isEmpty() && request.getPassword().isEmpty() && request.getDisplayName().isEmpty()) {
            status.setValue(Status.error(getApplication().getApplicationContext().getString(R.string.enter_all_fields)));
        } else {
            status.setValue(Status.loading(true));
            mAuth.createUserWithEmailAndPassword(request.getEmail(), request.getPassword())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            saveDisplayNameOnServer(firebaseUser, request.getDisplayName());
                            request.setDisplayName(null);
                            status.setValue(Status.onSignUp(request));
                        } else {
                            status.setValue(Status.error(task.getException().getMessage()));
                        }
                        status.setValue(Status.loading(false));
                    });
        }
    }

    public void signUpWithGoogle(Intent data) {
        status.setValue(Status.loading(true));
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
                            status.setValue(new Status(Status.Response.LOGIN, true));
                        } else {
                            status.setValue(Status.error(task.getException().getMessage()));
                        }
                        status.setValue(Status.loading(false));
                    });
        } catch (ApiException e) {
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            status.setValue(Status.error(e.getMessage()));
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
                }));
    }

    private void saveUserToDatabase(FirebaseUser firebaseUser, String displayName) {
        User user = new User(firebaseUser.getUid(), firebaseUser.getEmail(), displayName != null ? displayName : firebaseUser.getDisplayName(), firebaseUser.getPhotoUrl());
        compositeDisposable.add(insertUser(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> status.setValue(Status.login(user))));
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
