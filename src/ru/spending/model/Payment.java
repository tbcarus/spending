package ru.spending.model;

import ru.spending.util.DateUtil;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

// Класс траты
public class Payment {
    private String id; // ID траты в базе
    private PaymentType type; // Тип траты
    private int price; // Сумма траты
    private String description; // Описание траты
    private LocalDate date; // Дата совершения траты. Устанавливается автоматически или выбирается вручную
    private String userID; // Владелец траты

    public Payment() {

    }

    public Payment(LocalDate date) {
        this(UUID.randomUUID().toString(), date);
    }

    public Payment(String id, LocalDate date) {
        this.id = id;
        this.date = date;
    }

    public Payment(PaymentType type, int price) {
        this(type, price, "");
    }

    public Payment(PaymentType type, int price, String description) {
        this(type, price, description, LocalDate.now(), "1");
    }

    public Payment(PaymentType type, int price, String description, LocalDate localDate) {
        this(type, price, description, localDate, "1");
    }

    public Payment(PaymentType type, int price, String description, LocalDate date, String userID) {
        this(UUID.randomUUID().toString(), type, price, description, date, userID);
    }

    public Payment(String id, PaymentType type, int price, String description, LocalDate date, String userID) {
        this.id = id;
        this.type = type;
        this.price = price;
        this.description = description;
        this.date = date;
        this.userID = userID;
    }

    public String getId() {
        return id;
    }

    public PaymentType getType() {
        return type;
    }

    public int getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getUserID() {
        return userID;
    }

    @Override
    public String toString() {
        return id + " " + type.getTitle() + " " + price + " " + description + " " + DateUtil.FORMATTER.format(date) + " " + userID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Payment payment = (Payment) o;
        return price == payment.price && id.equals(payment.id) && type == payment.type && description.equals(payment.description) && date.equals(payment.date) && userID.equals(payment.userID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, price, description, date, userID);
    }
}
