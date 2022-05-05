package ru.spending.model;

import java.time.LocalDate;
import java.time.Year;
import java.util.UUID;

// Класс пользователя
public class User {
    private String uuid;
    private String name;
    private String email;
    private String password;
    private LocalDate startPeriodDate; // Дата начала периода учёта трат

    public User() {
    }

    public User(String name, String email, String password) {
        this(UUID.randomUUID().toString(), name, email, password, LocalDate.of(Year.now().getValue(), LocalDate.now().getMonth().getValue(), 10));
    }

    public User(String uuid, String name, String email, String password, LocalDate startPeriodDate) {
        this.uuid = uuid;
        this.name = name;
        this.email = email;
        this.password = password;
        this.startPeriodDate = startPeriodDate;
    }

    public String getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public LocalDate getStartPeriodDate() {
        return startPeriodDate;
    }

    // Дата окончания периода учёта трат. Период в программе принят равным 1 месяц с начала периода
    public LocalDate getEndPeriodDate() {
        return startPeriodDate.plusMonths(1).minusDays(1);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setStartPeriodDate(LocalDate startPeriodDate) {
        this.startPeriodDate = startPeriodDate;
    }
}
