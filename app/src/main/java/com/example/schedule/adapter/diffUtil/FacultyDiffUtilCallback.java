package com.example.schedule.adapter.diffUtil;

import androidx.recyclerview.widget.DiffUtil;

import com.example.schedule.data.database.entity.Faculty;

import java.util.List;

public class FacultyDiffUtilCallback extends DiffUtil.Callback {

    private final List<Faculty> oldList;
    private final List<Faculty> newList;

    public FacultyDiffUtilCallback(List<Faculty> oldList, List<Faculty> newList) {
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
        Faculty oldProduct = oldList.get(oldItemPosition);
        Faculty newProduct = newList.get(newItemPosition);
        return oldProduct.getID() == newProduct.getID();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Faculty oldProduct = oldList.get(oldItemPosition);
        Faculty newProduct = newList.get(newItemPosition);
        return oldProduct.getFacultyName().equals(newProduct.getFacultyName());
    }
}
