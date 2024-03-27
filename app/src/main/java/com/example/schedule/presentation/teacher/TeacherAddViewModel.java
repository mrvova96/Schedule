package com.example.schedule.presentation.teacher;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.schedule.data.database.ScheduleDatabase;
import com.example.schedule.data.database.entity.LoginDetails;
import com.example.schedule.data.database.entity.Teacher;
import com.example.schedule.data.repository.IScheduleRepository;
import com.example.schedule.data.repository.ScheduleRepository;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class TeacherAddViewModel extends AndroidViewModel {

    private IScheduleRepository repository;
    private MutableLiveData<Boolean> isTeacherExists = new MutableLiveData<>();
    private MutableLiveData<Boolean> isAdded = new MutableLiveData<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public TeacherAddViewModel(@NonNull Application application) {
        super(application);
        repository = new ScheduleRepository(ScheduleDatabase.getInstance(application).scheduleDao());
    }

    public LiveData<Boolean> isTeacherExists() {
        return isTeacherExists;
    }

    public LiveData<Boolean> isAdded() {
        return isAdded;
    }

    public void checkTeacher(String surname, String name, String patronymic) {
        compositeDisposable.add(repository.isTeacherExists(surname, name, patronymic)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean isExist) throws Throwable {
                        isTeacherExists.setValue(isExist);
                    }
                }));
    }

    public void addLoginDetails(LoginDetails loginDetails, String surname, String name, String patronymic, int facultyID) {
        repository.add(loginDetails)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull Long ID) {
                        add(new Teacher(ID, surname, name, patronymic, facultyID));
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        isAdded.setValue(false);
                    }
                });
    }

    private void add(Teacher teacher) {
        compositeDisposable.add(repository.add(teacher)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Throwable {
                        isAdded.setValue(true);
                    }
                }));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
