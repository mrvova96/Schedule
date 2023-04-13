package com.example.schedule.data.roles;

public class ScheduleRolesManager {

    private static ScheduleRolesManager instance;
    private ScheduleRole scheduleRole;

    private ScheduleRolesManager() {

    }

    public static ScheduleRolesManager getInstance() {
        if (instance == null) {
            instance = new ScheduleRolesManager();
        }
        return instance;
    }

    public static void setInstance(ScheduleRolesManager instance) {
        ScheduleRolesManager.instance = instance;
    }

    public ScheduleRole getScheduleRole() {
        return scheduleRole;
    }

    public void setScheduleRole(ScheduleRole scheduleRole) {
        this.scheduleRole = scheduleRole;
    }
}
