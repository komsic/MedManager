package com.komsic.android.medmanager.ui.main.schedule;

import com.komsic.android.medmanager.data.model.Reminder;
import com.komsic.android.medmanager.ui.base.MvpView;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by komsic on 4/2/2018.
 */

public interface MedScheduleMvpView extends MvpView {

    void updateList(List<Map<Reminder, Set<String>>> med);
}
