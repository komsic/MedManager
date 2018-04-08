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
import com.komsic.android.medmanager.ui.base.BaseFragment;
import com.komsic.android.medmanager.util.CalendarUtil;

import java.util.Calendar;

import static com.komsic.android.medmanager.util.CalendarUtil.getDateInString;
import static com.komsic.android.medmanager.util.CalendarUtil.parseDateFromString;

/**
 * Created by komsic on 4/2/2018.
 */

public class MedScheduleFragment extends BaseFragment implements  MedScheduleMvpView{
    RecyclerView mRecyclerView;
    StubScrollListener onScrollListener;
    CalendarView mCalendarView;
    Animation animationDown;
    String selectedDate;
    MedScheduleAdapter adapter;
    MedSchedulePresenter mPresenter;
    public static final String ACTION_NOTIFY = "com.komsic.android.med_manager.ACTION_NOTIFY";

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
        onScrollListener = new StubScrollListener();
        mRecyclerView.addOnScrollListener(onScrollListener);

        adapter = new MedScheduleAdapter(getContext(),
                parseDateFromString(selectedDate).getTime());
        mRecyclerView.setAdapter(adapter);

        animationDown = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.expand);
        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                Calendar c = Calendar.getInstance();
                c.set(year, month, dayOfMonth);
                selectedDate = getDateInString(c.getTimeInMillis());
                getActivity().setTitle(selectedDate);
                adapter.setDate(parseDateFromString(selectedDate));
            }
        });
        mPresenter = new MedSchedulePresenter(DataManager.getInstance());
        mPresenter.onAttach(this);
        mPresenter.onViewPrepared();

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
    public void updateList(Med med) {
        adapter.addMed(med);
    }

    @Override
    public void addAlarm(Alarm alarm) {

    }

    class StubScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public final void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (!recyclerView.canScrollVertically(-1)) {
                onScrolledToTop();
            } else if (!recyclerView.canScrollVertically(1)) {
                onScrolledToBottom();
            }
            if (dy < 0) {
                onScrolledUp();
            } else if (dy > 0) {
                onScrolledDown();
            }
        }

        void onScrolledUp() {

        }

        void onScrolledDown() {
            if (mCalendarView.getVisibility() == View.GONE) {
                return;
            }

            if (mRecyclerView.getScrollState() == RecyclerView.SCROLL_STATE_SETTLING) {
                mCalendarView.setVisibility(View.GONE);
                //mCalendarView.startAnimation(animationDown);
            }
        }

        void onScrolledToTop() {
            if (mCalendarView.getVisibility() == View.VISIBLE) {
                return;
            }

            mCalendarView.setVisibility(View.VISIBLE);
        }

        void onScrolledToBottom() {}
    }
}