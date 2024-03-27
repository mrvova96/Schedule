package com.example.schedule.presentation.student;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.schedule.data.database.ScheduleDatabase;
import com.example.schedule.data.database.entity.Student;
import com.example.schedule.data.repository.IScheduleRepository;
import com.example.schedule.data.repository.ScheduleRepository;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class StudentViewModel extends AndroidViewModel {

    private IScheduleRepository repository;
    private MutableLiveData<List<Student>> getStudents = new MutableLiveData<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public StudentViewModel(@NonNull Application application) {
        super(application);
        repository = new ScheduleRepository(ScheduleDatabase.getInstance(application).scheduleDao());
    }

    public LiveData<List<Student>> getStudents() {
        return getStudents;
    }

    public void refreshStudents(long groupID) {
        compositeDisposable.add(repository.getStudents(groupID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Student>>() {
                    @Override
                    public void accept(List<Student> studentsFromDB) throws Throwable {
                        getStudents.setValue(studentsFromDB);
                    }
                }));
    }

    public void remove(Student student, long groupID) {
        compositeDisposable.remove(repository.removeUser(student.getID())
                .subscribeOn(Schedulers.io())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Throwable {
                        refreshStudents(groupID);
                    }
                }));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
