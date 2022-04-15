package ru.spending.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    public static final SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd");
    public static final DateTimeFormatter DTFORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
    public static final DateTimeFormatter DTFORMATTER_DATE_ONLY = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final LocalDateTime ALL_TIME_START = LocalDateTime.of(1900, 1, 1, 0, 0, 0);
    public static final LocalDateTime NOW = LocalDateTime.now();

    public static LocalDate getStartPeriod(int day) {
        if (day < NOW.getDayOfMonth()) {
            return LocalDate.of(NOW.getYear(), NOW.getMonthValue(), day);
        } else {
            return LocalDate.of(NOW.getYear(), NOW.getMonthValue(), day).minusMonths(1);
        }
    }
}
