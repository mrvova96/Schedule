package com.example.schedule.presentation.admin;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.schedule.data.database.ScheduleDatabase;
import com.example.schedule.data.database.entity.Admin;
import com.example.schedule.data.database.entity.LoginDetails;
import com.example.schedule.data.repository.IScheduleRepository;
import com.example.schedule.data.repository.ScheduleRepository;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AdminUpdateViewModel extends AndroidViewModel {

    private IScheduleRepository repository;
    private MutableLiveData<LoginDetails> getLoginDetails = new MutableLiveData<>();
    private MutableLiveData<Boolean> loginIsExist = new MutableLiveData<>();
    private MutableLiveData<Boolean> isUpdated = new MutableLiveData<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public AdminUpdateViewModel(@NonNull Application application) {
        super(application);
        repository = new ScheduleRepository(ScheduleDatabase.getInstance(application).scheduleDao());
    }

    public LiveData<LoginDetails> getLoginData() {
        return getLoginDetails;
    }

    public LiveData<Boolean> loginIsExist() {
        return loginIsExist;
    }

    public LiveData<Boolean> isUpdated() {
        return isUpdated;
    }

    public void getLoginDetails(long adminID) {
        compositeDisposable.add(repository.get(adminID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LoginDetails>() {
                    @Override
                    public void accept(LoginDetails loginDetails) throws Throwable {
                        getLoginDetails.setValue(loginDetails);
                    }
                }));
    }

    public void isLoginExists(String login) {
        compositeDisposable.add(repository.isLoginExists(login)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isExist -> {
                    loginIsExist.setValue(isExist);
                }));
    }

    public void update(Admin admin, String login, String password) {
        repository.update(admin)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onComplete() {
                        updateLoginDetails(new LoginDetails(admin.getID(), login, password));
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        isUpdated.setValue(false);
                    }
                });
    }

    private void updateLoginDetails(LoginDetails loginDetails) {
        compositeDisposable.add(repository.update(loginDetails)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Throwable {
                        isUpdated.setValue(true);
                    }
                }));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
