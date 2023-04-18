package com.example.schedule.data.database.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "students")
public class Student {

    @PrimaryKey(autoGenerate = true)
    private int ID;
    private String surname;
    private String name;
    private String patronymic;
    private int groupID;

    public Student(int ID, String surname, String name, String patronymic, int groupID) {
        this.ID = ID;
        this.surname = surname;
        this.name = name;
        this.patronymic = patronymic;
        this.groupID = groupID;
    }

    @Ignore
    public Student(String surname, String name, String patronymic, int groupID) {
        this(0, surname, name, patronymic, groupID);
    }

    public int getID() {
        return ID;
    }

    public String getSurname() {
        return surname;
    }

    public String getName() {
        return name;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public int getGroupID() {
        return groupID;
    }
}
