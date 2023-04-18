package com.example.schedule.data.roles;

public class ScheduleRolesManager {

    private static ScheduleRolesManager instance;
    private ScheduleRole scheduleRole;

    public static ScheduleRolesManager getInstance() {
        if (instance == null) {
            instance = new ScheduleRolesManager();
        }
        return instance;
    }

    public ScheduleRole getScheduleRole() {
        return scheduleRole;
    }

    public void setScheduleRole(ScheduleRole scheduleRole) {
        this.scheduleRole = scheduleRole;
    }
}
