package com.example.schedule.data.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.schedule.data.database.entity.LoginDetails;

import java.util.List;

import io.reactivex.rxjava3.core.Single;

@Dao
public interface LoginDao {

    @Query("SELECT EXISTS(SELECT login, password FROM login_details WHERE login = :login AND password = :password)")
    Single<Boolean> isUserExists(String login, String password);

    @Query("SELECT * FROM login_details WHERE login = :login AND password = :password")
    Single<LoginDetails> getLoginInfo(String login, String password);

    @Insert(entity = LoginDetails.class)
    void insert(LoginDetails loginDetails);

    @Query("SELECT * FROM login_details")
    List<LoginDetails> getAll();
}
