package com.komsic.android.medmanager.ui.detail.choose_day;

import android.widget.TextView;

import com.komsic.android.medmanager.ui.base.DialogMvpView;

import java.util.Map;

/**
 * Created by komsic on 4/3/2018.
 */

public interface ChooseDayDialogMvpView extends DialogMvpView {

    void init(Map<String, Boolean> dayStateMap);
}
