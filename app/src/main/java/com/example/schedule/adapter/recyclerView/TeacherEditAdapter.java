package com.example.schedule.adapter.recyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schedule.R;
import com.example.schedule.adapter.diffUtil.TeacherDiffUtilCallback;
import com.example.schedule.data.database.entity.Teacher;

import java.util.ArrayList;
import java.util.List;

public class TeacherEditAdapter extends RecyclerView.Adapter<TeacherEditAdapter.TeacherViewHolder> {

    private List<Teacher> teachers = new ArrayList<>();

    public void setTeachers(List<Teacher> teachers) {
        TeacherDiffUtilCallback productDiffUtilCallback = new TeacherDiffUtilCallback(this.teachers, teachers);
        DiffUtil.DiffResult productDiffResult = DiffUtil.calculateDiff(productDiffUtilCallback);
        this.teachers = teachers;
        productDiffResult.dispatchUpdatesTo(this);
    }

    private OnUpdateClickListener onUpdateClickListener;
    private OnDeleteClickListener onDeleteClickListener;

    public void setOnUpdateClickListener(TeacherEditAdapter.OnUpdateClickListener onUpdateClickListener) {
        this.onUpdateClickListener = onUpdateClickListener;
    }

    public void setOnDeleteClickListener(TeacherEditAdapter.OnDeleteClickListener onDeleteClickListener) {
        this.onDeleteClickListener = onDeleteClickListener;
    }

    @NonNull
    @Override
    public TeacherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_edit, parent, false);
        return new TeacherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeacherViewHolder holder, int position) {
        Teacher teacher = teachers.get(position);
        holder.textViewItem.setText(teacher.getSurname().concat(" ").concat(teacher.getName())
                .concat(" ").concat(teacher.getPatronymic()));
        holder.imageViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onUpdateClickListener.onUpdateClick(teacher);
            }
        });
        holder.imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeleteClickListener.onDeleteClick(teacher);
            }
        });
    }

    @Override
    public int getItemCount() {
        return teachers.size();
    }

    class TeacherViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewItem;
        private ImageButton imageViewEdit;
        private ImageButton imageViewDelete;

        public TeacherViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewItem = itemView.findViewById(R.id.textViewItem);
            imageViewEdit = itemView.findViewById(R.id.imageViewEdit);
            imageViewDelete = itemView.findViewById(R.id.imageViewDelete);
        }
    }

    public interface OnUpdateClickListener {

        void onUpdateClick(Teacher teacher);
    }

    public interface OnDeleteClickListener {

        void onDeleteClick(Teacher teacher);
    }
}
