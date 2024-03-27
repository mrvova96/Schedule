package com.example.schedule.data.database.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "days",
        indices = {@Index(value = {"weekDay", "weekType"}, unique = true)}
)
public class Day {

    @PrimaryKey(autoGenerate = true)
    private int ID;
    private String weekDay;
    private String weekType;

    public Day(int ID, String weekDay, String weekType) {
        this.ID = ID;
        this.weekDay = weekDay;
        this.weekType = weekType;
    }

    @Ignore
    public Day(String weekDay, String weekType) {
        this(0, weekDay, weekType);
    }

    public int getID() {
        return ID;
    }

    public String getWeekDay() {
        return weekDay;
    }

    public String getWeekType() {
        return weekType;
    }
}
