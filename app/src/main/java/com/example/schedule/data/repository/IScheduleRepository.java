package com.example.schedule.data.repository;

import androidx.lifecycle.LiveData;

import com.example.schedule.data.database.entity.LoginDetails;
import com.example.schedule.data.roles.ScheduleRole;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Single;

public interface IScheduleRepository {

    Single<Boolean> isUserExists(String login, String password);

    Single<LoginDetails> getLoginInfo(String login, String password);
}
