package com.example.karlo.sstconference;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.Observer;

import com.example.karlo.sstconference.commons.Status;
import com.example.karlo.sstconference.database.user.UserDataSource;
import com.example.karlo.sstconference.models.LoginRequest;
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

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

import static junit.framework.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LoginViewModelTest {


    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @BeforeClass
    public static void before(){
        RxAndroidPlugins.reset();
        RxJavaPlugins.reset();
        RxJavaPlugins.setIoSchedulerHandler(scheduler -> Schedulers.trampoline());
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(schedulerCallable -> Schedulers.trampoline());
    }

    @AfterClass
    public static void after(){
        RxAndroidPlugins.reset();
        RxJavaPlugins.reset();
    }

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

    protected static String MAIL = "mail@email.com";
    protected static String FACILITY = "Some Facility";
    protected static String IMAGE = "http://www.picture.com";
    protected static String NUMBER = "0123456789";
    protected static String PASSWORD = "test123";

    @Before
    public void setup() {
        when(mockAuthTask.addOnCompleteListener(testOnCompleteListener.capture())).thenReturn(mockAuthTask);
        //when(mockAuthTask.addOnSuccessListener(testOnSuccessListener.capture())).thenReturn(mockAuthTask);
        //when(mockAuthTask.addOnFailureListener(testOnFailureListener.capture())).thenReturn(mockAuthTask);

        when(mockAuth.signInAnonymously()).thenReturn(mockAuthTask);
        when(mockAuth.signInWithEmailAndPassword(MAIL, PASSWORD)).thenReturn(mockAuthTask);
        when(mockAuth.signInWithCredential(mockCredentials)).thenReturn(mockAuthTask);
        when(mockAuth.createUserWithEmailAndPassword(MAIL, PASSWORD)).thenReturn(mockAuthTask);
        when(mockAuth.sendPasswordResetEmail(MAIL)).thenReturn(mockVoidTask);

        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
    }

    @Test
    public void signInWithEmailAndPassword() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setDisplayName(FACILITY);
        loginRequest.setEmail(MAIL);
        loginRequest.setPassword(PASSWORD);

        when(mockAuthTask.isSuccessful()).thenReturn(false);

        Observer observer = mock(Observer.class);
        loginViewModel.getStatus().observeForever(observer);
        loginViewModel.login(loginRequest);

        assertEquals(loginViewModel.getStatus().getValue().getResponse(), Status.Response.LOADING);
        assertEquals(loginViewModel.getStatus().getValue().getState(), true);
        verify(mockAuth).signInWithEmailAndPassword(eq(MAIL), eq(PASSWORD));
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