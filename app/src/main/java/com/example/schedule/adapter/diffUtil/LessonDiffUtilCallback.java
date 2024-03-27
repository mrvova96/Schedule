package com.example.schedule.adapter.diffUtil;

import androidx.recyclerview.widget.DiffUtil;

import com.example.schedule.data.database.entity.Group;
import com.example.schedule.data.database.entity.Lesson;

import java.util.List;

public class LessonDiffUtilCallback extends DiffUtil.Callback {

    private final List<Lesson> oldList;
    private final List<Lesson> newList;

    public LessonDiffUtilCallback(List<Lesson> oldList, List<Lesson> newList) {
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
        Lesson oldProduct = oldList.get(oldItemPosition);
        Lesson newProduct = newList.get(newItemPosition);
        return oldProduct.getID() == newProduct.getID();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Lesson oldProduct = oldList.get(oldItemPosition);
        Lesson newProduct = newList.get(newItemPosition);
        return oldProduct.getLessonName().equals(newProduct.getLessonName())
                && oldProduct.getLessonType().equals(newProduct.getLessonType());
    }
}
