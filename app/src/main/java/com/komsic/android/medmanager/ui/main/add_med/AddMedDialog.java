package com.komsic.android.medmanager.ui.main.add_med;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.komsic.android.medmanager.R;
import com.komsic.android.medmanager.data.DataManager;
import com.komsic.android.medmanager.data.model.Med;
import com.komsic.android.medmanager.ui.base.BaseDialog;
import com.komsic.android.medmanager.util.CalendarUtil;

import java.util.Calendar;
import java.util.Date;

import static com.komsic.android.medmanager.util.CalendarUtil.parseDateFromString;

/**
 * Created by komsic on 4/3/2018.
 */

public class AddMedDialog extends BaseDialog implements AddMedDialogMvpView{
    private static final String TAG = "AddMedDialog";

    private static final String POSITION = "index";
    private static final String EDIT_STATUS = "isEdit";

    private EditText mEditName, mEditDescription, mEditStartDate, mEditEndDate;
    private Calendar mCalendar;
    private int mPosition;
    private boolean mIsEdit;

    private AddMedDialogMvpPresenter<AddMedDialogMvpView> mPresenter;

    public static AddMedDialog newInstance(int position, boolean isEdit) {
        AddMedDialog fragment = new AddMedDialog();
        Bundle bundle = new Bundle();
        bundle.putInt(POSITION, position);
        bundle.putBoolean(EDIT_STATUS, isEdit);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPosition = getArguments().getInt(POSITION, -1);
            mIsEdit = getArguments().getBoolean(EDIT_STATUS, false);
        }
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
                mPresenter.onDoneClicked(mIsEdit, mPosition,
                        mEditName.getText().toString(),
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

        if (mIsEdit) {
            mPresenter.initView(mPosition);
        }

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
    public void initView(Med medFromList, int position) {
        mEditName.setText(medFromList.name);
        mEditDescription.setText(medFromList.description);
        mEditStartDate.setText(CalendarUtil.getDateInString(medFromList.startDate));
        mEditEndDate.setText(CalendarUtil.getDateInString(medFromList.endDate));
    }

    @Override
    public void onDestroyView() {
        mPresenter.onDetach();
        super.onDestroyView();
    }
}
