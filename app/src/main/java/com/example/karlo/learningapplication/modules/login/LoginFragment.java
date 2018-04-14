package com.example.karlo.learningapplication.modules.login;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.karlo.learningapplication.R;
import com.google.android.gms.common.SignInButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Karlo on 11.4.2018..
 */

public class LoginFragment extends android.support.v4.app.Fragment {

    @BindView(R.id.login_email)
    EditText mEmail;
    @BindView(R.id.login_password)
    EditText mPassword;
    @BindView(R.id.login_button)
    Button mLoginButton;
    @BindView(R.id.google_sign_in)
    SignInButton mSigninGoogleButton;
    @BindView(R.id.create_account)
    TextView mCreateAccount;

    interface LoginInterface {
        void onLogin(String email, String password);
        void signInWithGoogle();
        void goToRegister();
    }

    private LoginInterface mListener;
    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_login, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpListeners();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getActivity() instanceof LoginActivity) {
            mListener = (LoginInterface) getActivity();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    private void setUpListeners() {
        mLoginButton.setOnClickListener(view -> mListener.onLogin(mEmail.getText().toString(), mPassword.getText().toString()));
        mSigninGoogleButton.setOnClickListener(view -> mListener.signInWithGoogle());
        mCreateAccount.setOnClickListener(view -> mListener.goToRegister());
        TextView textView = (TextView) mSigninGoogleButton.getChildAt(0);
        textView.setText(R.string.sign_in_with_google);
    }
}
