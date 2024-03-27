package com.example.schedule.data.database.entity;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "lessons_time",
        indices = {@Index(value = {"lessonNumber", "beginningTime", "endingTime"}, unique = true)}

)
public class LessonsTime {

    @PrimaryKey
    private int lessonNumber;
    private String beginningTime;
    private String endingTime;

    public LessonsTime(int lessonNumber, String beginningTime, String endingTime) {
        this.lessonNumber = lessonNumber;
        this.beginningTime = beginningTime;
        this.endingTime = endingTime;
    }

    public int getLessonNumber() {
        return lessonNumber;
    }

    public String getBeginningTime() {
        return beginningTime;
    }

    public String getEndingTime() {
        return endingTime;
    }
}
