package org.ternence.compressionfile.utils;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.Log;

import java.util.Calendar;

public class CalendarUtils {

    private static final String TAG = "CalendarUtils";

    public static void openCalendar(Activity activity) {
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(2012, 0, 19, 1, 30);
        Calendar endTime = Calendar.getInstance();
        endTime.set(2012, 0, 19, 8, 30);
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                .putExtra(CalendarContract.Events.TITLE, "Yoga")
                .putExtra(CalendarContract.Events.DESCRIPTION, "Group class")
                .putExtra(CalendarContract.Events.EVENT_LOCATION, "The gym")
                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);
        activity.startActivity(intent);
    }

}
