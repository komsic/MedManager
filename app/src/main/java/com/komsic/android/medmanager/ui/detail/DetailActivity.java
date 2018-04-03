package com.komsic.android.medmanager.ui.detail;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.komsic.android.medmanager.R;
import com.komsic.android.medmanager.data.DataManager;
import com.komsic.android.medmanager.data.model.Med;
import com.komsic.android.medmanager.data.model.Reminder;
import com.komsic.android.medmanager.ui.base.BaseActivity;
import com.komsic.android.medmanager.ui.base.DialogMvpView;
import com.komsic.android.medmanager.util.CalendarUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DetailActivity extends BaseActivity implements DetailMvpView {

    public static final int MED_NAME_TEXT = 1;
    public static final int MED_DESCRIPTION_TEXT = 2;
    public static final int START_DAY_TEXT = 3;
    public static final int END_DAY_TEXT = 4;


    private static final String TAG = DetailActivity.class.getSimpleName();

    private ArrayList<View> reminderViews;
    private View mReminderView;
    private TextView textAddReminder;
    private TextView medNameText, medDescriptionText, startDayText, endDayText;
    private DetailPresenter mPresenter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mPresenter = new DetailPresenter(DataManager.getInstance());

        textAddReminder = findViewById(R.id.text_add_reminder);
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
    public void addReminder(Reminder rem) {
        mReminderView = getLayoutInflater().inflate(R.layout.item_reminder, null);

        reload((LinearLayout) mReminderView, rem);
        LinearLayout l = findViewById(R.id.linear_layout_reminder);
        l.addView(mReminderView);
        reminderViews.add(mReminderView);
    }

    @Override
    public Context setContext(){
        return this;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.onPause();
    }

    public void onDayReminderClick(final View view) {

        //This variable will allow us to determine view's position and therefore the appropriate reminder for it
        final LinearLayout layout = (LinearLayout) view.getParent();
        final int viewPositionInList = reminderViews.indexOf(layout);
        Reminder viewReminder = mPresenter.getReminder(viewPositionInList);

//        final DialogChooseDay dialogChooseDay = new DialogChooseDay();
//        dialogChooseDay.setDayStateMap(viewReminder.dayStates);
//        dialogChooseDay.show(getSupportFragmentManager(), "dialogChooseDay");

//        getSupportFragmentManager().executePendingTransactions();
//        dialogChooseDay.getDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialog) {
//                mSelectedMed.updateReminderDayState(viewPositionInList, dialogChooseDay.getDayStateMap());
//                reload(layout, mSelectedMed.reminders.get(viewPositionInList));
//            }
//        });
    }

    public void onTimeReminderClick(View view) {
        //This variable will allow us to determine view's position and therefore the appropriate reminder for it
        final LinearLayout layout = (LinearLayout) view.getParent();
        final int viewPositionInList = reminderViews.indexOf(layout);
        Reminder viewReminder = mPresenter.getReminder(viewPositionInList);

        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                Log.d(TAG, "time: " + calendar.getTimeInMillis());
                mPresenter.updateReminderTimeOfDay(viewPositionInList, calendar.getTimeInMillis());
                reload(layout, mPresenter.getReminder(viewPositionInList));
            }
        };

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(viewReminder.timeOfDay);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                timeSetListener, c.get(Calendar.HOUR_OF_DAY),
                c.get(Calendar.MINUTE), false);
        timePickerDialog.show();
    }

    private void addReminder() {
        Reminder reminder = new Reminder();
        reminder.init();
        mPresenter.addReminder(reminder);
        addReminder(reminder);
    }

    private void reload(LinearLayout reminderView, Reminder reminder){

        TextView timeText = reminderView.findViewById(R.id.text_time_of_day);
        TextView dayStates = reminderView.findViewById(R.id.text_day);

        if (reminder != null) {
            timeText.setText(CalendarUtil.getTimeInString(reminder.timeOfDay));
            if (!reminder.dayStates.containsValue(false)) {
                dayStates.setText("Daily");
            } else if (!reminder.dayStates.containsValue(true)) {
                mPresenter.removeReminderDayState(reminder);
                reminderView.setVisibility(View.GONE);
                reminderViews.remove(reminderView);
            } else {
                StringBuilder sb = new StringBuilder();
                for (String s : reminder.dayStates.keySet()) {
                    if (reminder.dayStates.get(s)) {
                        sb.append(s + " ");
                    }
                }
                dayStates.setText(sb.toString());
            }
        }
    }
}
