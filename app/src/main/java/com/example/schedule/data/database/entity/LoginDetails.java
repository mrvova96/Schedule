package com.example.schedule.data.database.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "login_details", indices = {@Index(value = "login", unique = true)})
public class LoginDetails {

    @PrimaryKey(autoGenerate = true)
    private int ID;
    private String login;
    @ColumnInfo()
    private String password;
    @ColumnInfo(defaultValue = "0")
    private int adminID;
    @ColumnInfo(defaultValue = "0")
    private int studentID;
    @ColumnInfo(defaultValue = "0")
    private int teacherID;

    public LoginDetails(int ID, String login, String password, int adminID, int studentID, int teacherID) {
        this.ID = ID;
        this.login = login;
        this.password = password;
        this.adminID = adminID;
        this.studentID = studentID;
        this.teacherID = teacherID;
    }

    @Ignore
    public LoginDetails(String login, String password, int adminID, int studentID, int teacherID) {
        this(0, login, password, adminID, studentID, teacherID);
    }

    public int getID() {
        return ID;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public int getAdminID() {
        return adminID;
    }

    public int getStudentID() {
        return studentID;
    }

    public int getTeacherID() {
        return teacherID;
    }
}
