package com.komsic.android.medmanager.ui.detail;

import android.content.Context;
import android.widget.LinearLayout;

import com.komsic.android.medmanager.data.model.Reminder;
import com.komsic.android.medmanager.ui.base.MvpView;

/**
 * Created by komsic on 4/3/2018.
 */

public interface DetailMvpView extends MvpView {
    void setText(String text, int whichTextView);
    void addReminder(Reminder reminder);
    Context setContext();
    void reload(LinearLayout reminderView, Reminder reminder);
}
