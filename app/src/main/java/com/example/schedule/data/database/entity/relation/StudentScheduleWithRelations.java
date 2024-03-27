package com.example.schedule.data.database.entity.relation;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.schedule.data.database.entity.Classroom;
import com.example.schedule.data.database.entity.Group;
import com.example.schedule.data.database.entity.Lesson;
import com.example.schedule.data.database.entity.LessonsTime;
import com.example.schedule.data.database.entity.Schedule;
import com.example.schedule.data.database.entity.Teacher;

public class StudentScheduleWithRelations {

    @Embedded
    public Schedule schedule;
    @Relation(parentColumn = "classroomID", entityColumn = "ID", entity = Classroom.class)
    public Classroom classroom;
    @Relation(parentColumn = "teacherID", entityColumn = "ID", entity = Teacher.class)
    public Teacher teacher;
    @Relation(parentColumn = "lessonID", entityColumn = "ID", entity = Lesson.class)
    public Lesson lesson;
    @Relation(parentColumn = "lessonNumber", entityColumn = "lessonNumber", entity = LessonsTime.class)
    public LessonsTime lessonsTime;

    public StudentScheduleWithRelations() {
        this.schedule = new Schedule(0, 0, 0, 0, 0, 0, 0);
        this.classroom = new Classroom(0, 0, 0);
        this.teacher = new Teacher(0, "", "", "", 0);
        this.lesson = new Lesson(0, "", "", 0);
        this.lessonsTime = new LessonsTime(0, "", "");
    }
}
