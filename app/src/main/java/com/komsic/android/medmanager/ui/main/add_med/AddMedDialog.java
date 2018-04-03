package com.komsic.android.medmanager.ui.main.add_med;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.komsic.android.medmanager.R;
import com.komsic.android.medmanager.data.DataManager;
import com.komsic.android.medmanager.data.model.Med;
import com.komsic.android.medmanager.ui.base.BaseActivity;
import com.komsic.android.medmanager.ui.base.BaseDialog;
import com.komsic.android.medmanager.ui.base.DialogMvpView;
import com.komsic.android.medmanager.util.CalendarUtil;

import java.util.Calendar;
import java.util.Date;

import static com.komsic.android.medmanager.util.CalendarUtil.parseDateFromString;

/**
 * Created by komsic on 4/3/2018.
 */

public class AddMedDialog extends BaseDialog implements AddMedDialogMvpView{

    private EditText mEditName, mEditDescription, mEditStartDate, mEditEndDate;
    private FrameLayout mFrameCancel, mFrameDone;

    private Calendar mCalendar;
    AddMedDialogPresenter mPresenter;

    public static AddMedDialog newInstance() {
        AddMedDialog fragment = new AddMedDialog();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_add_med, null);
        builder.setView(dialogView);

        mPresenter = new AddMedDialogPresenter(DataManager.getInstance());
        mPresenter.onAttach(this);

        mCalendar = Calendar.getInstance();

        mEditName = dialogView.findViewById(R.id.edit_name);
        mEditDescription = dialogView.findViewById(R.id.edit_description);

        mEditStartDate = dialogView.findViewById(R.id.edit_start_date);
        mEditEndDate = dialogView.findViewById(R.id.edit_end_date);
        processDateOperation(mEditStartDate, mEditEndDate);

        mFrameDone = dialogView.findViewById(R.id.frame_done);
        mFrameDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.onDoneClicked(mEditName.getText().toString(),
                        mEditDescription.getText().toString(),
                        parseDateFromString(mEditStartDate.getText().toString()).getTime(),
                        parseDateFromString(mEditEndDate.getText().toString()).getTime());

                mPresenter.onDismiss();
            }
        });

        mFrameCancel = dialogView.findViewById(R.id.frame_cancel);
        mFrameCancel.setOnClickListener(new View.OnClickListener() {
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

                    new DatePickerDialog(getActivity(), onDateSetListener, calendar.get(Calendar.YEAR),
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
}
