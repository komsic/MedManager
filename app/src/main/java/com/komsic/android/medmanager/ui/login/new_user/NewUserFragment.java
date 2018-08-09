package com.komsic.android.medmanager.ui.login.new_user;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.komsic.android.medmanager.R;

public class NewUserFragment extends Fragment {

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_user, container, false);
    }
}
