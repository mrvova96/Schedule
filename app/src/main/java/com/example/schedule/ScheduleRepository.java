package com.example.schedule;

import com.example.schedule.data.roles.ScheduleRole;

public interface ScheduleRepository {

    boolean login(String login, String password);

    ScheduleRole getCurrentUserScheduleRole();
}
