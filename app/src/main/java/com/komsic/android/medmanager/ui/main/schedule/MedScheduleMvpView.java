package com.komsic.android.medmanager.ui.main.schedule;

import com.komsic.android.medmanager.data.model.Alarm;
import com.komsic.android.medmanager.data.model.Med;
import com.komsic.android.medmanager.ui.base.MvpView;

/**
 * Created by komsic on 4/2/2018.
 */

public interface MedScheduleMvpView extends MvpView {
    void updateList(Med med);

    void addAlarm(Alarm alarm);
}
