package ru.spending.storage;

import ru.spending.model.Payment;
import ru.spending.model.PaymentType;
import ru.spending.model.User;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface Storage {
    void save(Payment p);

    Payment get(String id);

    User getUser(String email);

    void update(Payment p);

    void updateUser(User user);

    void delete(String id);

    void clear();

    int size();

    Map<PaymentType, List<Payment>> getAllSorted();

    Map<PaymentType, List<Payment>> getAllSortedByDate(Date startDate, Date endDate);

    List<Payment> getAllByType(PaymentType paymentType);

    int getSumType(PaymentType paymentType);

    Map<PaymentType, Integer> getSumMapByType();

    int getSumAll();
}
