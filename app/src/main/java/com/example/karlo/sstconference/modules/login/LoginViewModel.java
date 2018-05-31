package com.example.karlo.sstconference.modules.login;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Intent;
import android.util.Log;

import com.example.karlo.sstconference.R;
import com.example.karlo.sstconference.base.BaseViewModel;
import com.example.karlo.sstconference.commons.Status;
import com.example.karlo.sstconference.database.user.UserDataSource;
import com.example.karlo.sstconference.helpers.DatabaseHelper;
import com.example.karlo.sstconference.models.LoginRequest;
import com.example.karlo.sstconference.models.User;
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
import io.reactivex.MaybeObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LoginViewModel extends BaseViewModel {

    private static final String TAG = "LoginViewModel";
    private MutableLiveData<User> mUser;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private UserDataSource mDataSource;

    @Inject
    public LoginViewModel(UserDataSource userDataSource) {
        this.mDataSource = userDataSource;
    }

    private void checkIfLoggedIn() {
        mDataSource.getUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MaybeObserver<User>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(User user) {
                        mUser.setValue(user);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        mUser.setValue(null);
                    }
                });
    }

    public LiveData<User> getUser() {
        if (mUser == null) {
            mUser = new MutableLiveData<>();
            checkIfLoggedIn();
        }
        return mUser;
    }

    public Completable deleteUser(User user) {
        return Completable.fromAction(() -> mDataSource.deleteUser(user));
    }

    public void login(final LoginRequest request) {
        if(request.getEmail().isEmpty() && request.getPassword().isEmpty()) {
            mStatus.setValue(Status.error(R.string.enter_all_fields));
        } else {
            mStatus.setValue(Status.loading(true));
            mAuth.signInWithEmailAndPassword(request.getEmail(), request.getPassword())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            getUserDataFromServerAndSave(mAuth.getCurrentUser());
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
                            pushUserToServer(firebaseUser, request.getDisplayName());
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
                            getUserDataFromServerAndSave(firebaseUser);
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

    private void getUserDataFromServerAndSave(FirebaseUser firebaseUser) {
        mCompositeDisposable.add(mDataSource
                .getUserFromServer(firebaseUser.getUid())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::saveUserToDatabase,
                        throwable -> {
                            pushUserToServer(firebaseUser, null);
                            getUserDataFromServerAndSave(firebaseUser);
                        }));
    }

    private void saveUserToDatabase(User user) {
        mCompositeDisposable.add(mDataSource
                .insertOrUpdateUser(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> mUser.setValue(user)));
    }

    private void pushUserToServer(FirebaseUser firebaseUser, String displayName) {
        DatabaseHelper.getUserReference()
                .child(firebaseUser.getUid())
                .setValue(new User(
                        firebaseUser.getUid(),
                        firebaseUser.getEmail(),
                        displayName != null ? displayName : firebaseUser.getDisplayName(),
                        firebaseUser.getPhotoUrl()));
    }

    public void resetPassword(String email) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        mStatus.setValue(Status.error(R.string.password_reset_instructions));
                    } else {
                        mStatus.setValue(Status.error(R.string.email_not_found));
                    }
                });
    }
}
