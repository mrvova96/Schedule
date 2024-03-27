package com.example.schedule.data.repository;

import com.example.schedule.data.database.dao.ScheduleDao;
import com.example.schedule.data.database.entity.Admin;
import com.example.schedule.data.database.entity.Classroom;
import com.example.schedule.data.database.entity.Faculty;
import com.example.schedule.data.database.entity.Group;
import com.example.schedule.data.database.entity.Lesson;
import com.example.schedule.data.database.entity.LoginDetails;
import com.example.schedule.data.database.entity.MainAdmin;
import com.example.schedule.data.database.entity.Schedule;
import com.example.schedule.data.database.entity.Student;
import com.example.schedule.data.database.entity.Teacher;
import com.example.schedule.data.database.entity.TeacherLessonCrossRef;
import com.example.schedule.data.database.entity.relation.StudentScheduleWithRelations;
import com.example.schedule.data.database.entity.relation.TeacherScheduleWithRelations;
import com.example.schedule.data.database.entity.relation.TeacherWithLessons;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public class ScheduleRepository implements IScheduleRepository {

    private final ScheduleDao scheduleDao;

    public ScheduleRepository(ScheduleDao scheduleDao) {
        this.scheduleDao = scheduleDao;
    }

    // LoginDetails
    @Override
    public Single<Boolean> isLoginExists(String login) {
        return scheduleDao.isLoginExists(login);
    }

    @Override
    public Single<Long> add(LoginDetails loginDetails) {
        return scheduleDao.add(loginDetails);
    }

    @Override
    public Completable removeUser(long ID) {
        return scheduleDao.removeUser(ID);
    }

    @Override
    public Completable update(LoginDetails loginDetails) {
        return scheduleDao.update(loginDetails);
    }

    @Override
    public Single<LoginDetails> get(long userUD) {
        return scheduleDao.get(userUD);
    }

    @Override
    public Single<LoginDetails> getLoginInfo(String login, String password) {
        return scheduleDao.getLoginInfo(login, password);
    }

    @Override
    public Completable removeLoginDetails() {
        return scheduleDao.removeLoginDetails();
    }

    // Faculty
    @Override
    public Single<List<Faculty>> getFaculties() {
        return scheduleDao.getFaculties();
    }

    @Override
    public Single<Faculty> getFaculty(int facultyID) {
        return scheduleDao.getFaculty(facultyID);
    }

    @Override
    public Completable add(Faculty faculty) {
        return scheduleDao.add(faculty);
    }

    @Override
    public Completable remove(Faculty faculty) {
        return scheduleDao.remove(faculty);
    }

    @Override
    public Completable update(Faculty faculty) {
        return scheduleDao.update(faculty);
    }

    // Group
    @Override
    public Single<List<Group>> getGroups(int facultyID) {
        return scheduleDao.getGroups(facultyID);
    }

    @Override
    public Single<Group> getGroup(long groupID) {
        return scheduleDao.getGroup(groupID);
    }

    @Override
    public Completable add(Group group) {
        return scheduleDao.add(group);
    }

    @Override
    public Completable remove(Group group) {
        return scheduleDao.remove(group);
    }

    @Override
    public Completable update(Group group) {
        return scheduleDao.update(group);
    }

    // Classroom
    @Override
    public Single<List<Classroom>> getClassrooms(int facultyID) {
        return scheduleDao.getClassrooms(facultyID);
    }

    @Override
    public Completable add(Classroom classroom) {
        return scheduleDao.add(classroom);
    }

    @Override
    public Completable remove(Classroom classroom) {
        return scheduleDao.remove(classroom);
    }

    @Override
    public Completable update(Classroom classroom) {
        return scheduleDao.update(classroom);
    }

    // Lesson
    @Override
    public Single<List<Lesson>> getLessons(int facultyID) {
        return scheduleDao.getLessons(facultyID);
    }

    @Override
    public Completable add(Lesson lesson) {
        return scheduleDao.add(lesson);
    }

    @Override
    public Completable remove(Lesson lesson) {
        return scheduleDao.remove(lesson);
    }

    @Override
    public Completable update(Lesson lesson) {
        return scheduleDao.update(lesson);
    }

    // Student
    @Override
    public Single<List<Student>> getStudents(long groupID) {
        return scheduleDao.getStudents(groupID);
    }

    @Override
    public Single<Student> getStudent(long studentID) {
        return scheduleDao.getStudent(studentID);
    }

    @Override
    public Single<Boolean> isStudentExists(String surname, String name, String patronymic) {
        return scheduleDao.isStudentExists(surname, name, patronymic);
    }

    @Override
    public Completable add(Student student) {
        return scheduleDao.add(student);
    }

    @Override
    public Completable remove(Student student) {
        return scheduleDao.remove(student);
    }

    @Override
    public Completable update(Student student) {
        return scheduleDao.update(student);
    }

    // MainAdmin
    @Override
    public Single<MainAdmin> getMainAdmin(long adminID) {
        return scheduleDao.getMainAdmin(adminID);
    }

    @Override
    public Completable add(MainAdmin admin) {
        return scheduleDao.add(admin);
    }

    // Admin
    @Override
    public Single<List<Admin>> getAdmins(int facultyID) {
        return scheduleDao.getAdmins(facultyID);
    }

    @Override
    public Single<Admin> getAdmin(long adminID) {
        return scheduleDao.getAdmin(adminID);
    }

    @Override
    public Single<Boolean> isAdminExists(String surname, String name, String patronymic) {
        return scheduleDao.isAdminExists(surname, name, patronymic);
    }

    @Override
    public Completable add(Admin admin) {
        return scheduleDao.add(admin);
    }

    @Override
    public Completable remove(Admin admin) {
        return scheduleDao.remove(admin);
    }

    @Override
    public Completable update(Admin admin) {
        return scheduleDao.update(admin);
    }

    // Teacher
    @Override
    public Single<List<Teacher>> getTeachers(int facultyID) {
        return scheduleDao.getTeachers(facultyID);
    }

    @Override
    public Single<Teacher> getTeacher(long teacherID) {
        return scheduleDao.getTeacher(teacherID);
    }

    @Override
    public Single<Boolean> isTeacherExists(String surname, String name, String patronymic) {
        return scheduleDao.isTeacherExists(surname, name, patronymic);
    }

    @Override
    public Completable add(Teacher teacher) {
        return scheduleDao.add(teacher);
    }

    @Override
    public Completable remove(Teacher teacher) {
        return scheduleDao.remove(teacher);
    }

    @Override
    public Completable update(Teacher teacher) {
        return scheduleDao.update(teacher);
    }

    // TeacherLessonCrossRef
    @Override
    public Single<TeacherWithLessons> getTeacherWithLessons(long teacherID) {
        return scheduleDao.getTeacherWithLessons(teacherID);
    }

    @Override
    public Completable remove(long teacherID, int lessonID) {
        return scheduleDao.remove(teacherID, lessonID);
    }

    @Override
    public Completable add(TeacherLessonCrossRef teacherLessonCrossRef) {
        return scheduleDao.add(teacherLessonCrossRef);
    }

    // Schedule
    @Override
    public Single<List<TeacherScheduleWithRelations>> getTeacherScheduleList(long teacherID, int dayID) {
        return scheduleDao.getTeacherScheduleList(teacherID, dayID);
    }

    @Override
    public Single<List<StudentScheduleWithRelations>> getStudentScheduleList(long groupID, int dayID) {
        return scheduleDao.getStudentScheduleList(groupID, dayID);
    }

    @Override
    public Single<Integer> getGroupCountWhereTeacherTeachesLesson(int classroomID, long teacherID, int lessonID, int lessonNumber, int dayID) {
        return scheduleDao.getGroupCountWhereTeacherTeachesLesson(classroomID, teacherID, lessonID, lessonNumber, dayID);
    }

    @Override
    public Single<Boolean> checkClassroomBusy(int classroomID, int dayID, int lessonNumber) {
        return scheduleDao.checkClassroomBusy(classroomID, dayID, lessonNumber);
    }

    @Override
    public Single<Boolean> checkGroupBusy(long groupID, int dayID, int lessonNumber) {
        return scheduleDao.checkGroupBusy(groupID, dayID, lessonNumber);
    }

    @Override
    public Single<Boolean> checkTeacherBusy(long teacherID, int dayID, int lessonNumber) {
        return scheduleDao.checkTeacherBusy(teacherID, dayID, lessonNumber);
    }

    @Override
    public Completable add(Schedule... schedule) {
        return scheduleDao.add(schedule);
    }

    @Override
    public Completable remove(Schedule schedule) {
        return scheduleDao.remove(schedule);
    }

    @Override
    public Completable update(Schedule... schedule) {
        return scheduleDao.update(schedule);
    }

}
