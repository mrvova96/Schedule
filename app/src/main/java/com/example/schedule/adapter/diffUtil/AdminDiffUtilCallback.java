package com.example.schedule.adapter.diffUtil;

import androidx.recyclerview.widget.DiffUtil;

import com.example.schedule.data.database.entity.Admin;
import com.example.schedule.data.database.entity.Faculty;
import com.example.schedule.data.database.entity.Student;

import java.util.List;

public class AdminDiffUtilCallback extends DiffUtil.Callback {

    private final List<Admin> oldList;
    private final List<Admin> newList;

    public AdminDiffUtilCallback(List<Admin> oldList, List<Admin> newList) {
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
        Admin oldProduct = oldList.get(oldItemPosition);
        Admin newProduct = newList.get(newItemPosition);
        return oldProduct.getID() == newProduct.getID();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Admin oldProduct = oldList.get(oldItemPosition);
        Admin newProduct = newList.get(newItemPosition);
        return oldProduct.getName().equals(newProduct.getName())
                && oldProduct.getSurname().equals(newProduct.getSurname())
                && oldProduct.getPatronymic().equals(newProduct.getPatronymic());
    }
}
