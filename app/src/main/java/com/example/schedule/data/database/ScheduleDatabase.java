package com.example.schedule.data.database;

import android.app.Application;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.schedule.data.database.dao.ScheduleDao;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

@Database(
        entities =
                {
                        MainAdmin.class,
                        Admin.class,
                        Classroom.class,
                        Day.class,
                        Faculty.class,
                        Group.class,
                        Lesson.class,
                        LessonsTime.class,
                        LoginDetails.class,
                        Schedule.class,
                        Student.class,
                        Teacher.class,
                        TeacherLessonCrossRef.class
                },
        version = 1,
        exportSchema = false
)
public abstract class ScheduleDatabase extends RoomDatabase {

    private static ScheduleDatabase instance;

    public static ScheduleDatabase getInstance(Application application) {
        if (instance == null) {
            synchronized (ScheduleDatabase.class) {
                instance = Room.databaseBuilder(
                                application,
                                ScheduleDatabase.class,
                                "schedule_database")
                        .fallbackToDestructiveMigration()
                        .build();
            }
        }
        return instance;
    }

    public abstract ScheduleDao scheduleDao();
}















