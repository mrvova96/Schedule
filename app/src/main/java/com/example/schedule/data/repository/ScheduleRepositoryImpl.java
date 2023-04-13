package com.example.schedule.data.repository;

import com.example.schedule.ScheduleRepository;
import com.example.schedule.data.database.ScheduleDatabase;
import com.example.schedule.data.roles.ScheduleRole;
import com.example.schedule.data.roles.ScheduleRolesManager;

public class ScheduleRepositoryImpl implements ScheduleRepository {

    private ScheduleDatabase database;
    private ScheduleRolesManager rolesManager;

    public ScheduleRepositoryImpl(ScheduleDatabase database, ScheduleRolesManager rolesManager) {
        this.database = database;
        this.rolesManager = rolesManager;
    }

    @Override
    public boolean login(String login, String password) {
        boolean isUserExists = database.userExists();
        if (isUserExists) {
            rolesManager.setScheduleRole(ScheduleRole.STUDENT);
        }
        return isUserExists;
    }

    @Override
    public ScheduleRole getCurrentUserScheduleRole() {
        return rolesManager.getScheduleRole();
    }
}
