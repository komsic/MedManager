package com.komsic.android.medmanager.ui.main.list;

import android.app.SearchManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.google.firebase.auth.FirebaseAuth;
import com.komsic.android.medmanager.R;
import com.komsic.android.medmanager.data.DataManager;
import com.komsic.android.medmanager.data.model.Med;
import com.komsic.android.medmanager.ui.base.BaseFragment;
import com.komsic.android.medmanager.ui.splash.SplashActivity;

import java.util.List;

import static android.content.Context.SEARCH_SERVICE;

/**
 * Created by komsic on 4/2/2018.
 */

public class MedListFragment extends BaseFragment implements
        MedListMvpView {

    private MedListAdapter mAdapter;
    private MedListMvpPresenter<MedListMvpView> mPresenter;

    public static MedListFragment newInstance() {
        Bundle args = new Bundle();
        MedListFragment fragment = new MedListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_med_list, container, false);
        RecyclerView recyclerView = rootView.findViewById(R.id.recycler_view);


        mAdapter = new MedListAdapter(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mAdapter);

        mPresenter = new MedListPresenter<>(DataManager.getInstance());
        mPresenter.onAttach(this);
        mPresenter.onViewPrepared();

        return rootView;
    }

    @Override
    public void updateList(List<Med> newMedList) {
        mAdapter.addMedList(newMedList);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.fragment_list_menu, menu);

        SearchManager searchManager = (SearchManager) getBaseActivity().getSystemService(SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        if (searchManager != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getBaseActivity().getComponentName()));
        }
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort:
//                mAdapter.sortMedList();
                FirebaseAuth.getInstance().signOut();
                getBaseActivity().startActivity(SplashActivity.getStartIntent(getBaseActivity()));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDestroyView() {
        mPresenter.onDetach();
        super.onDestroyView();
    }
}
