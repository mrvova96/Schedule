package com.example.schedule.presentation.admin;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.schedule.data.database.ScheduleDatabase;
import com.example.schedule.data.database.entity.Admin;
import com.example.schedule.data.repository.IScheduleRepository;
import com.example.schedule.data.repository.ScheduleRepository;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AdminViewModel extends AndroidViewModel {

    private IScheduleRepository repository;
    private MutableLiveData<List<Admin>> getAdmins = new MutableLiveData<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public AdminViewModel(@NonNull Application application) {
        super(application);
        repository = new ScheduleRepository(ScheduleDatabase.getInstance(application).scheduleDao());
    }

    public LiveData<List<Admin>> getAdmins() {
        return getAdmins;
    }

    public void refreshAdmins(int facultyID) {
        compositeDisposable.add(repository.getAdmins(facultyID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Admin>>() {
                    @Override
                    public void accept(List<Admin> adminsFromDB) {
                        getAdmins.setValue(adminsFromDB);
                    }
                }));
    }

    public void remove(Admin admin, int facultyID) {
        compositeDisposable.remove(repository.removeUser(admin.getID())
                .subscribeOn(Schedulers.io())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Throwable {
                        refreshAdmins(facultyID);
                    }
                }));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
