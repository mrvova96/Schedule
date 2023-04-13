package com.example.schedule.data.database;

import com.example.schedule.data.roles.ScheduleRole;

public class ScheduleDatabase {

    public boolean userExists() {
        return true;
    }

    public ScheduleRole getCurrentUserScheduleRole() {
        return ScheduleRole.STUDENT;
    }
}
