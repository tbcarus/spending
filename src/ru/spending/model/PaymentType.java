package ru.spending.model;

public enum PaymentType {
    DINNER("Обед"),
    CAR("Машина"),
    GAS("Бензин"),
    FOOD("Продукты"),
    TRANSIT("Проезд"),
    ENTERTAINMENT("Развлечения"),
    CLOTH("Одежда"),
    PHARMACY("Аптека"),
    CHILDREN("Дети"),
    OTHER("Прочее");

    private final String title;

    PaymentType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
