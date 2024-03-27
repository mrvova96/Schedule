package com.example.schedule.data.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.schedule.data.database.entity.Admin;
import com.example.schedule.data.database.entity.Classroom;
import com.example.schedule.data.database.entity.Day;
import com.example.schedule.data.database.entity.Faculty;
import com.example.schedule.data.database.entity.Group;
import com.example.schedule.data.database.entity.Lesson;
import com.example.schedule.data.database.entity.LessonsTime;
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

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface ScheduleDao {

    // LoginDetails
    @Query("SELECT EXISTS(SELECT login FROM login_details WHERE login = :login)")
    Single<Boolean> isLoginExists(String login);

    @Insert
    Single<Long> add(LoginDetails loginDetails);

    @Query("DELETE FROM login_details WHERE ID = :ID")
    Completable removeUser(long ID);

    @Update
    Completable update(LoginDetails loginDetails);

    @Query("SELECT * FROM login_details WHERE ID = :userID")
    Single<LoginDetails> get(long userID);

    @Query("SELECT * FROM login_details WHERE login = :login AND password = :password")
    Single<LoginDetails> getLoginInfo(String login, String password);

    @Query("DELETE FROM login_details WHERE ID NOT IN(SELECT students.ID FROM students " +
            "UNION SELECT teachers.ID FROM teachers " +
            "UNION SELECT admins.ID FROM admins)")
    Completable removeLoginDetails();

    // Faculty
    @Query("SELECT * FROM faculties ORDER BY facultyName")
    Single<List<Faculty>> getFaculties();

    @Query("SELECT * FROM faculties WHERE ID = :facultyID")
    Single<Faculty> getFaculty(int facultyID);

    @Insert
    Completable add(Faculty faculty);

    @Delete
    Completable remove(Faculty faculty);

    @Update
    Completable update(Faculty faculty);

    // Group
    @Query("SELECT * FROM groups WHERE facultyID = :facultyID ORDER BY groupName")
    Single<List<Group>> getGroups(int facultyID);

    @Query("SELECT * FROM groups WHERE ID = :groupID")
    Single<Group> getGroup(long groupID);

    @Insert
    Completable add(Group group);

    @Delete
    Completable remove(Group group);

    @Update
    Completable update(Group group);

    // Classroom
    @Query("SELECT * FROM classrooms WHERE facultyID = :facultyID ORDER BY classroomNumber")
    Single<List<Classroom>> getClassrooms(int facultyID);

    @Insert
    Completable add(Classroom classroom);

    @Delete
    Completable remove(Classroom classroom);

    @Update
    Completable update(Classroom classroom);

    // Lesson
    @Query("SELECT * FROM lessons WHERE facultyID = :facultyID ORDER BY lessonName")
    Single<List<Lesson>> getLessons(int facultyID);

    @Insert
    Completable add(Lesson lesson);

    @Delete
    Completable remove(Lesson lesson);

    @Update
    Completable update(Lesson lesson);

    // Student
    @Query("SELECT * FROM students WHERE groupID = :groupID ORDER BY surname")
    Single<List<Student>> getStudents(long groupID);

    @Query("SELECT * FROM students WHERE ID = :studentID")
    Single<Student> getStudent(long studentID);

    @Query("SELECT EXISTS(SELECT * FROM students WHERE surname = :surname AND name = :name AND patronymic = :patronymic)")
    Single<Boolean> isStudentExists(String surname, String name, String patronymic);

    @Insert
    Completable add(Student student);

    @Delete
    Completable remove(Student student);

    @Update
    Completable update(Student student);

    // MainAdmin
    @Query("SELECT * FROM main_admins WHERE ID = :adminID")
    Single<MainAdmin> getMainAdmin(long adminID);

    @Insert
    Completable add(MainAdmin admin);

    // Admin
    @Query("SELECT * FROM admins WHERE facultyID = :facultyID ORDER BY surname")
    Single<List<Admin>> getAdmins(int facultyID);

    @Query("SELECT * FROM admins WHERE ID = :adminID")
    Single<Admin> getAdmin(long adminID);

    @Query("SELECT EXISTS(SELECT * FROM admins WHERE surname = :surname AND name = :name AND patronymic = :patronymic)")
    Single<Boolean> isAdminExists(String surname, String name, String patronymic);

    @Insert
    Completable add(Admin admin);

    @Delete
    Completable remove(Admin admin);

    @Update
    Completable update(Admin admin);

    // Teacher
    @Query("SELECT * FROM teachers WHERE facultyID = :facultyID ORDER BY surname")
    Single<List<Teacher>> getTeachers(int facultyID);

    @Query("SELECT * FROM teachers WHERE ID = :teacherID")
    Single<Teacher> getTeacher(long teacherID);

    @Query("SELECT EXISTS(SELECT * FROM teachers WHERE surname = :surname AND name = :name AND patronymic = :patronymic)")
    Single<Boolean> isTeacherExists(String surname, String name, String patronymic);

    @Insert
    Completable add(Teacher teacher);

    @Delete
    Completable remove(Teacher teacher);

    @Update
    Completable update(Teacher teacher);

    // TeacherLessonCrossRef
    @Transaction
    @Query("SELECT * FROM teachers WHERE ID = :teacherID")
    Single<TeacherWithLessons> getTeacherWithLessons(long teacherID);

    @Query("DELETE FROM teachers_lessons WHERE teacherID = :teacherID AND lessonID = :lessonID")
    Completable remove(long teacherID, int lessonID);

    @Insert
    Completable add(TeacherLessonCrossRef teacherLessonCrossRef);

    // Day
    @Insert
    void add(Day day);

    // LessonsTime
    @Insert
    void add(LessonsTime lessonsTime);

    // Schedule
    @Transaction
    @Query("SELECT * FROM schedule WHERE teacherID = :teacherID AND dayID = :dayID ORDER BY lessonNumber")
    Single<List<TeacherScheduleWithRelations>> getTeacherScheduleList(long teacherID, int dayID);

    @Transaction
    @Query("SELECT * FROM schedule WHERE groupID = :groupID AND dayID = :dayID ORDER BY lessonNumber")
    Single<List<StudentScheduleWithRelations>> getStudentScheduleList(long groupID, int dayID);

    @Query("SELECT COUNT(*) FROM schedule WHERE classroomID = :classroomID AND teacherID = :teacherID " +
            "AND lessonID = :lessonID AND lessonNumber = :lessonNumber AND dayID = :dayID")
    Single<Integer> getGroupCountWhereTeacherTeachesLesson(int classroomID, long teacherID,
                                                           int lessonID, int lessonNumber, int dayID);

    @Query("SELECT EXISTS(SELECT * FROM schedule WHERE" +
            " classroomID = :classroomID AND dayID = :dayID AND lessonNumber = :lessonNumber)")
    Single<Boolean> checkClassroomBusy(int classroomID, int dayID, int lessonNumber);

    @Query("SELECT EXISTS(SELECT * FROM schedule WHERE" +
            " groupID = :groupID AND dayID = :dayID AND lessonNumber = :lessonNumber)")
    Single<Boolean> checkGroupBusy(long groupID, int dayID, int lessonNumber);

    @Query("SELECT EXISTS(SELECT * FROM schedule WHERE" +
            " teacherID = :teacherID AND dayID = :dayID AND lessonNumber = :lessonNumber)")
    Single<Boolean> checkTeacherBusy(long teacherID, int dayID, int lessonNumber);

    @Insert
    Completable add(Schedule... schedule);

    @Delete
    Completable remove(Schedule schedule);

    @Update
    Completable update(Schedule... schedule);
}
