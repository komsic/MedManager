package com.komsic.android.medmanager.ui.main.schedule;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CalendarView;

import com.komsic.android.medmanager.R;
import com.komsic.android.medmanager.data.DataManager;
import com.komsic.android.medmanager.data.model.Alarm;
import com.komsic.android.medmanager.data.model.Med;
import com.komsic.android.medmanager.data.model.Reminder;
import com.komsic.android.medmanager.ui.base.BaseFragment;
import com.komsic.android.medmanager.util.CalendarUtil;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by komsic on 4/2/2018.
 */

public class MedScheduleFragment extends BaseFragment implements  MedScheduleMvpView{
    RecyclerView mRecyclerView;
    CalendarView mCalendarView;
    Animation animationDown;
    String selectedDate;
    MedScheduleAdapter adapter;
    MedSchedulePresenter mPresenter;

    public static MedScheduleFragment newInstance() {
        Bundle args = new Bundle();
        MedScheduleFragment fragment = new MedScheduleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_med_schedule, container, false);

        mCalendarView = rootView.findViewById(R.id.tv);
        mRecyclerView = rootView.findViewById(R.id.list);

        selectedDate = CalendarUtil.getDateInString(mCalendarView.getDate());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new MedScheduleAdapter(getContext());
        mRecyclerView.setAdapter(adapter);

        mPresenter = new MedSchedulePresenter(DataManager.getInstance());
        mPresenter.onAttach(this);
        mPresenter.onViewPrepared();
        mPresenter.onDateSelected(CalendarUtil.parseDateFromString(selectedDate).getTime());

        animationDown = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.expand);
        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {

                Calendar c = Calendar.getInstance();
                c.set(year, month, dayOfMonth);
                selectedDate = CalendarUtil.getDateInString(c.getTimeInMillis());

                getActivity().setTitle(selectedDate);

                mPresenter.onDateSelected(CalendarUtil.parseDateFromString(selectedDate).getTime());
            }
        });


        return rootView;
    }

    @Override
    public void onDestroyView() {
        mPresenter.onDetach();
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void updateList(List<Map<Reminder, Set<String>>> medDataList) {
        //adapter.addMed(med);
        adapter.addScheduleList(medDataList);
    }

    @Override
    public void addAlarm(Alarm alarm) {

    }
}