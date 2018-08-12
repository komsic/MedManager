package com.komsic.android.medmanager.ui.main.add_med;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.komsic.android.medmanager.R;
import com.komsic.android.medmanager.data.DataManager;
import com.komsic.android.medmanager.ui.base.BaseDialog;
import com.komsic.android.medmanager.util.CalendarUtil;

import java.util.Calendar;
import java.util.Date;

import static com.komsic.android.medmanager.util.CalendarUtil.parseDateFromString;

/**
 * Created by komsic on 4/3/2018.
 */

public class AddMedDialog extends BaseDialog implements AddMedDialogMvpView{

    private EditText mEditName, mEditDescription, mEditStartDate, mEditEndDate;
    private Calendar mCalendar;

    AddMedDialogMvpPresenter<AddMedDialogMvpView> mPresenter;

    public static AddMedDialog newInstance() {
        AddMedDialog fragment = new AddMedDialog();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getBaseActivity());
        View dialogView = getBaseActivity().getLayoutInflater().inflate(R.layout.dialog_add_med, null);
        builder.setView(dialogView);

        mPresenter = new AddMedDialogPresenter<>(DataManager.getInstance());
        mPresenter.onAttach(this);

        mCalendar = Calendar.getInstance();

        mEditName = dialogView.findViewById(R.id.edit_name);
        mEditDescription = dialogView.findViewById(R.id.edit_description);

        mEditStartDate = dialogView.findViewById(R.id.edit_start_date);
        mEditEndDate = dialogView.findViewById(R.id.edit_end_date);
        processDateOperation(mEditStartDate, mEditEndDate);

        FrameLayout frameDone = dialogView.findViewById(R.id.frame_done);
        frameDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Add a new med
                mPresenter.onDoneClicked(mEditName.getText().toString(),
                        mEditDescription.getText().toString(),
                        parseDateFromString(mEditStartDate.getText().toString()).getTime(),
                        parseDateFromString(mEditEndDate.getText().toString()).getTime());

                mPresenter.onDismiss();
            }
        });

        FrameLayout frameCancel = dialogView.findViewById(R.id.frame_cancel);
        frameCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.onDismiss();
            }
        });
        return builder.create();
    }

    private void processDateOperation(EditText... dateEditTexts) {
        for (final EditText dateEditText : dateEditTexts) {
            final DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    mCalendar.set(year, month, dayOfMonth);
                    dateEditText.setText(CalendarUtil.getDateInString(mCalendar.getTimeInMillis()));
                }
            };

            dateEditText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String dateString = dateEditText.getText().toString();
                    Calendar calendar = Calendar.getInstance();
                    if (dateString.length() > 0) {
                        Date date = parseDateFromString(dateString);
                        calendar.setTime(date);
                    }

                    new DatePickerDialog(getBaseActivity(), onDateSetListener, calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            });
        }
    }

    @Override
    public void dismissDialog() {
        dismiss();
    }

    @Override
    public FragmentActivity getContext() {
        return getActivity();
    }

    @Override
    public void onDestroyView() {
        mPresenter.onDetach();
        super.onDestroyView();
    }
}
