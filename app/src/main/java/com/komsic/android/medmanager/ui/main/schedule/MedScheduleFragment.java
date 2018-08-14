package com.komsic.android.medmanager.ui.main.schedule;

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
import android.widget.CalendarView;
import android.widget.ProgressBar;

import com.komsic.android.medmanager.R;
import com.komsic.android.medmanager.data.DataManager;
import com.komsic.android.medmanager.data.model.Alarm;
import com.komsic.android.medmanager.ui.base.BaseFragment;
import com.komsic.android.medmanager.ui.main.MainActivity;
import com.komsic.android.medmanager.util.CalendarUtil;

import java.util.Calendar;
import java.util.List;

/**
 * Created by komsic on 4/2/2018.
 */

public class MedScheduleFragment extends BaseFragment implements  MedScheduleMvpView{
    private static final String TAG = "MedScheduleFragment";

    private String selectedDate;
    private MedScheduleAdapter adapter;
    private MedScheduleMvpPresenter<MedScheduleMvpView> mPresenter;
    private CalendarView mCalendarView;
    private ProgressBar mProgressBar;

    public static MedScheduleFragment newInstance() {
        Bundle args = new Bundle();
        MedScheduleFragment fragment = new MedScheduleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_med_schedule, container, false);

        mCalendarView = rootView.findViewById(R.id.tv);
        RecyclerView recyclerView = rootView.findViewById(R.id.list);
        mProgressBar = rootView.findViewById(R.id.progress_bar);

        selectedDate = CalendarUtil.getDateInString(mCalendarView.getDate());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new MedScheduleAdapter(getContext());
        recyclerView.setAdapter(adapter);

        mPresenter = new MedSchedulePresenter<>(DataManager.getInstance());
        mPresenter.onAttach(this);
        mPresenter.onDateSelected(CalendarUtil.parseDateFromString(selectedDate).getTime());

        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month,
                                            int dayOfMonth) {

                Calendar c = Calendar.getInstance();
                c.set(year, month, dayOfMonth);
                selectedDate = CalendarUtil.getDateInString(c.getTimeInMillis());

                getBaseActivity().setTitle(selectedDate);

                mPresenter.onDateSelected(CalendarUtil.parseDateFromString(selectedDate).getTime());
            }
        });


        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_schedule_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_sign_out) {
            mPresenter.signOut();
            mProgressBar.setVisibility(View.VISIBLE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        mPresenter.onDetach();
        super.onDestroyView();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (CalendarUtil.parseDateFromString(selectedDate).getTime() <= 0) {
            mCalendarView.setDate(CalendarUtil.getCurrentTime());
        }
    }

    @Override
    public void updateList(List<Alarm> medDataList) {
        adapter.addScheduleList(medDataList);
    }

    @Override
    public void onSignOutDone() {
        mProgressBar.setVisibility(View.GONE);
        ((MainActivity) getBaseActivity()).openSplashActivity();
    }
}