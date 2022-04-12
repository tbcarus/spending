package ru.spending.util;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    public static final SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd");
    public static final DateTimeFormatter DTFORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
    public static final LocalDateTime ALL_TIME_START = LocalDateTime.of(1900, 1, 1, 0, 0, 0);
    public static final LocalDateTime NOW = LocalDateTime.now();
}
