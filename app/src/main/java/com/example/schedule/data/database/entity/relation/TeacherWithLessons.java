package com.example.schedule.data.database.entity.relation;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.example.schedule.data.database.entity.Lesson;
import com.example.schedule.data.database.entity.Teacher;
import com.example.schedule.data.database.entity.TeacherLessonCrossRef;

import java.util.List;

public class TeacherWithLessons {

    @Embedded
    public Teacher teacher;
    @Relation(
            parentColumn = "ID",
            entityColumn = "ID",
            associateBy = @Junction(
                    value = TeacherLessonCrossRef.class,
                    parentColumn = "teacherID",
                    entityColumn = "lessonID"
            )
    )
    public List<Lesson> lessons;
}
