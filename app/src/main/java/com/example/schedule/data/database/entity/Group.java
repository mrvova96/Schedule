package com.example.schedule.data.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "groups",
        indices = {@Index(value = "groupName", unique = true)},
        foreignKeys = @ForeignKey(onDelete = 5, entity = Faculty.class, parentColumns = "ID", childColumns = "facultyID")
)
public class Group {

    @PrimaryKey(autoGenerate = true)
    private final long ID;
    private final String groupName;
    private final int facultyID;

    public Group(long ID, String groupName, int facultyID) {
        this.ID = ID;
        this.groupName = groupName;
        this.facultyID = facultyID;
    }

    @Ignore
    public Group(String groupName, int facultyID) {
        this(0, groupName, facultyID);
    }

    public long getID() {
        return ID;
    }

    public String getGroupName() {
        return groupName;
    }

    public int getFacultyID() {
        return facultyID;
    }

    @NonNull
    @Override
    public String toString() {
        return groupName;
    }
}
