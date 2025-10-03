package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public final class DateUtil {
    private DateUtil(){}
    private static final String PATTERN_DATETIME = "yyyy-MM-dd HH:mm:ss";
    private static final String PATTERN_DATE = "yyyy-MM-dd";
    public static String formatDateTime(Date d) {
        return new SimpleDateFormat(PATTERN_DATETIME).format(d);
    }
    public static String formatDate(Date d) {
        return new SimpleDateFormat(PATTERN_DATE).format(d);
    }
    public static Date parseDate(String s) throws ParseException {
        return new SimpleDateFormat(PATTERN_DATE).parse(s);
    }
    public static Date parseDateTime(String s) throws ParseException {
        return new SimpleDateFormat(PATTERN_DATETIME).parse(s);
    }
    public static java.sql.Timestamp toTimestamp(Date d) {
        return new java.sql.Timestamp(d.getTime());
    }
    public static boolean isSameDay(Date a, Date b) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(a);
        Calendar cb = Calendar.getInstance();
        cb.setTime(b);
        return ca.get(Calendar.YEAR) == cb.get(Calendar.YEAR)
                && ca.get(Calendar.DAY_OF_YEAR) == cb.get(Calendar.DAY_OF_YEAR);
    }
}
