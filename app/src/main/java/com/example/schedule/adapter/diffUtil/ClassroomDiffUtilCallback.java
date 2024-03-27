package com.example.schedule.adapter.diffUtil;

import androidx.recyclerview.widget.DiffUtil;

import com.example.schedule.data.database.entity.Classroom;
import com.example.schedule.data.database.entity.Group;

import java.util.List;

public class ClassroomDiffUtilCallback extends DiffUtil.Callback {

    private final List<Classroom> oldList;
    private final List<Classroom> newList;

    public ClassroomDiffUtilCallback(List<Classroom> oldList, List<Classroom> newList) {
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
        Classroom oldProduct = oldList.get(oldItemPosition);
        Classroom newProduct = newList.get(newItemPosition);
        return oldProduct.getID() == newProduct.getID();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Classroom oldProduct = oldList.get(oldItemPosition);
        Classroom newProduct = newList.get(newItemPosition);
        return oldProduct.getClassroomNumber() == (newProduct.getClassroomNumber());
    }
}
