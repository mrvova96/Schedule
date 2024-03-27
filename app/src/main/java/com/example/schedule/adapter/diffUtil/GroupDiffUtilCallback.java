package com.example.schedule.adapter.diffUtil;

import androidx.recyclerview.widget.DiffUtil;

import com.example.schedule.data.database.entity.Group;

import java.util.List;

public class GroupDiffUtilCallback extends DiffUtil.Callback {

    private final List<Group> oldList;
    private final List<Group> newList;

    public GroupDiffUtilCallback(List<Group> oldList, List<Group> newList) {
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
        Group oldProduct = oldList.get(oldItemPosition);
        Group newProduct = newList.get(newItemPosition);
        return oldProduct.getID() == newProduct.getID();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Group oldProduct = oldList.get(oldItemPosition);
        Group newProduct = newList.get(newItemPosition);
        return oldProduct.getGroupName().equals(newProduct.getGroupName());
    }
}
