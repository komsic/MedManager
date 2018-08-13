package com.komsic.android.medmanager.ui.detail;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import com.komsic.android.medmanager.R;
import com.komsic.android.medmanager.data.DataManager;
import com.komsic.android.medmanager.data.model.Reminder;
import com.komsic.android.medmanager.ui.base.BaseActivity;
import com.komsic.android.medmanager.ui.detail.choose_day.ChooseDayDialog;

import java.util.Calendar;
import java.util.List;

public class DetailActivity extends BaseActivity implements DetailMvpView,
        ReminderItemAdapter.ReminderItemAdapterEvent {

    public static final int MED_NAME_TEXT = 1;
    public static final int MED_DESCRIPTION_TEXT = 2;
    public static final int START_DAY_TEXT = 3;
    public static final int END_DAY_TEXT = 4;


    private static final String TAG = DetailActivity.class.getSimpleName();

    private TextView medNameText, medDescriptionText, startDayText, endDayText;
    private DetailMvpPresenter<DetailMvpView> mPresenter;
    private ReminderItemAdapter mReminderItemAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mPresenter = new DetailPresenter<>(DataManager.getInstance());

        TextView textAddReminder = findViewById(R.id.text_add_reminder);

        textAddReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.addReminder();
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

        mReminderItemAdapter = new ReminderItemAdapter(this);
        RecyclerView reminderRecyclerView = findViewById(R.id.recycler_view_reminder);
        reminderRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        reminderRecyclerView.setAdapter(mReminderItemAdapter);
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
    public void updateReminderList(Reminder reminder) {
        mReminderItemAdapter.updateList(reminder);
    }

    @Override
    public void updateReminderList(List<Reminder> reminders) {
        mReminderItemAdapter.updateList(reminders);
    }

    @Override
    public void openDialogChooseDay(int reminderPosition) {
        final ChooseDayDialog dialogChooseDay = new ChooseDayDialog();
        dialogChooseDay.setReminderIndex(reminderPosition);
        dialogChooseDay.show(getSupportFragmentManager(), "dialogChooseDay");

        getSupportFragmentManager().executePendingTransactions();

        dialogChooseDay.getDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mPresenter.onDialogDismissed();
            }
        });
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

    @Override
    public void removeReminderDayState(int reminderPosition) {
        mPresenter.removeReminderDayState(reminderPosition);
    }

    @Override
    public void onDayReminderClick(int reminderPosition) {
        mPresenter.onDayReminderClick(reminderPosition);
    }

    @Override
    public void onTimeReminderClick(final int reminderPosition, long time) {
        TimePickerDialog.OnTimeSetListener timeSetListener =
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);

                        mPresenter.updateCurrentReminderTime(reminderPosition, calendar.getTimeInMillis());
                    }
                };

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                timeSetListener,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false);
        timePickerDialog.show();
    }
}
