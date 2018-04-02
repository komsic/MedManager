package com.komsic.android.medmanager.data;

import com.google.firebase.database.DataSnapshot;
import com.komsic.android.medmanager.data.model.Med;
import java.util.ArrayList;

/**
 * Created by komsic on 4/2/2018.
 * This clase is reponsible for storing data
 */

public class DataManager {

    private static DataManager sDataManager;

    public static DataManager getInstance() {
        if (sDataManager == null) {
            sDataManager = new DataManager();
        }
        return sDataManager;
    }

    private DataManager() {

    }
}
