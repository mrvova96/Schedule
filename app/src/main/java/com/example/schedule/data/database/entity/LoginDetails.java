package com.example.schedule.data.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "login_details",
        indices = @Index(value = "login", unique = true)

)
public class LoginDetails {

    @PrimaryKey(autoGenerate = true)
    private long ID;
    private String login;
    private String password;

    public LoginDetails(long ID, String login, String password) {
        this.ID = ID;
        this.login = login;
        this.password = password;
    }

    @Ignore
    public LoginDetails(String login, String password) {
        this(0, login, password);
    }

    public long getID() {
        return ID;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
