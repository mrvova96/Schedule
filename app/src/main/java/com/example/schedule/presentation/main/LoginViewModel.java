package com.example.schedule.presentation.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.schedule.data.database.ScheduleDatabase;
import com.example.schedule.data.database.entity.Admin;
import com.example.schedule.data.database.entity.Faculty;
import com.example.schedule.data.database.entity.Group;
import com.example.schedule.data.database.entity.LoginDetails;
import com.example.schedule.data.database.entity.MainAdmin;
import com.example.schedule.data.database.entity.Student;
import com.example.schedule.data.database.entity.Teacher;
import com.example.schedule.data.repository.IScheduleRepository;
import com.example.schedule.data.repository.ScheduleRepository;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LoginViewModel extends AndroidViewModel {

    private IScheduleRepository repository;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private MutableLiveData<MainAdmin> getMainAdminData = new MutableLiveData<>();
    private MutableLiveData<Admin> getAdminData = new MutableLiveData<>();
    private MutableLiveData<Student> getStudentData = new MutableLiveData<>();
    private MutableLiveData<Teacher> getTeacherData = new MutableLiveData<>();
    private MutableLiveData<Group> getGroupData = new MutableLiveData<>();
    private MutableLiveData<Faculty> getFacultyData = new MutableLiveData<>();
    private MutableLiveData<Boolean> getError = new MutableLiveData<>();

    public LoginViewModel(@NonNull Application application) {
        super(application);
        repository = new ScheduleRepository(ScheduleDatabase.getInstance(application).scheduleDao());
    }

    public LiveData<MainAdmin> getMainAdminData() {
        return getMainAdminData;
    }

    public LiveData<Admin> getAdminData() {
        return getAdminData;
    }

    public LiveData<Student> getStudentData() {
        return getStudentData;
    }

    public LiveData<Teacher> getTeacherData() {
        return getTeacherData;
    }

    public LiveData<Group> getGroupData() {
        return getGroupData;
    }

    public LiveData<Faculty> getFacultyData() {
        return getFacultyData;
    }

    public LiveData<Boolean> getError() {
        return getError;
    }

    public void defineUserType(String login, String password) {
        repository.getLoginInfo(login, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull LoginDetails loginDetails) {
                        defineUserByID(loginDetails.getID());
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        getError.setValue(true);
                    }
                });
    }

    public void defineUserByID(long userID) {
        getMainAdmin(userID);
    }

    public void getMainAdmin(long userID) {
        repository.getMainAdmin(userID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull MainAdmin mainAdmin) {
                        getMainAdminData.setValue(mainAdmin);
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        getAdmin(userID);
                    }
                });
    }

    public void getAdmin(long userID) {
        repository.getAdmin(userID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull Admin admin) {
                        getAdminData.setValue(admin);
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        getStudent(userID);
                    }
                });
    }

    public void getStudent(long userID) {
        repository.getStudent(userID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull Student student) {
                        getStudentData.setValue(student);
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        getTeacher(userID);
                    }
                });
    }

    public void getTeacher(long userID) {
        compositeDisposable.add(repository.getTeacher(userID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Teacher>() {
                    @Override
                    public void accept(Teacher teacher) throws Throwable {
                        getTeacherData.setValue(teacher);
                    }
                }));
    }

    public void getGroup(long groupID) {
        compositeDisposable.add(repository.getGroup(groupID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Group>() {
                    @Override
                    public void accept(Group group) throws Throwable {
                        getGroupData.setValue(group);
                    }
                }));
    }

    public void getFaculty(int facultyID) {
        compositeDisposable.add(repository.getFaculty(facultyID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Faculty>() {
                    @Override
                    public void accept(Faculty faculty) throws Throwable {
                        getFacultyData.setValue(faculty);
                    }
                }));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}