package com.example.schedule.presentation.teacher;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.schedule.data.database.ScheduleDatabase;
import com.example.schedule.data.database.entity.Lesson;
import com.example.schedule.data.database.entity.relation.TeacherWithLessons;
import com.example.schedule.data.repository.IScheduleRepository;
import com.example.schedule.data.repository.ScheduleRepository;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class TeacherLessonsViewModel extends AndroidViewModel {

    private IScheduleRepository repository;
    private MutableLiveData<List<Lesson>> lessons = new MutableLiveData<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public TeacherLessonsViewModel(@NonNull Application application) {
        super(application);
        repository = new ScheduleRepository(ScheduleDatabase.getInstance(application).scheduleDao());
    }

    public LiveData<List<Lesson>> getLessons() {
        return lessons;
    }

    public void refreshLessons(long teacherID) {
        compositeDisposable.add(repository.getTeacherWithLessons(teacherID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<TeacherWithLessons>() {
                    @Override
                    public void accept(TeacherWithLessons teacherWithLessonsFromDB) {
                        lessons.setValue(teacherWithLessonsFromDB.lessons);
                    }
                }));
    }

    public void remove(long teacherID, Lesson lesson) {
        compositeDisposable.add(repository.remove(teacherID, lesson.getID())
                .subscribeOn(Schedulers.io())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Throwable {
                        refreshLessons(teacherID);
                    }
                }));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
