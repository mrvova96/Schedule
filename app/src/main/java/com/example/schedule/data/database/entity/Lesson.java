package com.example.schedule.data.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "lessons",
        indices = {@Index(value = {"lessonName", "lessonType", "facultyID"}, unique = true)},
        foreignKeys = @ForeignKey(onDelete = 5, entity = Faculty.class, parentColumns = "ID", childColumns = "facultyID")
)
public class Lesson {

    @PrimaryKey(autoGenerate = true)
    private int ID;
    private String lessonName;
    private String lessonType;
    private int facultyID;

    public Lesson(int ID, String lessonName, String lessonType, int facultyID) {
        this.ID = ID;
        this.lessonName = lessonName;
        this.lessonType = lessonType;
        this.facultyID = facultyID;
    }

    @Ignore
    public Lesson(String lessonName, String lessonType, int facultyID) {
        this(0, lessonName, lessonType, facultyID);
    }

    public int getID() {
        return ID;
    }

    public String getLessonName() {
        return lessonName;
    }

    public String getLessonType() {
        return lessonType;
    }

    public int getFacultyID() {
        return facultyID;
    }

    @NonNull
    @Override
    public String toString() {
        return lessonName.concat(" (").concat(lessonType).concat(")");
    }
}
