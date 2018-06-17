package com.example.karlo.sstconference.viewmodel;

import android.arch.lifecycle.Observer;
import android.content.Intent;

import com.example.karlo.sstconference.commons.Status;
import com.example.karlo.sstconference.database.user.UserDataSource;
import com.example.karlo.sstconference.models.LoginRequest;
import com.example.karlo.sstconference.models.User;
import com.example.karlo.sstconference.modules.login.LoginViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import io.reactivex.Completable;
import io.reactivex.Observable;

import static junit.framework.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
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
    private AuthResult mockAuthResult;

    @Mock
    private DataSnapshot mockDatabaseDataSnapshot;

    @Mock
    private AuthCredential mockCredentials;

    @Mock
    private FirebaseUser mockUser;

    @Captor
    private ArgumentCaptor<OnCompleteListener> testOnCompleteListener;

    @Captor
    private ArgumentCaptor<OnSuccessListener> testOnSuccessListener;

    @Captor
    private ArgumentCaptor<OnFailureListener> testOnFailureListener;

    private Void mockRes = null;

    @Mock
    private UserDataSource userDataSource;

    @InjectMocks
    private LoginViewModel loginViewModel;

    private User user;
    private Observer observer;

    @Before
    public void setup() {
        when(mockAuthTask.addOnCompleteListener(testOnCompleteListener.capture())).thenReturn(mockAuthTask);
        when(mockAuthTask.addOnSuccessListener(testOnSuccessListener.capture())).thenReturn(mockAuthTask);
        when(mockAuthTask.addOnFailureListener(testOnFailureListener.capture())).thenReturn(mockAuthTask);

        when(mockAuth.signInAnonymously()).thenReturn(mockAuthTask);
        when(mockAuth.signInWithEmailAndPassword(MAIL, PASSWORD)).thenReturn(mockAuthTask);
        when(mockAuth.signInWithCredential(mockCredentials)).thenReturn(mockAuthTask);
        when(mockAuth.createUserWithEmailAndPassword(MAIL, PASSWORD)).thenReturn(mockAuthTask);
        when(mockAuth.sendPasswordResetEmail(MAIL)).thenReturn(mockVoidTask);
    }

    @Before
    public void setUpResponses() {
        user = getUser();
        observer = mock(Observer.class);
        when(userDataSource.getUser()).thenReturn(io.reactivex.Maybe.just(user));
        when(userDataSource.insertOrUpdateUser(any(User.class))).thenReturn(Completable.complete());
        when(userDataSource.getUserFromServer(any(String.class))).thenReturn(Observable.just(user));

        AuthResult authResult = Mockito.mock(AuthResult.class);
        FirebaseUser firebaseUser = Mockito.mock(FirebaseUser.class);
        when(firebaseUser.getDisplayName()).thenReturn(DISPLAY_NAME);
        when(firebaseUser.getEmail()).thenReturn(MAIL);
        when(firebaseUser.getUid()).thenReturn(USER_ID);
        when(firebaseUser.getDisplayName()).thenReturn(DISPLAY_NAME);

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

    /*@Mock
    private UserDataSource userDataSource;

    @Mock
    private FirebaseAuth auth;

    @InjectMocks
    private LoginViewModel loginViewModel;

    private User user;
    private Observer observer;

    @Before
    public void setUpResponses() {
        user = getUser();
        observer = mock(Observer.class);
        when(userDataSource.getUser()).thenReturn(io.reactivex.Maybe.just(user));
        when(userDataSource.insertOrUpdateUser(any(User.class))).thenReturn(Completable.complete());
        when(userDataSource.getUserFromServer(any(String.class))).thenReturn(Observable.just(user));

        AuthResult authResult = Mockito.mock(AuthResult.class);
        FirebaseUser firebaseUser = Mockito.mock(FirebaseUser.class);
        when(firebaseUser.getDisplayName()).thenReturn(DISPLAY_NAME);
        when(firebaseUser.getEmail()).thenReturn(MAIL);
        when(firebaseUser.getUid()).thenReturn(USER_ID);
        when(firebaseUser.getDisplayName()).thenReturn(DISPLAY_NAME);

        when(auth.getCurrentUser()).thenReturn(firebaseUser);
        when(authResult.getUser()).thenReturn(firebaseUser);

        when(auth.signInWithEmailAndPassword(MAIL, PASSWORD)).thenReturn(null);
    }

    @Test
    public void testGetUser() {
        loginViewModel.getUser().observeForever(observer);

        sleep(500);

        verify(observer).onChanged(user);
    }

    @Test
    public void testLoginUser() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setDisplayName(DISPLAY_NAME);
        loginRequest.setEmail(MAIL);
        loginRequest.setPassword(PASSWORD);
        loginViewModel.getStatus().observeForever(observer);
        loginViewModel.login(loginRequest);

        sleep(500);

        assertEquals(loginViewModel.getStatus().getValue().getResponse(), Status.Response.ERROR);
    }*/

}