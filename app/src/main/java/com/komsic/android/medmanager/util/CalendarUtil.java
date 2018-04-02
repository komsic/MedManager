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
}
