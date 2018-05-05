package com.example.karlo.learningapplication.modules.login;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.karlo.learningapplication.R;
import com.example.karlo.learningapplication.commons.Constants;
import com.example.karlo.learningapplication.database.user.LocalUserDataSource;
import com.example.karlo.learningapplication.database.user.UserDao;
import com.example.karlo.learningapplication.database.user.UserDatabase;
import com.example.karlo.learningapplication.models.LoginRequest;
import com.example.karlo.learningapplication.modules.home.HomeActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;

public class LoginActivity extends AppCompatActivity implements
        LoginView,
        LoginFragment.LoginInterface,
        RegisterFragment.RegisterInterface {

    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.pager)
    ViewPager mPager;

    private CompositeDisposable mDisposable = new CompositeDisposable();
    private LoginViewModel mViewModel;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ftue_fragment);

        ButterKnife.bind(this);

        mViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        UserDao userDao = UserDatabase.getDatabase(this).userModel();
        mViewModel.setDataSource(new LocalUserDataSource(userDao));
        mViewModel.checkIfLoggedIn();

        PagerAdapter mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        setUpGoogleClient();

        mViewModel.getUser().observe(this, user -> goToHome());

        mViewModel.getStatus().observe(this, status -> {
            switch(status.getResponse()) {
                case SIGNUP:
                    onSignUp(status.getLoginRequest());
                    break;
                case LOGIN:
                    onLoggedIn();
                    break;
                case LOADING:
                    loadingData(status.getState());
                    break;
                case ERROR:
                    showError(new Throwable(status.getMessage()));
                    break;
                case SUCCESS:
                    break;
            }
        });
    }

    private void setUpGoogleClient() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void goToHome() {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDisposable.clear();
    }

    @Override
    public void onLoggedIn() {
        Toast.makeText(this, R.string.success, Toast.LENGTH_SHORT).show();
        goToHome();
    }

    @Override
    public void onSignUp(LoginRequest loginRequest) {
        mViewModel.login(loginRequest);
    }

    @Override
    public void loadingData(boolean loading) {
        mProgressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showError(Throwable t) {
        Toast.makeText(this, t.getMessage(), Toast.LENGTH_SHORT).show();
    }

    private void signInGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, Constants.RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.RC_SIGN_IN) {
            mViewModel.signUpWithGoogle(data);
        }
    }

    @Override
    public void onLogin(String email, String password) {
        mViewModel.login(new LoginRequest(email, password));
    }

    @Override
    public void onRegister(LoginRequest loginRequest) {
        mViewModel.signup(loginRequest);
    }

    @Override
    public void signInWithGoogle() {
        signInGoogle();
    }

    @Override
    public void goToLogin() {
        mPager.setCurrentItem(0);
    }

    @Override
    public void goToRegister() {
        mPager.setCurrentItem(1);
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        private ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return position == 0 ? new LoginFragment() : new RegisterFragment();
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
