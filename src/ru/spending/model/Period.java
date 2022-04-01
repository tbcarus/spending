package ru.spending.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Period {
    private static int count;
    private static Map<String, Period> instanceMap = new HashMap<>();
    private String userID;
    private int startDate;
    private Date startDatePeriod;
    private Date endDatePeriod;

    public static Period getInstance(String userID, int startDate, Date startDatePeriod) {
        if (!instanceMap.containsKey(userID)) {
            return new Period(userID, startDate, startDatePeriod);
        }
        Period period = instanceMap.get(userID);
        period.startDate = startDate;
        period.startDatePeriod = startDatePeriod;
        period.endDatePeriod = null;
        return period;
    }

    private Period(String userID, int startDate, Date startDatePeriod) {
        this.userID = userID;
        this.startDate = startDate;
        this.startDatePeriod = startDatePeriod;

    }
}
