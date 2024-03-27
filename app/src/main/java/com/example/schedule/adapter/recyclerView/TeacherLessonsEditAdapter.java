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
import com.example.schedule.adapter.diffUtil.LessonDiffUtilCallback;
import com.example.schedule.data.database.entity.Lesson;

import java.util.ArrayList;
import java.util.List;

public class TeacherLessonsEditAdapter extends RecyclerView.Adapter<TeacherLessonsEditAdapter.TeacherLessonsViewHolder> {

    private List<Lesson> lessons = new ArrayList<>();

    public void setLessons(List<Lesson> lessons) {
        LessonDiffUtilCallback productDiffUtilCallback = new LessonDiffUtilCallback(this.lessons, lessons);
        DiffUtil.DiffResult productDiffResult = DiffUtil.calculateDiff(productDiffUtilCallback);
        this.lessons = lessons;
        productDiffResult.dispatchUpdatesTo(this);
    }

    private OnDeleteClickListener onDeleteClickListener;

    public void setOnDeleteClickListener(TeacherLessonsEditAdapter.OnDeleteClickListener onDeleteClickListener) {
        this.onDeleteClickListener = onDeleteClickListener;
    }

    @NonNull
    @Override
    public TeacherLessonsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_edit, parent, false);
        return new TeacherLessonsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeacherLessonsViewHolder holder, int position) {
        Lesson lesson = lessons.get(position);
        holder.imageViewEdit.setVisibility(View.INVISIBLE);
        holder.textViewItem.setText(lesson.toString());
        holder.imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeleteClickListener.onDeleteClick(lesson);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lessons.size();
    }

    static class TeacherLessonsViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewItem;
        private ImageButton imageViewEdit;
        private ImageButton imageViewDelete;

        public TeacherLessonsViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewItem = itemView.findViewById(R.id.textViewItem);
            imageViewEdit = itemView.findViewById(R.id.imageViewEdit);
            imageViewDelete = itemView.findViewById(R.id.imageViewDelete);
        }
    }

    public interface OnDeleteClickListener {

        void onDeleteClick(Lesson lesson);
    }
}
