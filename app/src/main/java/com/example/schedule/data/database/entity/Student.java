package com.example.schedule.data.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "students",
        indices = @Index(value = {"surname", "name", "patronymic"}, unique = true),
        foreignKeys = {
                @ForeignKey(onDelete = 5, entity = Group.class, parentColumns = "ID", childColumns = "groupID"),
                @ForeignKey(onDelete = 5, entity = LoginDetails.class, parentColumns = "ID", childColumns = "ID")
        }
)
public class Student {

    @PrimaryKey
    private long ID;
    private String surname;
    private String name;
    private String patronymic;
    private long groupID;

    public Student(long ID, String surname, String name, String patronymic, long groupID) {
        this.ID = ID;
        this.surname = surname;
        this.name = name;
        this.patronymic = patronymic;
        this.groupID = groupID;
    }

    public long getID() {
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

    public long getGroupID() {
        return groupID;
    }

    @NonNull
    @Override
    public String toString() {
        return getSurname().concat(" ")
                .concat(String.valueOf(getName().charAt(0))).concat(". ")
                .concat(String.valueOf(getPatronymic().charAt(0))).concat(".");
    }
}
