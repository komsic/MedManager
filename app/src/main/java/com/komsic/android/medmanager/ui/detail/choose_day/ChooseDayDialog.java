package com.komsic.android.medmanager.ui.detail.choose_day;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.komsic.android.medmanager.R;
import com.komsic.android.medmanager.data.DataManager;
import com.komsic.android.medmanager.ui.base.BaseDialog;

import java.util.Map;

/**
 * Created by komsic on 4/3/2018.
 */

public class ChooseDayDialog extends BaseDialog implements ChooseDayDialogMvpView {

    private ChooseDayDialogMvpPresenter<ChooseDayDialog> mPresenter;

    private TextView monTextView, tueTextView, wedTextView, thuTextView, friTextView, satTextView,
            sunTextView;

    public ChooseDayDialog() {
        mPresenter = new ChooseDayDialogPresenter<>(DataManager.getInstance());
        mPresenter.onAttach(this);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getBaseActivity());
        View dialogView = getBaseActivity().getLayoutInflater()
                .inflate(R.layout.dialog_choose_day, null);

        monTextView = dialogView.findViewById(R.id.text_mon);
        tueTextView = dialogView.findViewById(R.id.text_tue);
        wedTextView = dialogView.findViewById(R.id.text_wed);
        thuTextView = dialogView.findViewById(R.id.text_thu);
        friTextView = dialogView.findViewById(R.id.text_fri);
        satTextView = dialogView.findViewById(R.id.text_sat);
        sunTextView = dialogView.findViewById(R.id.text_sun);

        FrameLayout cancelLayout = dialogView.findViewById(R.id.frame_cancel);
        cancelLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.onDismiss();
            }
        });

        FrameLayout doneLayout = dialogView.findViewById(R.id.frame_done);
        doneLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.onDismiss();
            }
        });

        mPresenter.init();

        builder.setView(dialogView);

        return builder.create();
    }


    private void init(final Map<String, Boolean> dayStateMap, TextView... dayTextViews) {
        for (int dayOfTheWeek = 0; dayOfTheWeek < dayTextViews.length; dayOfTheWeek++) {
            final TextView dayTextView = dayTextViews[dayOfTheWeek];
            final int dayOfTheWeek1 = dayOfTheWeek;
            if (dayStateMap.get(DataManager.daysOfTheWeek[dayOfTheWeek1])) {
                dayTextView.setBackground(ResourcesCompat.getDrawable(getResources(),
                        R.drawable.day_select, null));
            } else {
                dayTextView.setBackground(ResourcesCompat.getDrawable(getResources(),
                        R.drawable.day_unselect, null));
            }

            dayTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPresenter.onClick(v, dayOfTheWeek1);
                }
            });
        }
    }

    @Override
    public void onDayClicked(boolean status, View view, int dayOfTheWeek) {
        boolean newStatus;
        if (status) {
            view.setBackground(ResourcesCompat.getDrawable(view.getContext().getResources(),
                    R.drawable.day_unselect, null));
            newStatus = false;
        } else {
            view.setBackground(ResourcesCompat.getDrawable(view.getContext().getResources(),
                    R.drawable.day_select, null));
            newStatus = true;
        }
        mPresenter.updateCurrentReminderDayState(newStatus, dayOfTheWeek);
    }

    public void setReminderIndex(int reminderPosition) {
        if (reminderPosition >= 0) {
            mPresenter.setReminderIndex(reminderPosition);
        }
    }

    @Override
    public void dismissDialog(String tag) {
        dismiss();
    }

    @Override
    public void init(Map<String, Boolean> dayStateMap) {
        init(dayStateMap, sunTextView, monTextView, tueTextView, wedTextView, thuTextView,
                friTextView, satTextView);
    }

    @Override
    public void onDestroyView() {
        mPresenter.onDetach();
        super.onDestroyView();
    }
}
