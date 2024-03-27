package com.example.schedule.data.enums;

import androidx.annotation.NonNull;

public enum WeekDay {

    MONDAY("Понедельник"),
    TUESDAY("Вторник"),
    WEDNESDAY("Среда"),
    THURSDAY("Четверг"),
    FRIDAY("Пятница"),
    SATURDAY("Суббота");

    private final String weekDay;

    WeekDay(String weekDay) {
        this.weekDay = weekDay;
    }

    @NonNull
    @Override
    public String toString() {
        return weekDay;
    }
}
