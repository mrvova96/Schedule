package com.example.schedule.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.schedule.data.database.dao.AdminDao;
import com.example.schedule.data.database.dao.LoginDao;
import com.example.schedule.data.database.dao.StudentDao;
import com.example.schedule.data.database.entity.LoginDetails;
import com.example.schedule.data.roles.ScheduleRole;
import com.example.schedule.data.roles.ScheduleRolesManager;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ScheduleRepository implements IScheduleRepository {

    // Класс репозитория абстрагирует доступ к нескольким источникам данных. Репозиторий
    // не является частью библиотек компонентов архитектуры, но является рекомендуемой
    // практикой для разделения кода и чистой архитектуры

    // Репозиторий содержит весь код, необходимый для обработки всех источников данных,
    // используемых приложением

    // Класс будет отвечать за взаимодействие с базой данных Room от имени ViewModel и должен
    // будет предоставить методы, которые используют DAO

    private LoginDao loginDao;
    private StudentDao studentDao;
    private AdminDao adminDao;
    private ScheduleRolesManager rolesManager = ScheduleRolesManager.getInstance();

    public ScheduleRepository(LoginDao loginDao) {
        this.loginDao = loginDao;
    }

    public ScheduleRepository(StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    public ScheduleRepository(AdminDao adminDao) {
        this.adminDao = adminDao;
    }

    @Override
    public Single<Boolean> isUserExists(String login, String password) {
        return loginDao.isUserExists(login, password);
    }

    @Override
    public Single<LoginDetails> getLoginInfo(String login, String password) {
        return loginDao.getLoginInfo(login, password);
    }
}
