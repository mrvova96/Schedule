package com.example.schedule.data.database.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "teachers")
public class Teacher {

    @PrimaryKey(autoGenerate = true)
    private int ID;
    private String surname;
    private String name;
    private String patronymic;
    private int lessonID;
    private int facultyID;

    public Teacher(int ID, String surname, String name, String patronymic, int lessonID, int facultyID) {
        this.ID = ID;
        this.surname = surname;
        this.name = name;
        this.patronymic = patronymic;
        this.lessonID = lessonID;
        this.facultyID = facultyID;
    }

    @Ignore
    public Teacher(String surname, String name, String patronymic, int lessonID, int facultyID) {
        this(0, surname, name, patronymic, lessonID, facultyID);
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

    public int getLessonID() {
        return lessonID;
    }

    public int getFacultyID() {
        return facultyID;
    }
}
