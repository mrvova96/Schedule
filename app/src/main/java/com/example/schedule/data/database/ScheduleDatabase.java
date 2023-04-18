package com.example.schedule.data.database;

import android.app.Application;

import androidx.room.AutoMigration;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.schedule.data.database.dao.AdminDao;
import com.example.schedule.data.database.dao.LoginDao;
import com.example.schedule.data.database.dao.StudentDao;
import com.example.schedule.data.database.dao.TeacherDao;
import com.example.schedule.data.database.entity.LoginDetails;
import com.example.schedule.data.database.entity.Schedule;
import com.example.schedule.data.database.entity.Student;
import com.example.schedule.data.database.entity.Teacher;
import com.example.schedule.data.roles.ScheduleRole;

@Database(
        entities =
                {
                        LoginDetails.class,
                        Student.class,
                        Teacher.class,
                        Schedule.class
                },
        version = 2,
        exportSchema = false
//        autoMigrations = {
//                @AutoMigration(from = 1, to = 2)
//        }
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

    public abstract LoginDao loginDao();

    public abstract AdminDao adminDao();

    public abstract StudentDao studentDao();

    public abstract TeacherDao teacherDao();
}
