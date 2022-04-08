package ru.spending.model;

import java.time.LocalDate;
import java.time.Year;
import java.util.HashMap;
import java.util.Map;

public class User {
    private String name;
    private String email;
    private String password;
    private LocalDate startPeriodDate;
    private LocalDate endPeriodDate;

    public User() {
    }

    protected User(String name, String email, String password) {
        this(name, email, password, LocalDate.of(Year.now().getValue(), LocalDate.now().getMonth().getValue(), 10));
    }

    protected User(String name, String email, String password, LocalDate startPeriodDate) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.startPeriodDate = startPeriodDate;
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

    public LocalDate getEndPeriodDate() {
        return startPeriodDate.plusMonths(1);
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
