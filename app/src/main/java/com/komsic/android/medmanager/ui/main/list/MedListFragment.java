package com.komsic.android.medmanager.ui.main.list;

import android.app.SearchManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.komsic.android.medmanager.R;
import com.komsic.android.medmanager.data.DataManager;
import com.komsic.android.medmanager.data.model.Med;
import com.komsic.android.medmanager.ui.base.BaseFragment;
import com.komsic.android.medmanager.ui.main.MainActivity;
import com.komsic.android.medmanager.ui.main.add_med.AddMedDialog;

import java.util.List;

import static android.content.Context.SEARCH_SERVICE;

/**
 * Created by komsic on 4/2/2018.
 */

public class MedListFragment extends BaseFragment implements
        MedListMvpView, MedListAdapter.ItemInteractionListener {


    private MedListAdapter mAdapter;
    private MedListMvpPresenter<MedListMvpView> mPresenter;
    private ProgressBar mProgressBar;
    private RecyclerView recyclerView;
    private boolean isAdapterSetUp;

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
        recyclerView = rootView.findViewById(R.id.recycler_view);
        isAdapterSetUp = false;
        mProgressBar = rootView.findViewById(R.id.progress_bar);

        mPresenter = new MedListPresenter<>(DataManager.getInstance());
        mPresenter.onAttach(this);
        mPresenter.onViewPrepared();

        return rootView;
    }

    @Override
    public void onDestroyView() {
        mAdapter.removeListener();
        mPresenter.onDetach();
        super.onDestroyView();
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
                mAdapter.sortMedList();
                return true;
            case R.id.action_sign_out:
                mPresenter.signOut();
                mProgressBar.setVisibility(View.VISIBLE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSignOutDone() {
        mProgressBar.setVisibility(View.GONE);
        ((MainActivity) getBaseActivity()).openSplashActivity();
    }

    @Override
    public void updateList(List<Med> newMedList) {
        if (isAdapterSetUp) {
            mAdapter.notifyDataSetChanged();
        } else {
            isAdapterSetUp = true;
            mAdapter = new MedListAdapter(getContext(), this, newMedList);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(mAdapter);

            ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                    ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                @Override
                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                    mPresenter.removeMed(viewHolder.getAdapterPosition());
                }
            });
            helper.attachToRecyclerView(recyclerView);
        }
    }

    @Override
    public void updateMedAtIndexAt(int indexToBeChanged) {
        mAdapter.notifyItemChanged(indexToBeChanged);
    }

    @Override
    public void notifyMedRemoved(int position) {
        Toast.makeText(getBaseActivity(), "Med deleted", Toast.LENGTH_SHORT).show();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onEditClicked(int position) {
        AddMedDialog dialogAddMed = AddMedDialog.newInstance(position, true);
        dialogAddMed.show(getBaseActivity().getSupportFragmentManager(), "DialogAddMed");
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mPresenter == null) {
            mPresenter = new MedListPresenter<>(DataManager.getInstance());
        }
    }
}
