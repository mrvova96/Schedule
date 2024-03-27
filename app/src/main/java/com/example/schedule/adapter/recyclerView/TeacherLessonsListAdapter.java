package com.example.schedule.adapter.recyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schedule.R;
import com.example.schedule.adapter.diffUtil.LessonDiffUtilCallback;
import com.example.schedule.data.database.entity.Lesson;

import java.util.ArrayList;
import java.util.List;

public class TeacherLessonsListAdapter extends RecyclerView.Adapter<TeacherLessonsListAdapter.TeacherLessonsAddViewHolder> {

    private List<Lesson> lessons = new ArrayList<>();

    public void setLessons(List<Lesson> lessons) {
        LessonDiffUtilCallback productDiffUtilCallback = new LessonDiffUtilCallback(this.lessons, lessons);
        DiffUtil.DiffResult productDiffResult = DiffUtil.calculateDiff(productDiffUtilCallback);
        this.lessons = lessons;
        productDiffResult.dispatchUpdatesTo(this);
    }

    private OnLessonClickListener onLessonClickListener;

    public void setOnLessonClickListener(TeacherLessonsListAdapter.OnLessonClickListener onLessonClickListener) {
        this.onLessonClickListener = onLessonClickListener;
    }

    @NonNull
    @Override
    public TeacherLessonsAddViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_button, parent, false);
        return new TeacherLessonsAddViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeacherLessonsAddViewHolder holder, int position) {
        Lesson lesson = lessons.get(position);
        holder.buttonLesson.setText(lesson.getLessonName().concat(" (").concat(lesson.getLessonType()).concat(")"));
        holder.buttonLesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLessonClickListener.onLessonClick(lesson);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lessons.size();
    }

    static class TeacherLessonsAddViewHolder extends RecyclerView.ViewHolder {

        private Button buttonLesson;

        public TeacherLessonsAddViewHolder(@NonNull View itemView) {
            super(itemView);
            buttonLesson = itemView.findViewById(R.id.buttonItem);
        }
    }

    public interface OnLessonClickListener {

        void onLessonClick(Lesson lesson);
    }
}
