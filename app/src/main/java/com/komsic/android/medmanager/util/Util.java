package com.komsic.android.medmanager.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by komsic on 4/3/2018.
 */

public class Util {

    public static List<String> sortDaysOfWeek(Set<String> days) {
        ArrayList<String> sortedDays = new ArrayList<>();

        if (days.contains("sun"))
            sortedDays.add("sun");
        if (days.contains("mon"))
            sortedDays.add("mon");
        if (days.contains("tue"))
            sortedDays.add("tue");
        if (days.contains("wed"))
            sortedDays.add("wed");
        if (days.contains("thu"))
            sortedDays.add("thu");
        if (days.contains("fri"))
            sortedDays.add("fri");
        if (days.contains("sat"))
            sortedDays.add("sat");

        return sortedDays;
    }
}
