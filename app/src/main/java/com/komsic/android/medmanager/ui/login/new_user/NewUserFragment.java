package com.komsic.android.medmanager.ui.login.new_user;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
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

public class NewUserFragment extends BaseFragment implements NewUserMvpView, View.OnClickListener {
    private static final String TAG = "NewUserFragment";

    private NewUserMvpPresenter<NewUserMvpView> mPresenter;

    private TextInputEditText passwordEditText;
    private TextInputEditText emailEditText;
    private TextInputEditText fullNameEditText;
    private TextInputEditText usernameEditText;

    private TextInputLayout emailInputLayout;
    private TextInputLayout fullNameInputLayout;
    private TextInputLayout passwordInputLayout;
    private TextInputLayout usernameInputLayout;
    private ProgressBar mProgressBar;

    public NewUserFragment() {
        // Required empty public constructor
    }

    public static NewUserFragment newInstance() {
        NewUserFragment fragment = new NewUserFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_user, container, false);

        mPresenter = new NewUserPresenter<>(DataManager.getInstance());
        mPresenter.onAttach(this);

        passwordEditText = rootView.findViewById(R.id.password_text_edit);
        emailEditText = rootView.findViewById(R.id.email_text_edit);
        fullNameEditText = rootView.findViewById(R.id.full_name_text_edit);
        usernameEditText = rootView.findViewById(R.id.user_text_edit);

        emailInputLayout = rootView.findViewById(R.id.email_text_input);
        fullNameInputLayout = rootView.findViewById(R.id.full_name_text_input);
        usernameInputLayout = rootView.findViewById(R.id.user_text_input);
        passwordInputLayout = rootView.findViewById(R.id.password_text_input);

        mProgressBar = rootView.findViewById(R.id.progress_bar);

        TextView signUpText = rootView.findViewById(R.id.text_sign_up);
        signUpText.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        mProgressBar.setVisibility(View.GONE);
        mPresenter.onDetach();
        super.onDestroyView();
    }

    @Override
    public void openMainActivity() {
        Intent intent = MainActivity.getStartIntent(getBaseActivity());
        startActivity(intent);
        mProgressBar.setVisibility(View.GONE);
        getBaseActivity().finish();
    }

    @Override
    public void issueError() {
        mProgressBar.setVisibility(View.GONE);
        Toast.makeText(getBaseActivity(), "Error creating user. Please retry",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.text_sign_up) {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString();
            String fullName = fullNameEditText.getText().toString().trim();
            String userName = usernameEditText.getText().toString().trim();

            if (email.contains("@") && email.length() > 4 &&
                    password.length() > 8 &&
                    !fullName.isEmpty() &&
                    !userName.isEmpty()) {
                usernameInputLayout.setError(null);
                emailInputLayout.setError(null);
                passwordInputLayout.setError(null);
                fullNameInputLayout.setError(null);

                mPresenter.createUserWithEmailAndPassword(
                        email,
                        password,
                        fullName,
                        userName);
                mProgressBar.setVisibility(View.VISIBLE);
            } else {
                Log.e(TAG, "onClick: Error creating user");
                if (!email.contains("@") && email.length() < 4) {
                    emailInputLayout.setError("Email is not valid");
                } else {
                    emailInputLayout.setError(null);
                }
                if (password.length() < 8) {
                    passwordInputLayout.setError("Password must be more than 8 characters");
                } else {
                    passwordInputLayout.setError(null);
                }
                if (fullName.isEmpty()) {
                    fullNameInputLayout.setError("Full Name can not be empty");
                } else {
                    fullNameInputLayout.setError(null);
                }
                if (userName.isEmpty()) {
                    usernameInputLayout.setError("Username can not be empty");
                } else {
                    usernameInputLayout.setError(null);
                }
            }
        }
    }
}
