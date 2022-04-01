package ru.spending.model;

import ru.spending.util.DateUtil;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class Payment {
    private String id;
    private PaymentType type;
    private int prise;
    private String description;
    private Date date;
    private String userID;

    public Payment() {

    }

    public Payment(Date date) {
        this.date = date;
        this.id = UUID.randomUUID().toString();
    }

    public Payment(String id, Date date) {
        this.id = id;
        this.date = date;
    }

    public Payment(PaymentType type, int prise) {
        this(type, prise, "");
    }

    public Payment(PaymentType type, int prise, String description) {
        this(type, prise, description, new Date(), "1");
    }

    public Payment(String id, PaymentType type, int prise, String description, Date date, String userID) {
        this.id = id;
        this.type = type;
        this.prise = prise;
        this.description = description;
        this.date = date;
        this.userID = userID;
    }

    public Payment(PaymentType type, int prise, String description, Date date, String userID) {
        this(UUID.randomUUID().toString(), type, prise, description, date, userID);
    }

    public String getId() {
        return id;
    }

    public PaymentType getType() {
        return type;
    }

    public int getPrise() {
        return prise;
    }

    public String getDescription() {
        return description;
    }

    public Date getDate() {
        return date;
    }

    public String getUserID() {
        return userID;
    }

    @Override
    public String toString() {
        return id + " " + type.getTitle() + " " + prise + " " + description + " " + DateUtil.FORMATTER.format(date) + " " + userID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Payment payment = (Payment) o;
        return prise == payment.prise && id.equals(payment.id) && type == payment.type && description.equals(payment.description) && date.equals(payment.date) && userID.equals(payment.userID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, prise, description, date, userID);
    }
}
