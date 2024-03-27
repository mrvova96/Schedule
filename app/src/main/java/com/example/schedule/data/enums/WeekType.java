package com.example.schedule.data.enums;

import androidx.annotation.NonNull;

public enum WeekType {

    NUMERATOR("Числитель"),
    DENOMINATOR("Знаменатель");

    private final String weekType;

    WeekType(String weekType) {
        this.weekType = weekType;
    }

    @NonNull
    @Override
    public String toString() {
        return weekType;
    }
}
