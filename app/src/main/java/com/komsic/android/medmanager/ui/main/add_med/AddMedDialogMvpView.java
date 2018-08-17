package com.komsic.android.medmanager.ui.main.add_med;

import android.support.v4.app.FragmentActivity;

import com.komsic.android.medmanager.data.model.Med;
import com.komsic.android.medmanager.ui.base.DialogMvpView;

/**
 * Created by komsic on 4/3/2018.
 */

public interface AddMedDialogMvpView extends DialogMvpView {

    void dismissDialog();

    FragmentActivity getContext();

    void initView(Med medFromList, int position);
}