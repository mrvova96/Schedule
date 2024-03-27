package com.example.schedule.presentation.teacher;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.schedule.data.database.ScheduleDatabase;
import com.example.schedule.data.database.entity.Lesson;
import com.example.schedule.data.database.entity.TeacherLessonCrossRef;
import com.example.schedule.data.repository.IScheduleRepository;
import com.example.schedule.data.repository.ScheduleRepository;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class TeacherLessonsAddViewModel extends AndroidViewModel {

    private IScheduleRepository repository;
    private MutableLiveData<List<Lesson>> getLessons = new MutableLiveData<>();
    private MutableLiveData<Boolean> isAdded = new MutableLiveData<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public TeacherLessonsAddViewModel(@NonNull Application application) {
        super(application);
        repository = new ScheduleRepository(ScheduleDatabase.getInstance(application).scheduleDao());
    }

    public LiveData<List<Lesson>> getLessons() {
        return getLessons;
    }

    public LiveData<Boolean> isAdded() {
        return isAdded;
    }

    public void refreshLessons(int facultyID) {
        compositeDisposable.add(repository.getLessons(facultyID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Lesson>>() {
                    @Override
                    public void accept(List<Lesson> lessonsFromDB) throws Throwable {
                        getLessons.setValue(lessonsFromDB);
                    }
                }));
    }

    public void add(long teacherID, int lessonID) {
        repository.add(new TeacherLessonCrossRef(teacherID, lessonID))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onComplete() {
                        isAdded.setValue(true);
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        isAdded.setValue(false);
                    }
                });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
