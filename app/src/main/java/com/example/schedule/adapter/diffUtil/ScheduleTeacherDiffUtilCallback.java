package com.example.schedule.adapter.diffUtil;

import androidx.recyclerview.widget.DiffUtil;

import com.example.schedule.data.database.entity.relation.TeacherScheduleWithRelations;

import java.util.List;

public class ScheduleTeacherDiffUtilCallback extends DiffUtil.Callback {

    private final List<TeacherScheduleWithRelations> oldList;
    private final List<TeacherScheduleWithRelations> newList;

    public ScheduleTeacherDiffUtilCallback(List<TeacherScheduleWithRelations> oldList, List<TeacherScheduleWithRelations> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        TeacherScheduleWithRelations oldProduct = oldList.get(oldItemPosition);
        TeacherScheduleWithRelations newProduct = newList.get(newItemPosition);
        return oldProduct.schedule.getID() == newProduct.schedule.getID();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        TeacherScheduleWithRelations oldProduct = oldList.get(oldItemPosition);
        TeacherScheduleWithRelations newProduct = newList.get(newItemPosition);
        return oldProduct.schedule.getGroupID() == newProduct.schedule.getGroupID()
                && oldProduct.schedule.getClassroomID() == newProduct.schedule.getClassroomID()
                && oldProduct.schedule.getDayID() == newProduct.schedule.getDayID()
                && oldProduct.schedule.getLessonNumber() == newProduct.schedule.getLessonNumber()
                && oldProduct.schedule.getLessonID() == newProduct.schedule.getLessonID();
    }
}
