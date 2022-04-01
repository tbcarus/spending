package ru.spending.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {
    public static final SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd");
    public static final Date NOW = dateOnly(new Date());
    public static String defaultPeriodStartDate = startDatePeriodStr();
    public static String customStartDatePeriod;

    public static Calendar cal = new GregorianCalendar();

    public static Date dateOnly(Date date) {

        try {
            return FORMATTER.parse(FORMATTER.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date startDatePeriod() {
        Date date = (Date) NOW.clone();
        if (date.getDay() < 10) {
            date.setMonth(date.getMonth() - 1);
        }
        date.setDate(10);
        return dateOnly(date);
    }

    public static String startDatePeriodStr() {
        return FORMATTER.format(startDatePeriod());
    }

    public static Date endDatePeriod() {
        Date date = startDatePeriod();
        date.setMonth(date.getMonth() + 1);
        return date;
    }

    public static String endDatePeriodStr() {
        return FORMATTER.format(endDatePeriod());
    }

    public static Date startDatePeriod(Date inputDate) {
        Date date = (Date) inputDate.clone();
        if (date.getDay() < 10) {
            date.setMonth(date.getMonth() - 1);
        }
        date.setDate(10);
        return dateOnly(date);
    }

    public static String startDatePeriodStr(Date inputDate) {
        return FORMATTER.format(startDatePeriod(inputDate));
    }

    public static Date endDatePeriod(Date inputDate) {
        Date date = startDatePeriod(inputDate);
        date.setMonth(date.getMonth() + 1);
        return date;
    }

    public static String endDatePeriodStr(Date inputDate) {
        return FORMATTER.format(endDatePeriod(inputDate));
    }
}
