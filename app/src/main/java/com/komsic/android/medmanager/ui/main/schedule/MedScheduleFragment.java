package com.komsic.android.medmanager.ui.main.schedule;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import com.komsic.android.medmanager.R;
import com.komsic.android.medmanager.data.DataManager;
import com.komsic.android.medmanager.data.model.Alarm;
import com.komsic.android.medmanager.ui.base.BaseFragment;
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

    public static MedScheduleFragment newInstance() {
        Bundle args = new Bundle();
        MedScheduleFragment fragment = new MedScheduleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_med_schedule, container, false);

        CalendarView calendarView = rootView.findViewById(R.id.tv);
        RecyclerView recyclerView = rootView.findViewById(R.id.list);

        selectedDate = CalendarUtil.getDateInString(calendarView.getDate());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new MedScheduleAdapter(getContext());
        recyclerView.setAdapter(adapter);

        mPresenter = new MedSchedulePresenter<>(DataManager.getInstance());
        mPresenter.onAttach(this);
        mPresenter.onDateSelected(CalendarUtil.parseDateFromString(selectedDate).getTime());

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

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
    public void onDestroyView() {
        mPresenter.onDetach();
        Log.e(TAG, "onDestroyView: ");
        super.onDestroyView();
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void updateList(List<Alarm> medDataList) {
        adapter.addScheduleList(medDataList);
    }
}