package com.komsic.android.medmanager.ui.login.sign_in;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.komsic.android.medmanager.R;
import com.komsic.android.medmanager.data.DataManager;
import com.komsic.android.medmanager.ui.base.BaseFragment;
import com.komsic.android.medmanager.ui.main.MainActivity;

public class SignInFragment extends BaseFragment implements SignInMvpView, View.OnClickListener {

    private SignInMvpPresenter<SignInFragment> mPresenter;
    private TextInputEditText passwordEditText;
    private TextInputEditText emailEditText;
    private TextInputLayout emailInputLayout;
    private TextInputLayout passwordInputLayout;
    private ProgressBar mProgressBar;

    public SignInFragment() {
        // Required empty public constructor
    }

    public static SignInFragment newInstance() {
        SignInFragment fragment = new SignInFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sign_in, container, false);

        mPresenter = new SignInPresenter<>(DataManager.getInstance());
        mPresenter.onAttach(this);

        mProgressBar = rootView.findViewById(R.id.progress_bar);

        passwordEditText = rootView.findViewById(R.id.password_text_edit);
        emailEditText = rootView.findViewById(R.id.email_text_edit);
        emailInputLayout = rootView.findViewById(R.id.email_text_input);
        passwordInputLayout = rootView.findViewById(R.id.password_text_input);

        TextView loginTextView = rootView.findViewById(R.id.text_login);
        loginTextView.setOnClickListener(this);
        TextView forgotPasswordTextView = rootView.findViewById(R.id.text_forgot_password);
        forgotPasswordTextView.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        mProgressBar.setVisibility(View.GONE);
        mPresenter.onDetach();
        super.onDestroyView();
    }

    @Override
    public void onClick(View view) {
        String email = emailEditText.getText().toString();
        switch (view.getId()) {
            case R.id.text_forgot_password:
                if (email.length() > 4 && email.contains("@")) {
                    mPresenter.onForgotPasswordClicked(email);
                } else {
                    Toast.makeText(getBaseActivity(), "Please enter a valid email",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.text_login:
                if (email.length() < 4 && !email.contains("@") &&
                        passwordEditText.getText().length() > 0) {

                    issueError(true);

                    return;
                }
                issueError(false);

                String password = passwordEditText.getText().toString();
                mProgressBar.setVisibility(View.VISIBLE);
                mPresenter.signIn(email, password);
                break;
        }
    }

    @Override
    public void openMainActivity() {
        Intent intent = MainActivity.getStartIntent(getBaseActivity());
        startActivity(intent);
        getBaseActivity().finish();
    }

    @Override
    public void issueError(boolean status) {
        if (status) {
            mProgressBar.setVisibility(View.GONE);
            emailInputLayout.setError("Email is not valid");
            passwordInputLayout.setError("Or password is incorrect");
        } else {
            emailInputLayout.setError(null);
            passwordInputLayout.setError(null);
        }
    }

    @Override
    public void issueForgottenEmailError() {
        Toast.makeText(getBaseActivity(), "Email sent", Toast.LENGTH_SHORT).show();
    }
}
