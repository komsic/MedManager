package com.komsic.android.medmanager.ui.detail.choose_day;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.komsic.android.medmanager.R;
import com.komsic.android.medmanager.data.DataManager;
import com.komsic.android.medmanager.ui.base.BaseDialog;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by komsic on 4/3/2018.
 */

public class ChooseDayDialog extends BaseDialog implements ChooseDayDialogMvpView {

    private ChooseDayDialogPresenter<ChooseDayDialog> mPresenter;

    private TextView monTextView, tueTextView, wedTextView, thuTextView, friTextView, satTextView,
            sunTextView;

    public ChooseDayDialog() {
        mPresenter = new ChooseDayDialogPresenter<>(DataManager.getInstance());
        mPresenter.onAttach(this);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_choose_day, null);

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
        for (int count = 0; count < dayTextViews.length; count++) {
            final TextView dayTextView = dayTextViews[count];
            final int count1 = count;
            if (dayStateMap.get(DataManager.daysOfTheWeek[count1])) {
                dayTextView.setBackground(ResourcesCompat.getDrawable(getResources(),
                        R.drawable.day_select, null));
            } else {
                dayTextView.setBackground(ResourcesCompat.getDrawable(getResources(),
                        R.drawable.day_unselect, null));
            }

            dayTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPresenter.onClick(v, count1);
                }
            });
        }
    }

    public void setDayStateMap(Map<String, Boolean> dayStateMap) {
        if (dayStateMap != null) {
            mPresenter.setDayStateMap(dayStateMap);
        }
    }

    public Map<String, Boolean> getDayStateMap() {
        return mPresenter.getDataManager().getDayStateMap();
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
}
