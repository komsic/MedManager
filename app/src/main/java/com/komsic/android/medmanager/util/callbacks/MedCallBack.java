package com.komsic.android.medmanager.util.callbacks;

import com.komsic.android.medmanager.data.model.Med;

/**
 * Created by komsic on 4/2/2018.
 */

public interface MedCallBack {
    void onNewMedAdded(Med med);

    void onUpadateMed(Med med);
}
