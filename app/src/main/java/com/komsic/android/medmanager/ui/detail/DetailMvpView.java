package com.komsic.android.medmanager.ui.detail;

import com.komsic.android.medmanager.data.model.Reminder;
import com.komsic.android.medmanager.ui.base.MvpView;

import java.util.List;

/**
 * Created by komsic on 4/3/2018.
 */

public interface DetailMvpView extends MvpView {
    void setText(String text, int whichTextView);

    void updateReminderList(Reminder reminder);

    void openDialogChooseDay(int reminderPosition);

    void updateReminderList(List<Reminder> reminders);
}
