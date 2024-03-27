package com.example.schedule.adapter.diffUtil;

import androidx.recyclerview.widget.DiffUtil;

import com.example.schedule.data.database.entity.relation.StudentScheduleWithRelations;
import com.example.schedule.data.database.entity.relation.TeacherScheduleWithRelations;

import java.util.List;

public class ScheduleStudentDiffUtilCallback extends DiffUtil.Callback {

    private final List<StudentScheduleWithRelations> oldList;
    private final List<StudentScheduleWithRelations> newList;

    public ScheduleStudentDiffUtilCallback(List<StudentScheduleWithRelations> oldList, List<StudentScheduleWithRelations> newList) {
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
        StudentScheduleWithRelations oldProduct = oldList.get(oldItemPosition);
        StudentScheduleWithRelations newProduct = newList.get(newItemPosition);
        return oldProduct.schedule.getID() == newProduct.schedule.getID();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        StudentScheduleWithRelations oldProduct = oldList.get(oldItemPosition);
        StudentScheduleWithRelations newProduct = newList.get(newItemPosition);
        return oldProduct.schedule.getTeacherID() == newProduct.schedule.getTeacherID()
                && oldProduct.schedule.getClassroomID() == newProduct.schedule.getClassroomID()
                && oldProduct.schedule.getDayID() == newProduct.schedule.getDayID()
                && oldProduct.schedule.getLessonNumber() == newProduct.schedule.getLessonNumber()
                && oldProduct.schedule.getLessonID() == newProduct.schedule.getLessonID();
    }
}
