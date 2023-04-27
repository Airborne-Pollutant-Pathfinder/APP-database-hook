package edu.utdallas.cs.app.database.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

public final class DateUtil {
    private DateUtil() {
    }

    public static Date createDate(DateTimeFormatter formatter, String raw) throws ParseException {
        LocalDateTime dateTime = LocalDateTime.parse(raw, formatter.withZone(ZoneOffset.UTC));
        return Date.from(dateTime.atZone(ZoneOffset.UTC).toInstant());
    }
}
