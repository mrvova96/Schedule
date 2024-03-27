package com.example.schedule.data.repository;

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

public interface IScheduleRepository {

    // LoginDetails
    Single<Boolean> isLoginExists(String login);

    Single<Long> add(LoginDetails loginDetails);

    Completable removeUser(long ID);

    Completable update(LoginDetails loginDetails);

    Single<LoginDetails> get(long userUD);

    Single<LoginDetails> getLoginInfo(String login, String password);

    Completable removeLoginDetails();

    // Faculty
    Single<List<Faculty>> getFaculties();

    Single<Faculty> getFaculty(int facultyID);

    Completable add(Faculty faculty);

    Completable remove(Faculty faculty);

    Completable update(Faculty faculty);

    // Group
    Single<List<Group>> getGroups(int facultyID);

    Single<Group> getGroup(long groupID);

    Completable add(Group group);

    Completable remove(Group group);

    Completable update(Group group);

    // Classroom
    Single<List<Classroom>> getClassrooms(int facultyID);

    Completable add(Classroom classroom);

    Completable remove(Classroom classroom);

    Completable update(Classroom classroom);

    // Lesson
    Single<List<Lesson>> getLessons(int facultyID);

    Completable add(Lesson lesson);

    Completable remove(Lesson lesson);

    Completable update(Lesson lesson);

    // Student
    Single<List<Student>> getStudents(long groupID);

    Single<Student> getStudent(long studentID);

    Single<Boolean> isStudentExists(String surname, String name, String patronymic);

    Completable add(Student student);

    Completable remove(Student student);

    Completable update(Student student);

    // MainAdmin
    Single<MainAdmin> getMainAdmin(long adminID);

    Completable add(MainAdmin admin);

    // Admin
    Single<List<Admin>> getAdmins(int facultyID);

    Single<Admin> getAdmin(long adminID);

    Single<Boolean> isAdminExists(String surname, String name, String patronymic);

    Completable add(Admin admin);

    Completable remove(Admin admin);

    Completable update(Admin admin);

    // Teacher
    Single<List<Teacher>> getTeachers(int facultyID);

    Single<Teacher> getTeacher(long teacherID);

    Single<Boolean> isTeacherExists(String surname, String name, String patronymic);

    Completable add(Teacher teacher);

    Completable remove(Teacher teacher);

    Completable update(Teacher teacher);

    // TeacherLessonCrossRef
    Single<TeacherWithLessons> getTeacherWithLessons(long teacherID);

    Completable remove(long teacherID, int lessonID);

    Completable add(TeacherLessonCrossRef teacherLessonCrossRef);

    // Schedule
    Single<List<TeacherScheduleWithRelations>> getTeacherScheduleList(long teacherID, int dayID);

    Single<List<StudentScheduleWithRelations>> getStudentScheduleList(long groupID, int dayID);

    Single<Integer> getGroupCountWhereTeacherTeachesLesson(int classroomID, long teacherID, int lessonID, int lessonNumber, int dayID);

    Single<Boolean> checkClassroomBusy(int classroomID, int dayID, int lessonNumber);

    Single<Boolean> checkGroupBusy(long groupID, int dayID, int lessonNumber);

    Single<Boolean> checkTeacherBusy(long teacherID, int dayID, int lessonNumber);

    Completable add(Schedule... schedule);

    Completable remove(Schedule schedule);

    Completable update(Schedule... schedule);
}
