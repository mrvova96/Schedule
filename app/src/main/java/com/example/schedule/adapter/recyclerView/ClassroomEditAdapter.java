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
import com.example.schedule.adapter.diffUtil.ClassroomDiffUtilCallback;
import com.example.schedule.data.database.entity.Classroom;

import java.util.ArrayList;
import java.util.List;

public class ClassroomEditAdapter extends RecyclerView.Adapter<ClassroomEditAdapter.ClassroomEditViewHolder> {

    private List<Classroom> classrooms = new ArrayList<>();

    public void setClassrooms(List<Classroom> classrooms) {
        ClassroomDiffUtilCallback productDiffUtilCallback = new ClassroomDiffUtilCallback(this.classrooms, classrooms);
        DiffUtil.DiffResult productDiffResult = DiffUtil.calculateDiff(productDiffUtilCallback);
        this.classrooms = classrooms;
        productDiffResult.dispatchUpdatesTo(this);
    }

    private OnUpdateClickListener onUpdateClickListener;
    private OnDeleteClickListener onDeleteClickListener;

    public void setOnUpdateClickListener(ClassroomEditAdapter.OnUpdateClickListener onUpdateClickListener) {
        this.onUpdateClickListener = onUpdateClickListener;
    }

    public void setOnDeleteClickListener(ClassroomEditAdapter.OnDeleteClickListener onDeleteClickListener) {
        this.onDeleteClickListener = onDeleteClickListener;
    }

    @NonNull
    @Override
    public ClassroomEditViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_edit, parent, false);
        return new ClassroomEditViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassroomEditViewHolder holder, int position) {
        Classroom classroom = classrooms.get(position);
        holder.textViewItem.setText(String.valueOf(classroom.getClassroomNumber()));
        holder.imageViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onUpdateClickListener.onUpdateClick(classroom);
            }
        });
        holder.imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeleteClickListener.onDeleteClick(classroom);
            }
        });
    }

    @Override
    public int getItemCount() {
        return classrooms.size();
    }

    class ClassroomEditViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewItem;
        private ImageButton imageViewEdit;
        private ImageButton imageViewDelete;

        public ClassroomEditViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewItem = itemView.findViewById(R.id.textViewItem);
            imageViewEdit = itemView.findViewById(R.id.imageViewEdit);
            imageViewDelete = itemView.findViewById(R.id.imageViewDelete);
        }
    }

    public interface OnUpdateClickListener {

        void onUpdateClick(Classroom classroom);
    }

    public interface OnDeleteClickListener {

        void onDeleteClick(Classroom classroom);
    }
}
