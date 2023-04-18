package com.example.schedule.data.database.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "schedule")
public class Schedule {

    @PrimaryKey(autoGenerate = true)
    private int ID;

    public Schedule(int ID) {
        this.ID = ID;
    }

    @Ignore
    public Schedule() {
        this(0);
    }

    public int getID() {
        return ID;
    }
}
