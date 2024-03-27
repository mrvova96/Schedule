package com.example.schedule.presentation.schedule;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.schedule.data.database.ScheduleDatabase;
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

public class ScheduleUpdateViewModel extends AndroidViewModel {

    private IScheduleRepository repository;
    private MutableLiveData<List<Lesson>> lessons = new MutableLiveData<>();
    private MutableLiveData<List<Group>> groups = new MutableLiveData<>();
    private MutableLiveData<List<Teacher>> teachers = new MutableLiveData<>();
    private MutableLiveData<List<Classroom>> classrooms = new MutableLiveData<>();
    private MutableLiveData<Boolean> classroomIsBusy = new MutableLiveData<>();
    private MutableLiveData<Integer> groupIsBusy = new MutableLiveData<>();
    private MutableLiveData<Boolean> teacherIsBusy = new MutableLiveData<>();
    private MutableLiveData<Boolean> isUpdated = new MutableLiveData<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private Schedule scheduleForUpdate2;
    private Schedule schedule2;

    public ScheduleUpdateViewModel(@NonNull Application application) {
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

    public LiveData<Boolean> isUpdated() {
        return isUpdated;
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

    public void updateForTeacher(Schedule scheduleForUpdate1, Schedule scheduleForUpdate2, Schedule schedule1, Schedule schedule2) {
        this.scheduleForUpdate2 = scheduleForUpdate2;
        this.schedule2 = schedule2;
        if (scheduleForUpdate2 != null && schedule2 == null)
            checkGroupBusyForNewSchedule(scheduleForUpdate1, scheduleForUpdate2, schedule1);

        else if (scheduleForUpdate2 == null) {
            if (scheduleForUpdate1.getGroupID() == schedule1.getGroupID() && scheduleForUpdate1.getClassroomID() == schedule1.getClassroomID())
                updateSchedule(scheduleForUpdate1);
            else if (scheduleForUpdate1.getGroupID() == schedule1.getGroupID() && scheduleForUpdate1.getClassroomID() != schedule1.getClassroomID())
                checkClassroomBusy(scheduleForUpdate1, null);
            else if (scheduleForUpdate1.getGroupID() != schedule1.getGroupID() && scheduleForUpdate1.getClassroomID() == schedule1.getClassroomID())
                checkGroupBusy(scheduleForUpdate1, null, 0);
            else
                checkClassroomAndGroupBusy(scheduleForUpdate1, null);

        } else {
            if (scheduleForUpdate1.getGroupID() == schedule1.getGroupID() && scheduleForUpdate2.getGroupID() == schedule2.getGroupID() && scheduleForUpdate1.getClassroomID() == schedule1.getClassroomID())
                updateSchedule(scheduleForUpdate1, scheduleForUpdate2);
            else if (scheduleForUpdate1.getGroupID() == schedule1.getGroupID() && scheduleForUpdate2.getGroupID() == schedule2.getGroupID() && scheduleForUpdate1.getClassroomID() != schedule1.getClassroomID())
                checkClassroomBusy(scheduleForUpdate1, scheduleForUpdate2);
            else if (scheduleForUpdate1.getGroupID() != schedule1.getGroupID() && scheduleForUpdate2.getGroupID() == schedule2.getGroupID() && scheduleForUpdate1.getClassroomID() == schedule1.getClassroomID())
                checkGroupBusy(scheduleForUpdate1, null, 1);
            else if (scheduleForUpdate1.getGroupID() == schedule1.getGroupID() && scheduleForUpdate2.getGroupID() != schedule2.getGroupID() && scheduleForUpdate1.getClassroomID() == schedule1.getClassroomID())
                checkGroupBusy(scheduleForUpdate2, null, 2);
            else if (scheduleForUpdate1.getGroupID() != schedule1.getGroupID() && scheduleForUpdate2.getGroupID() != schedule2.getGroupID() && scheduleForUpdate1.getClassroomID() == schedule1.getClassroomID())
                checkGroupBusy(scheduleForUpdate1, scheduleForUpdate2, 1);
            else if (scheduleForUpdate1.getGroupID() != schedule1.getGroupID() && scheduleForUpdate2.getGroupID() == schedule2.getGroupID() && scheduleForUpdate1.getClassroomID() != schedule1.getClassroomID())
                checkClassroomAndGroupBusy(scheduleForUpdate1, null);
            else if (scheduleForUpdate1.getGroupID() == schedule1.getGroupID() && scheduleForUpdate2.getGroupID() != schedule2.getGroupID() && scheduleForUpdate1.getClassroomID() != schedule1.getClassroomID())
                checkClassroomAndGroupBusy(scheduleForUpdate2, null);
            else
                checkClassroomAndGroupBusy(scheduleForUpdate1, scheduleForUpdate2);
        }
    }

    public void updateForStudent(Schedule schedule, Schedule scheduleForUpdate) {
        getGroupCountWhereTeacherTeachesLesson(schedule, scheduleForUpdate);
    }

    private void getGroupCountWhereTeacherTeachesLesson(Schedule schedule, Schedule scheduleForUpdate) {
        compositeDisposable.add(repository.getGroupCountWhereTeacherTeachesLesson(schedule.getClassroomID(),
                        schedule.getTeacherID(), schedule.getLessonID(), schedule.getLessonNumber(), schedule.getDayID())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer count) throws Throwable {
                        compositeDisposable.add(repository.getGroupCountWhereTeacherTeachesLesson(
                                        scheduleForUpdate.getClassroomID(), scheduleForUpdate.getTeacherID(),
                                        scheduleForUpdate.getLessonID(), scheduleForUpdate.getLessonNumber(),
                                        scheduleForUpdate.getDayID())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<Integer>() {
                                    @Override
                                    public void accept(Integer countForUpdate) throws Throwable {
                                        switch (countForUpdate) {
                                            case 0:
                                                if (count == 1) {
                                                    if (scheduleForUpdate.getTeacherID() == schedule.getTeacherID() &&
                                                            scheduleForUpdate.getClassroomID() == schedule.getClassroomID())
                                                        updateSchedule(scheduleForUpdate);
                                                    else if (scheduleForUpdate.getTeacherID() == schedule.getTeacherID() &&
                                                            scheduleForUpdate.getClassroomID() != schedule.getClassroomID())
                                                        checkClassroomBusy(scheduleForUpdate, null);
                                                    else if (scheduleForUpdate.getTeacherID() != schedule.getTeacherID() &&
                                                            scheduleForUpdate.getClassroomID() == schedule.getClassroomID())
                                                        checkTeacherBusy(scheduleForUpdate);
                                                    else
                                                        checkClassroomAndTeacherBusy(scheduleForUpdate);
                                                } else {
                                                    if (scheduleForUpdate.getTeacherID() == schedule.getTeacherID() &&
                                                            scheduleForUpdate.getClassroomID() == schedule.getClassroomID() &&
                                                            scheduleForUpdate.getLessonID() == schedule.getLessonID())
                                                        updateSchedule(scheduleForUpdate);
                                                    else if (scheduleForUpdate.getTeacherID() != schedule.getTeacherID() &&
                                                            scheduleForUpdate.getClassroomID() != schedule.getClassroomID())
                                                        checkClassroomAndTeacherBusy(scheduleForUpdate);
                                                    else if (scheduleForUpdate.getTeacherID() == schedule.getTeacherID())
                                                        teacherIsBusy.setValue(true);
                                                    else
                                                        classroomIsBusy.setValue(true);
                                                }
                                                break;
                                            case 1:
                                                updateSchedule(scheduleForUpdate);
                                                break;
                                            default:
                                                if (count.equals(countForUpdate))
                                                    updateSchedule(scheduleForUpdate);
                                                else
                                                    isUpdated.setValue(false);
                                                break;
                                        }
                                    }
                                }));
                    }
                }));
    }

    public void checkClassroomAndGroupBusy(Schedule schedule1, Schedule schedule2) {
        compositeDisposable.add(repository.checkClassroomBusy(
                        schedule1.getClassroomID(), schedule1.getDayID(), schedule1.getLessonNumber())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean isBusy) throws Throwable {
                        if (isBusy)
                            classroomIsBusy.setValue(true);
                        else if (schedule2 == null)
                            checkGroupBusy(schedule1, schedule2, 0);
                        else {
                            compositeDisposable.add(repository.checkClassroomBusy(
                                            schedule2.getClassroomID(), schedule2.getDayID(), schedule2.getLessonNumber())
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Consumer<Boolean>() {
                                        @Override
                                        public void accept(Boolean isBusy) throws Throwable {
                                            if (isBusy)
                                                classroomIsBusy.setValue(true);
                                            else
                                                checkGroupBusy(schedule1, schedule2, 1);
                                        }
                                    }));
                        }
                    }
                }));
    }

    public void checkClassroomAndTeacherBusy(Schedule schedule) {
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
                            checkTeacherBusy(schedule);
                    }
                }));
    }

    public void checkGroupBusy(Schedule schedule1, Schedule schedule2, int busyCode) {
        compositeDisposable.add(repository.checkGroupBusy(
                        schedule1.getGroupID(), schedule1.getDayID(), schedule1.getLessonNumber())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean isBusy) throws Throwable {
                        if (isBusy)
                            groupIsBusy.setValue(busyCode);
                        else if (schedule2 == null)
                            updateSchedule(schedule1);
                        else {
                            compositeDisposable.add(repository.checkGroupBusy(
                                            schedule2.getGroupID(), schedule2.getDayID(), schedule2.getLessonNumber())
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Consumer<Boolean>() {
                                        @Override
                                        public void accept(Boolean isBusy) throws Throwable {
                                            if (isBusy)
                                                groupIsBusy.setValue(2);
                                            else
                                                updateSchedule(schedule1, schedule2);
                                        }
                                    }));
                        }
                    }
                }));
    }

    public void checkGroupBusyForNewSchedule(Schedule scheduleForUpdate1, Schedule scheduleForUpdate2, Schedule schedule1) {
        compositeDisposable.add(repository.checkGroupBusy(
                        scheduleForUpdate2.getGroupID(), scheduleForUpdate2.getDayID(), scheduleForUpdate2.getLessonNumber())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean isBusy) throws Throwable {
                        if (isBusy)
                            groupIsBusy.setValue(2);
                        else {
                            if (scheduleForUpdate1.getGroupID() == schedule1.getGroupID() && scheduleForUpdate1.getClassroomID() == schedule1.getClassroomID())
                                updateSchedule(scheduleForUpdate1);
                            else if (scheduleForUpdate1.getGroupID() == schedule1.getGroupID() && scheduleForUpdate1.getClassroomID() != schedule1.getClassroomID())
                                checkClassroomBusy(scheduleForUpdate1, null);
                            else if (scheduleForUpdate1.getGroupID() != schedule1.getGroupID() && scheduleForUpdate1.getClassroomID() == schedule1.getClassroomID())
                                checkGroupBusy(scheduleForUpdate1, null, 1);
                            else
                                checkClassroomAndGroupBusy(scheduleForUpdate1, null);
                        }
                    }
                }));
    }

    public void checkTeacherBusy(Schedule schedule) {
        compositeDisposable.add(repository.checkTeacherBusy(
                        schedule.getTeacherID(), schedule.getDayID(), schedule.getLessonNumber())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean isBusy) throws Throwable {
                        if (isBusy)
                            teacherIsBusy.setValue(true);
                        else
                            updateSchedule(schedule);
                    }
                }));
    }

    public void checkClassroomBusy(Schedule schedule1, Schedule schedule2) {
        compositeDisposable.add(repository.checkClassroomBusy(
                        schedule1.getClassroomID(), schedule1.getDayID(), schedule1.getLessonNumber())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean isBusy) throws Throwable {
                        if (isBusy)
                            classroomIsBusy.setValue(true);
                        else if (schedule2 == null)
                            updateSchedule(schedule1);
                        else {
                            compositeDisposable.add(repository.checkClassroomBusy(
                                            schedule2.getClassroomID(), schedule2.getDayID(), schedule2.getLessonNumber())
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Consumer<Boolean>() {
                                        @Override
                                        public void accept(Boolean isBusy) throws Throwable {
                                            if (isBusy)
                                                classroomIsBusy.setValue(true);
                                            else
                                                updateSchedule(schedule1, schedule2);
                                        }
                                    }));
                        }
                    }
                }));
    }

    public void updateSchedule(Schedule schedule) {
        compositeDisposable.add(repository.update(schedule)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Throwable {
                        if (scheduleForUpdate2 == null && schedule2 != null)
                            removeSchedule(schedule2);
                        if (scheduleForUpdate2 != null && schedule2 == null)
                            addSchedule(scheduleForUpdate2);
                        isUpdated.setValue(true);
                    }
                }));
    }

    public void updateSchedule(Schedule schedule1, Schedule schedule2) {
        compositeDisposable.add(repository.update(schedule1, schedule2)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Throwable {
                        isUpdated.setValue(true);
                    }
                }));
    }

    private void addSchedule(Schedule schedule) {
        compositeDisposable.add(repository.add(schedule)
                .subscribeOn(Schedulers.io())
                .subscribe());
    }

    public void removeSchedule(Schedule schedule) {
        compositeDisposable.add(repository.remove(schedule)
                .subscribeOn(Schedulers.io())
                .subscribe());
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
