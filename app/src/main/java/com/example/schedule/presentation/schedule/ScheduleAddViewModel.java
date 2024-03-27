package com.example.schedule.presentation.schedule;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.schedule.data.database.ScheduleDatabase;
import com.example.schedule.data.database.dao.ScheduleDao;
import com.example.schedule.data.database.entity.Classroom;
import com.example.schedule.data.database.entity.Group;
import com.example.schedule.data.database.entity.Lesson;
import com.example.schedule.data.database.entity.Schedule;
import com.example.schedule.data.database.entity.Teacher;
import com.example.schedule.data.database.entity.relation.TeacherWithLessons;
import com.example.schedule.data.repository.IScheduleRepository;
import com.example.schedule.data.repository.ScheduleRepository;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ScheduleAddViewModel extends AndroidViewModel {

    private IScheduleRepository repository;
    private MutableLiveData<List<Lesson>> lessons = new MutableLiveData<>();
    private MutableLiveData<List<Group>> groups = new MutableLiveData<>();
    private MutableLiveData<List<Teacher>> teachers = new MutableLiveData<>();
    private MutableLiveData<List<Classroom>> classrooms = new MutableLiveData<>();
    private MutableLiveData<Boolean> classroomIsBusy = new MutableLiveData<>();
    private MutableLiveData<Integer> groupIsBusy = new MutableLiveData<>();
    private MutableLiveData<Boolean> teacherIsBusy = new MutableLiveData<>();
    private MutableLiveData<Boolean> isAdded = new MutableLiveData<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Schedule schedule1;

    public ScheduleAddViewModel(@NonNull Application application) {
        super(application);
        repository = new ScheduleRepository(ScheduleDatabase.getInstance(application).scheduleDao());
    }

    public LiveData<List<Lesson>> getLessons() {
        return lessons;
    }

    public LiveData<List<Group>> getGroups() {
        return groups;
    }

    public LiveData<List<Teacher>> getTeachers() {
        return teachers;
    }

    public LiveData<List<Classroom>> getClassrooms() {
        return classrooms;
    }

    public LiveData<Boolean> checkClassroomBusy() {
        return classroomIsBusy;
    }

    public LiveData<Integer> checkGroupBusy() {
        return groupIsBusy;
    }

    public LiveData<Boolean> checkTeacherBusy() {
        return teacherIsBusy;
    }

    public LiveData<Boolean> isAdded() {
        return isAdded;
    }

    public void refreshLessons(long teacherID) {
        compositeDisposable.add(repository.getTeacherWithLessons(teacherID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<TeacherWithLessons>() {
                    @Override
                    public void accept(TeacherWithLessons lessonsFromDB) {
                        lessons.setValue(lessonsFromDB.lessons);
                    }
                }));
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

    public void refreshTeachers(int facultyID) {
        compositeDisposable.add(repository.getTeachers(facultyID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Teacher>>() {
                    @Override
                    public void accept(List<Teacher> teachersFromDB) {
                        teachers.setValue(teachersFromDB);
                    }
                }));
    }

    public void refreshClassrooms(int facultyID) {
        compositeDisposable.add(repository.getClassrooms(facultyID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Classroom>>() {
                    @Override
                    public void accept(List<Classroom> classroomsFromDB) {
                        classrooms.setValue(classroomsFromDB);
                    }
                }));
    }

    public void addForTeacher(Schedule schedule, Schedule schedule2) {
        if (schedule2 != null)
            checkClassroomBusy(schedule, schedule2, 1);
        else
            checkClassroomBusy(schedule, schedule2, 0);
    }

    public void addForStudent(Schedule schedule) {
        getGroupCountWhereTeacherTeachesLesson(schedule);
    }

    private void getGroupCountWhereTeacherTeachesLesson(Schedule schedule) {
        compositeDisposable.add(repository.getGroupCountWhereTeacherTeachesLesson(schedule.getClassroomID(),
                        schedule.getTeacherID(), schedule.getLessonID(), schedule.getLessonNumber(), schedule.getDayID())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer count) throws Throwable {
                        switch (count) {
                            case 0:
                                checkClassroomBusy(schedule, null, 0);
                                break;
                            case 1:
                                addSchedule(schedule);
                                break;
                            default:
                                isAdded.setValue(false);
                                break;
                        }
                    }
                }));
    }

    private void checkClassroomBusy(Schedule schedule, Schedule schedule2, int busyCode) {
        compositeDisposable.add(repository.checkClassroomBusy(
                        schedule.getClassroomID(), schedule.getDayID(), schedule.getLessonNumber())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean isBusy) throws Throwable {
                        if (isBusy)
                            classroomIsBusy.setValue(true);
                        else
                            checkGroupBusy(schedule, schedule2, busyCode);
                    }
                }));
    }

    private void checkGroupBusy(Schedule schedule, Schedule schedule2, int busyCode) {
        compositeDisposable.add(repository.checkGroupBusy(
                        schedule.getGroupID(), schedule.getDayID(), schedule.getLessonNumber())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean isBusy) throws Throwable {
                        if (isBusy) {
                            groupIsBusy.setValue(busyCode);
                            schedule1 = null;
                        }
                        else
                            checkTeacherBusy(schedule, schedule2);
                    }
                }));
    }

    private void checkTeacherBusy(Schedule schedule, Schedule schedule2) {
        compositeDisposable.add(repository.checkTeacherBusy(
                        schedule.getTeacherID(), schedule.getDayID(), schedule.getLessonNumber())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean isBusy) throws Throwable {
                        if (isBusy)
                            teacherIsBusy.setValue(true);
                        else if (schedule1 == null) {
                            if (schedule2 != null) {
                                schedule1 = schedule;
                                checkClassroomBusy(schedule2, null, 2);
                            } else
                                addSchedule(schedule);
                        } else {
                            addSchedule(schedule, schedule1);
                            schedule1 = null;
                        }
                    }
                }));
    }

    private void addSchedule(Schedule schedule) {
        compositeDisposable.add(repository.add(schedule)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Throwable {
                        isAdded.setValue(true);
                    }
                }));
    }

    private void addSchedule(Schedule schedule, Schedule schedule1) {
        compositeDisposable.add(repository.add(schedule, schedule1)
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
