package com.example.schedule.presentation.teacher;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.schedule.data.database.ScheduleDatabase;
import com.example.schedule.data.database.entity.Teacher;
import com.example.schedule.data.repository.IScheduleRepository;
import com.example.schedule.data.repository.ScheduleRepository;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class TeacherViewModel extends AndroidViewModel {

    private IScheduleRepository repository;
    private MutableLiveData<List<Teacher>> getTeachers = new MutableLiveData<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public TeacherViewModel(@NonNull Application application) {
        super(application);
        repository = new ScheduleRepository(ScheduleDatabase.getInstance(application).scheduleDao());
    }

    public LiveData<List<Teacher>> getTeachers() {
        return getTeachers;
    }

    public void refreshTeachers(int facultyID) {
        compositeDisposable.add(repository.getTeachers(facultyID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Teacher>>() {
                    @Override
                    public void accept(List<Teacher> teachersFromDB) throws Throwable {
                        getTeachers.setValue(teachersFromDB);
                    }
                }));
    }

    public void remove(Teacher teacher, int facultyID) {
        compositeDisposable.remove(repository.removeUser(teacher.getID())
                .subscribeOn(Schedulers.io())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Throwable {
                        refreshTeachers(facultyID);
                    }
                }));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
