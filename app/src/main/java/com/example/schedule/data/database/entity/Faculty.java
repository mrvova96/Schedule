package com.example.schedule.data.database.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "faculties",
        indices = {@Index(value = "facultyName", unique = true)}
)
public class Faculty {

    @PrimaryKey(autoGenerate = true)
    private int ID;
    private String facultyName;

    public Faculty(int ID, String facultyName) {
        this.ID = ID;
        this.facultyName = facultyName;
    }

    @Ignore
    public Faculty(String facultyName) {
        this(0, facultyName);
    }

    public int getID() {
        return ID;
    }

    public String getFacultyName() {
        return facultyName;
    }
}
