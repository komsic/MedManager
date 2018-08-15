package com.komsic.android.medmanager.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by komsic on 4/2/2018.
 */

public class CalendarUtil {

    public static String getDateInString(long longDate) {
        return DateFormat.getDateInstance(DateFormat.MEDIUM).format(new Date(longDate));
    }

    public static long getCurrentDate00HrsLong(long time) {
        String currentDate = getDateInString(System.currentTimeMillis());

        return (parseDateFromString(currentDate).getTime() + 3600000L + time);
    }

    public static String getTimeInString(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        return sdf.format(new Date(time));
    }

    public static long getCurrentTime() {
        return Calendar.getInstance().getTimeInMillis();
    }

    public static Date parseDateFromString(String dateString) {
        try {
            return DateFormat.getDateInstance(DateFormat.MEDIUM).parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        throw new UnsupportedOperationException();
    }

    public static long convertToTime(long time) {
        long i = 0;
        try {
            i = DateFormat.getTimeInstance(DateFormat.SHORT).parse(
                    DateFormat.getTimeInstance(DateFormat.SHORT).format(time)).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //throw new UnsupportedOperationException();
        return i;
    }

    public static long getCurrentTimeInTimeInstanceForm() {
        return convertToTime(Calendar.getInstance().getTimeInMillis());
    }
}
