package com.example.schedule.data.database.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(
        tableName = "teachers_lessons",
        primaryKeys = {"teacherID", "lessonID"},
        foreignKeys = {
                @ForeignKey(onDelete = 5, entity = Teacher.class, parentColumns = "ID", childColumns = "teacherID"),
                @ForeignKey(onDelete = 5, entity = Lesson.class, parentColumns = "ID", childColumns = "lessonID")
        }
)
public class TeacherLessonCrossRef {

    private long teacherID;
    private int lessonID;

    public TeacherLessonCrossRef(long teacherID, int lessonID) {
        this.teacherID = teacherID;
        this.lessonID = lessonID;
    }

    public long getTeacherID() {
        return teacherID;
    }

    public int getLessonID() {
        return lessonID;
    }
}
