package ru.spending.storage;

import ru.spending.model.Payment;
import ru.spending.model.PaymentType;
import ru.spending.model.User;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface Storage {
    // Методы для Payment
    void save(Payment p);

    Payment get(String id);

    void update(Payment p);

    void delete(String id);

    void clear();

    int size();

    // Получение всех трат всех пользователей за период
    Map<PaymentType, List<Payment>> getAllSorted(LocalDate startDate, LocalDate endDate);

    // Получение всех трат пользователя за период
    Map<PaymentType, List<Payment>> getAllSortedByUser(String userID, LocalDate startDate, LocalDate endDate);

    // Получение всех трат определённого типа за период
    List<Payment> getAllByType(PaymentType paymentType, String userId, LocalDate startDate, LocalDate endDate);

    // Не используется в программе
    int getSumType(PaymentType paymentType, String userId);

    // Методы для User
    User getUserByEmail(String email);

    User getUserById(String id);

    void updateUser(User user);
}
