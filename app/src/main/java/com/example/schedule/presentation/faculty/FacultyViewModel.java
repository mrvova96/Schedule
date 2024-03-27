package com.example.schedule.presentation.faculty;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.schedule.data.database.ScheduleDatabase;
import com.example.schedule.data.database.entity.Faculty;
import com.example.schedule.data.repository.IScheduleRepository;
import com.example.schedule.data.repository.ScheduleRepository;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FacultyViewModel extends AndroidViewModel {

    private IScheduleRepository repository;
    private MutableLiveData<List<Faculty>> faculties = new MutableLiveData<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public FacultyViewModel(@NonNull Application application) {
        super(application);
        repository = new ScheduleRepository(ScheduleDatabase.getInstance(application).scheduleDao());
    }

    public LiveData<List<Faculty>> getFaculties() {
        return faculties;
    }

    public void refreshFaculties() {
        compositeDisposable.add(repository.getFaculties()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Faculty>>() {
                    @Override
                    public void accept(List<Faculty> facultiesFromDB) {
                        faculties.setValue(facultiesFromDB);
                    }
                }));
    }

    public void remove(Faculty faculty) {
        compositeDisposable.add(repository.remove(faculty)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Throwable {
                        refreshFaculties();
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
