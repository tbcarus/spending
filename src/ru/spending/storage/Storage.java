package ru.spending.storage;

import ru.spending.model.Payment;
import ru.spending.model.PaymentType;
import ru.spending.model.User;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface Storage {
    // Payment
    void save(Payment p);

    Payment get(String id);

    void update(Payment p);

    void delete(String id);

    void clear();

    int size();

    Map<PaymentType, List<Payment>> getAllSorted(LocalDate startDate, LocalDate endDate);

    Map<PaymentType, List<Payment>> getAllSortedByUser(String userID, LocalDate startDate, LocalDate endDate);

    List<Payment> getAllByType(PaymentType paymentType);

    int getSumType(PaymentType paymentType);

//    Map<PaymentType, Integer> getSumMapByType(LocalDate startDate, LocalDate endDate);
//
//    Map<PaymentType, Integer> getSumMapByType(String userID, LocalDate startDate, LocalDate endDate);
//
//    int getSumAll(LocalDate startDate, LocalDate endDate);
//
//    int getSumAll(String userID, LocalDate startDate, LocalDate endDate);

    //User
    User getUser(String email);

    void updateUser(User user);
}
