package ru.spending.model;

import ru.spending.util.Config;
import java.util.HashMap;
import java.util.Map;

// Утильный класс для хранения юзеров из базы в мапе
public class Users {
    // В мапу попадает юзер, который залогинился. Далее, пока юзер не вышел,
    // он берётся из мапы, а не из базы.
    private static Map<String, User> userMap = new HashMap<>();


    public static User getUserByEmail(String email) {
        if (userMap.containsKey(email)) {
            return userMap.get(email);
        }
        return Config.getINSTANCE().getSqlStorage().getUserByEmail(email);
    }
}
