package edu.utdallas.cs.app.database.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class DateUtil {
    private DateUtil() {
    }

    public static Date createDate(SimpleDateFormat dateFormat, String raw) throws ParseException {
        if (raw.endsWith("Z")) {
            // This is UTC time with a "Z" suffix. Replace "Z" with "+0000" to match the date format string.
            raw = raw.substring(0, raw.length() - 1) + "+0000";
        } else {
            // This is a timezone offset time. Insert a colon ":" between the hour and minute components of the offset.
            int offsetIndex = raw.length() - 6;
            String offset = raw.substring(offsetIndex);
            raw = raw.substring(0, offsetIndex) + offset.substring(0, 3) + ":" + offset.substring(3);
        }
        return dateFormat.parse(raw);
    }
}
