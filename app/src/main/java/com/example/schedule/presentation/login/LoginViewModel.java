package com.example.schedule.presentation.login;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.schedule.ScheduleRepository;

public class LoginViewModel extends ViewModel {

    private ScheduleRepository repository;

    public MutableLiveData<LoginState> loginLiveData = new MutableLiveData<>();


    public LoginViewModel(ScheduleRepository repository) {
        this.repository = repository;
    }

    public void login(String login, String password) {
        // базовую валидацию можно делать прямо тут, например, правильность ввода
        loginLiveData.postValue(new LoginState(new LoginError(""), repository.login(login, password)));
    }
}
