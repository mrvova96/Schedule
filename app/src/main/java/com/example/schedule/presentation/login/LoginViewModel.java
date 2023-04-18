package com.example.schedule.presentation.login;

import android.app.Application;
import android.database.sqlite.SQLiteConstraintException;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelStore;

import com.example.schedule.data.database.ScheduleDatabase;
import com.example.schedule.data.database.dao.LoginDao;
import com.example.schedule.data.database.dao.StudentDao;
import com.example.schedule.data.database.entity.LoginDetails;
import com.example.schedule.data.repository.IScheduleRepository;
import com.example.schedule.data.repository.ScheduleRepository;
import com.example.schedule.data.roles.ScheduleRole;
import com.example.schedule.data.roles.ScheduleRolesManager;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LoginViewModel extends AndroidViewModel {

    // Роль ViewModel заключается в предоставлении данных в UI и сохранении изменений конфигурации
    // ViewModel действует как центр связи между репозиторием и пользовательским интерфейсом

    // ViewModel отвечает за создание экземпляра репозитория и за предоставление методов и
    // объектов LiveData для фрагментов

    private LoginDao loginDao;
    private IScheduleRepository repository;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private MutableLiveData<Boolean> isExistsLiveData = new MutableLiveData<>();
    private MutableLiveData<ScheduleRole> roleLiveData = new MutableLiveData<>();
    private Boolean validateInProcess;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        loginDao = ScheduleDatabase.getInstance(application).loginDao();
        repository = new ScheduleRepository(loginDao);

        LoginDetails loginDetails = new LoginDetails("st", "ps", 0, 1, 0);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                    //loginDao.insert(loginDetails);
            }
        });
        thread.start();
    }

    public LiveData<Boolean> isUserExists() {
        return isExistsLiveData;
    }

    public LiveData<ScheduleRole> getScheduleRole() {
        return roleLiveData;
    }

    public Boolean getValidateInProcess() {
        return validateInProcess;
    }

    public void setValidateInProcess(Boolean validateInProcess) {
        this.validateInProcess = validateInProcess;
    }

    public void inputCheck(String login, String password) {
        compositeDisposable.add(repository.isUserExists(login, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isExist -> {
                    isExistsLiveData.setValue(isExist);
                    if (isExist)
                        defineScheduleRole(login, password);
                }));
    }

    public void defineScheduleRole(String login, String password) {
            compositeDisposable.add(repository.getLoginInfo(login, password)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(loginDetails -> {
                        if (loginDetails.getAdminID() != 0)
                            roleLiveData.setValue(ScheduleRole.ADMIN);
                        if (loginDetails.getStudentID() != 0)
                            roleLiveData.setValue(ScheduleRole.STUDENT);
                        if (loginDetails.getTeacherID() != 0)
                            roleLiveData.setValue(ScheduleRole.TEACHER);
                    }));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}