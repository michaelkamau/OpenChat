package mikekamau.com.openchat.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeUtils {

    public static String getTimeFromTimestamp(final String timestamp) {

        LocalDateTime dateTime = LocalDateTime.parse(timestamp);
        String timeStr = DateTimeFormatter.ofPattern("kkmm").format(dateTime);
        return timeStr;
    }
}
