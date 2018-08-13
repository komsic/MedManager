package com.komsic.android.medmanager.data.model.alarm;

public class AlarmItem {
    public long time;
    public String medName;

    public AlarmItem(long time, String medName) {
        this.time = time;
        this.medName = medName;
    }
}
