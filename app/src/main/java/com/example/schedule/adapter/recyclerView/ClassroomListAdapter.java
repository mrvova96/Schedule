package com.example.schedule.adapter.recyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schedule.R;
import com.example.schedule.adapter.diffUtil.ClassroomDiffUtilCallback;
import com.example.schedule.adapter.diffUtil.FacultyDiffUtilCallback;
import com.example.schedule.data.database.entity.Classroom;
import com.example.schedule.data.database.entity.Faculty;

import java.util.ArrayList;
import java.util.List;

public class ClassroomListAdapter extends RecyclerView.Adapter<ClassroomListAdapter.ClassroomListViewHolder> {

    private List<Classroom> classrooms = new ArrayList<>();

    public void setClassrooms(List<Classroom> classrooms) {
        ClassroomDiffUtilCallback productDiffUtilCallback = new ClassroomDiffUtilCallback(this.classrooms, classrooms);
        DiffUtil.DiffResult productDiffResult = DiffUtil.calculateDiff(productDiffUtilCallback);
        this.classrooms = classrooms;
        productDiffResult.dispatchUpdatesTo(this);
    }

    @NonNull
    @Override
    public ClassroomListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_button, parent, false);
        return new ClassroomListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassroomListViewHolder holder, int position) {
        Classroom classroom = classrooms.get(position);
        holder.buttonClassroom.setText(String.valueOf(classroom.getClassroomNumber()));
    }

    @Override
    public int getItemCount() {
        return classrooms.size();
    }

    static class ClassroomListViewHolder extends RecyclerView.ViewHolder {

        private final Button buttonClassroom;

        public ClassroomListViewHolder(@NonNull View itemView) {
            super(itemView);
            buttonClassroom = itemView.findViewById(R.id.buttonItem);
        }
    }
}
