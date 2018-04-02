package com.komsic.android.medmanager.ui.main.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.komsic.android.medmanager.R;
import com.komsic.android.medmanager.data.DataManager;
import com.komsic.android.medmanager.data.model.Med;
import com.komsic.android.medmanager.ui.base.BaseFragment;

import java.util.List;

/**
 * Created by komsic on 4/2/2018.
 */

public class MedListFragment extends BaseFragment implements
        MedListMvpView {

    private RecyclerView mRecyclerView;
    private MedListAdapter mAdapter;
    private MedListPresenter<MedListMvpView> mPresenter;

    public static MedListFragment newInstance() {
        Bundle args = new Bundle();
        MedListFragment fragment = new MedListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_med_list, container, false);
        mRecyclerView = rootView.findViewById(R.id.recycler_view);


        mAdapter = new MedListAdapter(getContext());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);

        mPresenter = new MedListPresenter<>(DataManager.getInstance());
        mPresenter.onAttach(this);
        mPresenter.onViewPrepared();

        return rootView;
    }

    @Override
    public void updateList(Med med) {
        mAdapter.addMed(med);
    }

    @Override
    public void onDestroyView() {
        mPresenter.onDetach();
        super.onDestroyView();
    }
}
