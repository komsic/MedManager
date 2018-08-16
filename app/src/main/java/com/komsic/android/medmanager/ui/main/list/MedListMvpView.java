package com.komsic.android.medmanager.ui.main.list;

import com.komsic.android.medmanager.data.model.Med;
import com.komsic.android.medmanager.ui.base.MvpView;

import java.util.List;

/**
 * Created by komsic on 4/2/2018.
 */

public interface MedListMvpView extends MvpView {
    void updateList(List<Med> newMedList);

    void onSignOutDone();

    void updateMedAtIndexAt(int indexToBeChanged);

    void notifyMedRemoved(int position);
}
