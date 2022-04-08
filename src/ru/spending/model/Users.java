package ru.spending.model;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Users {
    private static Map<String, User> userMap = new HashMap<>();

    public User getUser(String name, String email, String password) {
        if (userMap.containsKey(email)) {
            return userMap.get(email);
        }
        return new User(name, email, password);
    }

    public User getUser(String name, String email, String password, LocalDate startPeriodDate) {
        if (userMap.containsKey(email)) {
            return userMap.get(email);
        }
        return new User(name, email, password, startPeriodDate);
    }
}
