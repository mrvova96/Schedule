package com.example.schedule.data.database.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(
        tableName = "schedule",
        indices = {
                @Index(value = {"teacherID", "dayID"}),
                @Index(value = {"groupID", "dayID"})},
        foreignKeys = {
                @ForeignKey(onDelete = 5, entity = Classroom.class, parentColumns = "ID", childColumns = "classroomID"),
                @ForeignKey(onDelete = 5, entity = Teacher.class, parentColumns = "ID", childColumns = "teacherID"),
                @ForeignKey(onDelete = 5, entity = Group.class, parentColumns = "ID", childColumns = "groupID"),
                @ForeignKey(onDelete = 5, entity = Lesson.class, parentColumns = "ID", childColumns = "lessonID"),
                @ForeignKey(onDelete = 5, entity = LessonsTime.class, parentColumns = "lessonNumber", childColumns = "lessonNumber"),
                @ForeignKey(onDelete = 5, entity = Day.class, parentColumns = "ID", childColumns = "dayID")
        }
)
public class Schedule implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int ID;
    private int classroomID;
    private long teacherID;
    private long groupID;
    private int lessonID;
    private int lessonNumber;
    private int dayID;

    public Schedule(int ID, int classroomID, long teacherID, long groupID, int lessonID, int lessonNumber, int dayID) {
        this.ID = ID;
        this.classroomID = classroomID;
        this.teacherID = teacherID;
        this.groupID = groupID;
        this.lessonID = lessonID;
        this.lessonNumber = lessonNumber;
        this.dayID = dayID;
    }

    @Ignore
    public Schedule(int classroomID, long teacherID, long groupID, int lessonID, int lessonNumber, int dayID) {
        this(0, classroomID, teacherID, groupID, lessonID, lessonNumber, dayID);
    }

    public int getID() {
        return ID;
    }

    public int getClassroomID() {
        return classroomID;
    }

    public long getTeacherID() {
        return teacherID;
    }

    public long getGroupID() {
        return groupID;
    }

    public int getLessonID() {
        return lessonID;
    }

    public int getLessonNumber() {
        return lessonNumber;
    }

    public int getDayID() {
        return dayID;
    }
}
