package com.example.karlo.sstconference.viewmodel;

import android.arch.lifecycle.Observer;
import android.content.Intent;

import com.example.karlo.sstconference.commons.Status;
import com.example.karlo.sstconference.database.user.UserDataSource;
import com.example.karlo.sstconference.models.LoginRequest;
import com.example.karlo.sstconference.models.User;
import com.example.karlo.sstconference.modules.login.LoginViewModel;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;

import static junit.framework.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LoginViewModelTest extends BaseViewModelTest {

    @Mock
    private FirebaseAuth mockAuth;

    @Mock
    private Task<AuthResult> mockAuthTask;

    @Mock
    private Task<Void> mockVoidTask;

    @Mock
    private AuthCredential mockCredentials;

    @Mock
    private UserDataSource userDataSource;

    @InjectMocks
    private LoginViewModel loginViewModel;

    private User user;
    private Observer observer;
    private FirebaseUser firebaseUser;

    @Before
    public void setup() {
        when(mockAuth.signInWithEmailAndPassword(MAIL, PASSWORD)).thenReturn(mockAuthTask);
        when(mockAuth.signInWithCredential(mockCredentials)).thenReturn(mockAuthTask);
        when(mockAuth.createUserWithEmailAndPassword(MAIL, PASSWORD)).thenReturn(mockAuthTask);
    }

    @Before
    public void setUpResponses() {
        user = getUser();
        observer = mock(Observer.class);
        when(userDataSource.getUser()).thenReturn(io.reactivex.Maybe.just(user));
        when(userDataSource.insertOrUpdateUser(any(User.class))).thenReturn(Completable.complete());
        when(userDataSource.getUserFromServer(any(String.class))).thenReturn(Observable.just(user));

        AuthResult authResult = Mockito.mock(AuthResult.class);
        firebaseUser = Mockito.mock(FirebaseUser.class);
        when(firebaseUser.getDisplayName()).thenReturn(DISPLAY_NAME);
        when(firebaseUser.getEmail()).thenReturn(MAIL);
        when(firebaseUser.getUid()).thenReturn(USER_ID);

        when(mockAuth.getCurrentUser()).thenReturn(firebaseUser);
        when(authResult.getUser()).thenReturn(firebaseUser);
    }

    @Test
    public void testSignInWithEmailAndPassword() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setDisplayName(DISPLAY_NAME);
        loginRequest.setEmail(MAIL);
        loginRequest.setPassword(PASSWORD);

        when(mockAuthTask.isSuccessful()).thenReturn(false);
        mockAuthTask.addOnCompleteListener(task -> assertEquals(task.isSuccessful(), true));

        Observer observer = mock(Observer.class);
        loginViewModel.getStatus().observeForever(observer);
        loginViewModel.login(loginRequest);

        sleep(500);

        assertEquals(loginViewModel.getStatus().getValue().getResponse(), Status.Response.LOADING);
        assertEquals(loginViewModel.getStatus().getValue().getState(), true);
        verify(mockAuth).signInWithEmailAndPassword(eq(MAIL), eq(PASSWORD));

    }

    @Test
    public void testCreateUserWithEmailAndPassword() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setDisplayName(DISPLAY_NAME);
        loginRequest.setEmail(MAIL);
        loginRequest.setPassword(PASSWORD);

        when(mockAuthTask.isSuccessful()).thenReturn(false);
        mockAuthTask.addOnCompleteListener(task -> assertEquals(task.isSuccessful(), true));

        Observer observer = mock(Observer.class);
        loginViewModel.getStatus().observeForever(observer);
        loginViewModel.signup(loginRequest);

        sleep(500);

        assertEquals(loginViewModel.getStatus().getValue().getResponse(), Status.Response.LOADING);
        assertEquals(loginViewModel.getStatus().getValue().getState(), true);
        verify(mockAuth).createUserWithEmailAndPassword(eq(MAIL), eq(PASSWORD));

    }

    @Test
    public void testGetUser() {
        loginViewModel.getUser().observeForever(observer);

        sleep(500);

        verify(observer).onChanged(user);
    }

    @Test
    public void testSignupWithGoogle() {
        loginViewModel.signUpWithGoogle(new Intent());
        sleep(500);
    }

    @Test
    public void testGetUserAndSave() {
        when(userDataSource.getUserFromServer(USER_ID))
                .thenReturn(Observable.just(user));

        loginViewModel.getUser().observeForever(observer);

        loginViewModel.getUserDataFromServerAndSave(firebaseUser);
        sleep(500);
        verify(userDataSource).insertOrUpdateUser(user);
    }

    @Test
    public void testNoUserAndPush() {
        when(userDataSource.getUserFromServer(USER_ID))
                .thenReturn(Observable.empty());
        when(userDataSource.getUser())
                .thenReturn(Maybe.empty());

        loginViewModel.getUser().observeForever(observer);

        sleep(500);

        verify(observer).onChanged(null);

        loginViewModel.getUserDataFromServerAndSave(firebaseUser);
        sleep(500);
        verify(userDataSource, never()).insertOrUpdateUser(any(User.class));
    }

    @Test
    public void testResetPassword() {
        when(mockAuth.sendPasswordResetEmail(MAIL))
                .thenReturn(mockVoidTask);

        loginViewModel.resetPassword(MAIL);
        sleep(500);
        verify(mockAuth).sendPasswordResetEmail(eq(MAIL));
    }
}