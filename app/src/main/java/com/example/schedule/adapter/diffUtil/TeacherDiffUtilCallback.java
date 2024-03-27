package com.example.schedule.adapter.diffUtil;

import androidx.recyclerview.widget.DiffUtil;

import com.example.schedule.data.database.entity.Faculty;
import com.example.schedule.data.database.entity.Student;
import com.example.schedule.data.database.entity.Teacher;

import java.util.List;

public class TeacherDiffUtilCallback extends DiffUtil.Callback {

    private final List<Teacher> oldList;
    private final List<Teacher> newList;

    public TeacherDiffUtilCallback(List<Teacher> oldList, List<Teacher> newList) {
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
        Teacher oldProduct = oldList.get(oldItemPosition);
        Teacher newProduct = newList.get(newItemPosition);
        return oldProduct.getID() == newProduct.getID();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Teacher oldProduct = oldList.get(oldItemPosition);
        Teacher newProduct = newList.get(newItemPosition);
        return oldProduct.getName().equals(newProduct.getName())
                && oldProduct.getSurname().equals(newProduct.getSurname())
                && oldProduct.getPatronymic().equals(newProduct.getPatronymic());
    }
}
