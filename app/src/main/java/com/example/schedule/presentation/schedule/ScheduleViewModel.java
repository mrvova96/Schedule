package com.example.schedule.presentation.schedule;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.schedule.data.database.ScheduleDatabase;
import com.example.schedule.data.database.entity.Schedule;
import com.example.schedule.data.database.entity.relation.StudentScheduleWithRelations;
import com.example.schedule.data.database.entity.relation.TeacherScheduleWithRelations;
import com.example.schedule.data.repository.IScheduleRepository;
import com.example.schedule.data.repository.ScheduleRepository;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ScheduleViewModel extends AndroidViewModel {

    private IScheduleRepository repository;
    private MutableLiveData<List<TeacherScheduleWithRelations>> teacherScheduleList = new MutableLiveData<>();
    private MutableLiveData<List<StudentScheduleWithRelations>> studentScheduleList = new MutableLiveData<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public ScheduleViewModel(@NonNull Application application) {
        super(application);
        repository = new ScheduleRepository(ScheduleDatabase.getInstance(application).scheduleDao());
    }

    public LiveData<List<TeacherScheduleWithRelations>> getTeacherScheduleList() {
        return teacherScheduleList;
    }

    public LiveData<List<StudentScheduleWithRelations>> getStudentScheduleList() {
        return studentScheduleList;
    }

    public void refreshTeacherScheduleList(long teacherID, int dayID) {
        compositeDisposable.add(repository.getTeacherScheduleList(teacherID, dayID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<TeacherScheduleWithRelations>>() {
                    @Override
                    public void accept(List<TeacherScheduleWithRelations> scheduleListFromDB) {
                        teacherScheduleList.setValue(scheduleListFromDB);
                    }
                }));
    }

    public void refreshStudentScheduleList(long groupID, int dayID) {
        compositeDisposable.add(repository.getStudentScheduleList(groupID, dayID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<StudentScheduleWithRelations>>() {
                    @Override
                    public void accept(List<StudentScheduleWithRelations> scheduleListFromDB) {
                        studentScheduleList.setValue(scheduleListFromDB);
                    }
                }));
    }

    public void remove(Schedule schedule, long groupID, long teacherID) {
        compositeDisposable.add(repository.remove(schedule)
                .subscribeOn(Schedulers.io())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Throwable {
                        if (groupID == 0)
                            refreshTeacherScheduleList(teacherID, schedule.getDayID());
                        if (teacherID == 0)
                            refreshStudentScheduleList(groupID, schedule.getDayID());
                    }
                }));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
