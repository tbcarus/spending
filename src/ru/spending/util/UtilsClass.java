package ru.spending.util;

import ru.spending.model.Payment;
import ru.spending.model.PaymentType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UtilsClass {

    public static int maxSize(Map<PaymentType, List<Payment>> map) {
        int max = 0;
        for (PaymentType pt : map.keySet()) {
            if (max < map.get(pt).size()) {
                max = map.get(pt).size();
            }
        }
        return max;
    }

    public static int getSumByType(List<Payment> list) {
        int sum = 0;
        for (Payment p : list) {
            sum += p.getPrise();
        }
        return sum;
    }

    public static Map<PaymentType, Integer> getSumMapByType(Map<PaymentType, List<Payment>> map) {
        Map<PaymentType, Integer> sumMap = new HashMap<>();
        for (PaymentType pt : map.keySet()) {
            sumMap.put(pt, getSumByType(map.get(pt)));
        }
        return sumMap;
    }

    public static int getSumAll(Map<PaymentType, Integer> map) {
        int sum = 0;
        for (PaymentType pt : map.keySet()) {
            sum += map.get(pt);
        }
        return sum;
    }
}
