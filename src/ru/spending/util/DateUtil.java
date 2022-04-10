package ru.spending.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {
    public static final SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd");
    public static final DateTimeFormatter DTFORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
    public static final LocalDateTime ALL_TIME_START = LocalDateTime.of(1900, 1, 1, 0, 0, 0);
    public static final LocalDateTime NOW = LocalDateTime.now();

//    public static LocalDate startDatePeriod(LocalDate inputDate) {
//        LocalDate date = inputDate;
//        if (NOW.getDayOfMonth() < 10) {
//            date = date.minusMonths(1);
//        }
//        return date;
//    }
//
//    public static LocalDate endDatePeriod(LocalDate inputDate) {
//        LocalDate date = startDatePeriod(inputDate);
//        return date.plusMonths(1);
//    }
//
//    public static String toStr(LocalDate date) {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        return date.format(formatter);
//    }
}
