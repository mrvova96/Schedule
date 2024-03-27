package com.example.schedule.adapter.recyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schedule.R;
import com.example.schedule.adapter.diffUtil.TeacherDiffUtilCallback;
import com.example.schedule.data.database.entity.Teacher;

import java.util.ArrayList;
import java.util.List;

public class TeacherListAdapter extends RecyclerView.Adapter<TeacherListAdapter.TeacherListViewHolder> {

    private List<Teacher> teachers = new ArrayList<>();

    public void setTeachers(List<Teacher> teachers) {
        TeacherDiffUtilCallback productDiffUtilCallback = new TeacherDiffUtilCallback(this.teachers, teachers);
        DiffUtil.DiffResult productDiffResult = DiffUtil.calculateDiff(productDiffUtilCallback);
        this.teachers = teachers;
        productDiffResult.dispatchUpdatesTo(this);
    }

    private OnTeacherClickListener onTeacherClickListener;

    public void setOnTeacherClickListener(TeacherListAdapter.OnTeacherClickListener onTeacherClickListener) {
        this.onTeacherClickListener = onTeacherClickListener;
    }

    @NonNull
    @Override
    public TeacherListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_button, parent, false);
        return new TeacherListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeacherListViewHolder holder, int position) {
        Teacher teacher = teachers.get(position);
        holder.buttonTeacher.setText(teacher.getSurname().concat(" ")
                .concat(String.valueOf(teacher.getName().charAt(0))).concat(". ")
                .concat(String.valueOf(teacher.getPatronymic().charAt(0))).concat("."));
        holder.buttonTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTeacherClickListener.onTeacherClick(teacher);
            }
        });
    }

    @Override
    public int getItemCount() {
        return teachers.size();
    }

    class TeacherListViewHolder extends RecyclerView.ViewHolder {

        private Button buttonTeacher;

        public TeacherListViewHolder(@NonNull View itemView) {
            super(itemView);
            buttonTeacher = itemView.findViewById(R.id.buttonItem);
        }
    }

    public interface OnTeacherClickListener {

        void onTeacherClick(Teacher teacher);
    }
}
