package mikekamau.com.openchat.utils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;

public class TimeUtils {

    private static final String TAG = "TimeUtils";

    public static String getTimeFromTimestamp(final String currentDateTime) {

        DateTime now = new DateTime(currentDateTime).withZoneRetainFields(DateTimeZone.getDefault());
        org.joda.time.format.DateTimeFormatter dateTimeFormatter = DateTimeFormat.shortTime();
        return dateTimeFormatter.print(now);
    }
}
