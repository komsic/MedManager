package com.komsic.android.medmanager.ui.detail;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.komsic.android.medmanager.R;
import com.komsic.android.medmanager.data.DataManager;
import com.komsic.android.medmanager.data.model.Reminder;
import com.komsic.android.medmanager.ui.base.BaseActivity;
import com.komsic.android.medmanager.util.CalendarUtil;
import com.komsic.android.medmanager.util.Util;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends BaseActivity implements DetailMvpView {

    public static final int MED_NAME_TEXT = 1;
    public static final int MED_DESCRIPTION_TEXT = 2;
    public static final int START_DAY_TEXT = 3;
    public static final int END_DAY_TEXT = 4;


    private static final String TAG = DetailActivity.class.getSimpleName();

    private ArrayList<View> reminderViews;
    private TextView medNameText, medDescriptionText, startDayText, endDayText;
    private DetailMvpPresenter<DetailMvpView> mPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mPresenter = new DetailPresenter<>(DataManager.getInstance());

        TextView textAddReminder = findViewById(R.id.text_add_reminder);
        reminderViews = new ArrayList<>();

        textAddReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addReminder();
            }
        });

        medNameText = findViewById(R.id.text_med_name);
        medDescriptionText = findViewById(R.id.text_description);
        startDayText = findViewById(R.id.text_start_date);
        endDayText = findViewById(R.id.text_end_date);

        setUp();
    }

    @Override
    protected void setUp() {
        mPresenter.onAttach(this);
        //noinspection ConstantConditions
        String s = getIntent().getExtras().getString("key");
        mPresenter.onViewPrepared(s);
    }

    @Override
    public void setText(String text, int whichTextView) {
        switch (whichTextView) {
            case MED_NAME_TEXT:
                medNameText.setText(text);
                break;
            case MED_DESCRIPTION_TEXT:
                medDescriptionText.setText(text);
                break;
            case START_DAY_TEXT:
                startDayText.setText(text);
                break;
            case END_DAY_TEXT:
                endDayText.setText(text);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDetach();
        super.onDestroy();
    }

    @Override
    public void addReminder(Reminder rem) {
        View reminderView = getLayoutInflater().inflate(R.layout.item_reminder, null);
        rem.setTimeOfDay(rem.getTimeOfDay());
        reload((LinearLayout) reminderView, rem);
        LinearLayout l = findViewById(R.id.linear_layout_reminder);
        l.addView(reminderView);
        reminderViews.add(reminderView);
    }

    @Override
    public Context setContext() {
        return this;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mPresenter.onPause();
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.onPause();
    }

    public void onDayReminderClick(final View view) {
        mPresenter.onDayReminderClick(view, reminderViews);
    }

    public void onTimeReminderClick(View view) {
        mPresenter.onTimeReminderClick(view, reminderViews);
    }

    private void addReminder() {
        mPresenter.addReminder();
    }

    @Override
    public void reload(LinearLayout reminderView, Reminder reminder) {

        TextView timeText = reminderView.findViewById(R.id.text_time_of_day);
        TextView dayStates = reminderView.findViewById(R.id.text_day);

        if (reminder != null) {
            timeText.setText(CalendarUtil.getTimeInString(reminder.getTimeOfDay()));
            if (!reminder.dayStates.containsValue(false)) {
                dayStates.setText("Daily");
            } else if (!reminder.dayStates.containsValue(true)) {
                mPresenter.removeReminderDayState(reminder);
                reminderView.setVisibility(View.GONE);
                reminderViews.remove(reminderView);
            } else {
                StringBuilder sb = new StringBuilder();
                List<String> sortedDays = Util.sortDaysOfWeek(reminder.dayStates.keySet());
                for (String s : sortedDays) {
                    if (reminder.dayStates.get(s)) {
                        sb.append(s).append(" ");
                    }
                }
                dayStates.setText(sb.toString());
            }
        }
    }
}
