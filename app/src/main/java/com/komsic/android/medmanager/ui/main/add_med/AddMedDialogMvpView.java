package com.komsic.android.medmanager.ui.main.add_med;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;

import com.komsic.android.medmanager.ui.base.DialogMvpView;

import java.util.Calendar;

/**
 * Created by komsic on 4/3/2018.
 */

public interface AddMedDialogMvpView extends DialogMvpView {

    void dismissDialog();

    FragmentActivity getContext();
}
