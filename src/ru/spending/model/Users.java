package ru.spending.model;

import ru.spending.util.Config;

import java.util.HashMap;
import java.util.Map;

public class Users {
    private static Map<String, User> userMap = new HashMap<>();


    public static User getUser(String email) {
        if (userMap.containsKey(email)) {
            return userMap.get(email);
        }
        return Config.getINSTANCE().getSqlStorage().getUser(email);
    }
}
