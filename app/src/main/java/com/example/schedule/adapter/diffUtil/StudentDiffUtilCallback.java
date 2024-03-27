package com.example.schedule.adapter.diffUtil;

import androidx.recyclerview.widget.DiffUtil;

import com.example.schedule.data.database.entity.Faculty;
import com.example.schedule.data.database.entity.Student;

import java.util.List;

public class StudentDiffUtilCallback extends DiffUtil.Callback {

    private final List<Student> oldList;
    private final List<Student> newList;

    public StudentDiffUtilCallback(List<Student> oldList, List<Student> newList) {
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
        Student oldProduct = oldList.get(oldItemPosition);
        Student newProduct = newList.get(newItemPosition);
        return oldProduct.getID() == newProduct.getID();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Student oldProduct = oldList.get(oldItemPosition);
        Student newProduct = newList.get(newItemPosition);
        return oldProduct.getName().equals(newProduct.getName())
                && oldProduct.getSurname().equals(newProduct.getSurname())
                && oldProduct.getPatronymic().equals(newProduct.getPatronymic());
    }
}
