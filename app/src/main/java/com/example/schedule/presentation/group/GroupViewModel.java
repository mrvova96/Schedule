package com.example.schedule.presentation.group;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.schedule.data.database.ScheduleDatabase;
import com.example.schedule.data.database.entity.Group;
import com.example.schedule.data.repository.IScheduleRepository;
import com.example.schedule.data.repository.ScheduleRepository;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class GroupViewModel extends AndroidViewModel {

    private final IScheduleRepository repository;
    private final MutableLiveData<List<Group>> groups = new MutableLiveData<>();
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public GroupViewModel(@NonNull Application application) {
        super(application);
        repository = new ScheduleRepository(ScheduleDatabase
                .getInstance(application).scheduleDao());
    }

    public LiveData<List<Group>> getGroups() {
        return groups;
    }

    public void refreshGroups(int facultyID) {
        compositeDisposable.add(repository.getGroups(facultyID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Group>>() {
                    @Override
                    public void accept(List<Group> groupsFromDB) {
                        groups.setValue(groupsFromDB);
                    }
                }));
    }

    public void remove(Group group, int facultyID) {
        compositeDisposable.remove(repository.remove(group)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Throwable {
                        refreshGroups(facultyID);
                        removeLoginDetails();
                    }
                }));
    }

    public void removeLoginDetails() {
        compositeDisposable.add(repository.removeLoginDetails()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe());
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
