package com.komsic.android.medmanager.util;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.komsic.android.medmanager.R;
import com.komsic.android.medmanager.data.sync.SyncAlarmService;
import com.komsic.android.medmanager.ui.main.MainActivity;

import java.util.ArrayList;

import static com.komsic.android.medmanager.data.sync.SyncAlarmService.ACTION_NOTIFY;

/**
 * Created by komsic on 4/15/2018.
 */

public class AlarmReceiver extends BroadcastReceiver {

    private static final String TAG = "AlarmReceiver";

    private static final int NOTIFICATION_ID = 0;
    @Override
    public void onReceive(Context context, Intent intent) {
        if (ACTION_NOTIFY.equals(intent.getAction())) {
            Log.e(TAG, "onReceive: ");
            ArrayList<String> strings =
                    intent.getStringArrayListExtra(SyncAlarmService.ACTION_NOTIFY_EXTRA);

            StringBuilder sb = new StringBuilder();
            sb.append("| ");
            for (String s : strings) {
                sb.append(s);
                sb.append(" | ");
            }

            // an Intent broadcast.
            NotificationManager notificationManager = (NotificationManager)
                    context.getSystemService(Context.NOTIFICATION_SERVICE);
            //Create the content intent for the notification, which launches this activity
            Intent contentIntent = new Intent(context, MainActivity.class);
            PendingIntent contentPendingIntent = PendingIntent.getActivity
                    (context, NOTIFICATION_ID, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            //Build the notification
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                    //.setSmallIcon(R.drawable.ic_clock)
                    .setContentTitle("Take your fucking pill")
                    .setContentText(sb.toString())
                    .setContentIntent(contentPendingIntent)
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.ic_med_manager_logo)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setDefaults(NotificationCompat.DEFAULT_ALL);

            //Deliver the notification
            if (notificationManager != null) {
                notificationManager.notify(NOTIFICATION_ID, builder.build());
            }
        }
    }
}
