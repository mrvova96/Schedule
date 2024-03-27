package com.example.schedule.data.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "classrooms",
        indices = {@Index(value = {"classroomNumber", "facultyID"}, unique = true)},
        foreignKeys = @ForeignKey(onDelete = 5, entity = Faculty.class, parentColumns = "ID", childColumns = "facultyID")
)
public class Classroom {

    @PrimaryKey(autoGenerate = true)
    private int ID;
    private int classroomNumber;
    private int facultyID;

    public Classroom(int ID, int classroomNumber, int facultyID) {
        this.ID = ID;
        this.classroomNumber = classroomNumber;
        this.facultyID = facultyID;
    }

    @Ignore
    public Classroom(int classroomNumber, int facultyID) {
        this(0, classroomNumber, facultyID);
    }

    public int getID() {
        return ID;
    }

    public int getClassroomNumber() {
        return classroomNumber;
    }

    public int getFacultyID() {
        return facultyID;
    }

    @NonNull
    @Override
    public String toString() {
        return String.valueOf(classroomNumber);
    }
}
