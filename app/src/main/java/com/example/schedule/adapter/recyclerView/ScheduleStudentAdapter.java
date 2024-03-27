package com.example.schedule.adapter.recyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schedule.R;
import com.example.schedule.data.database.entity.relation.StudentScheduleWithRelations;
import com.example.schedule.data.enums.UserType;

import java.util.ArrayList;
import java.util.List;

public class ScheduleStudentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<StudentScheduleWithRelations> scheduleList = new ArrayList<>();
    private List<Integer> lessonPositions;
    private UserType userType;

    public void setScheduleList(List<StudentScheduleWithRelations> scheduleList, UserType userType) {
        List<StudentScheduleWithRelations> list = new ArrayList<>(6);
        lessonPositions = new ArrayList<>();
        for (int i = 0; i < 6; i++)
            list.add(new StudentScheduleWithRelations());
        for (StudentScheduleWithRelations schedule : scheduleList) {
            lessonPositions.add(schedule.lessonsTime.getLessonNumber() - 1);
            list.set(schedule.lessonsTime.getLessonNumber() - 1, schedule);
        }
//        ScheduleStudentDiffUtilCallback productDiffUtilCallback = new ScheduleStudentDiffUtilCallback(this.scheduleList, list);
//        DiffUtil.DiffResult productDiffResult = DiffUtil.calculateDiff(productDiffUtilCallback);
        this.scheduleList = list;
        this.userType = userType;
//        productDiffResult.dispatchUpdatesTo(this);
        notifyDataSetChanged();
    }

    private OnAddClickListener onAddClickListener;
    private OnUpdateClickListener onUpdateClickListener;
    private OnDeleteClickListener onDeleteClickListener;

    public void setOnAddClickListener(ScheduleStudentAdapter.OnAddClickListener onAddClickListener) {
        this.onAddClickListener = onAddClickListener;
    }

    public void setOnUpdateClickListener(ScheduleStudentAdapter.OnUpdateClickListener onUpdateClickListener) {
        this.onUpdateClickListener = onUpdateClickListener;
    }

    public void setOnDeleteClickListener(ScheduleStudentAdapter.OnDeleteClickListener onDeleteClickListener) {
        this.onDeleteClickListener = onDeleteClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        if (scheduleList.get(position).schedule.getID() == 0) {
            return 0;
        }
        return 1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 0) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedule_empty, parent, false);
            return new ScheduleEmptyViewHolder(view);
        }
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedule, parent, false);
        return new ScheduleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        StudentScheduleWithRelations schedule = scheduleList.get(position);

        if (schedule.schedule.getID() == 0) {
            ScheduleEmptyViewHolder holder0 = (ScheduleEmptyViewHolder) holder;
            holder0.textViewLessonNumber.setText(String.valueOf(position + 1).concat("."));
            if (!lessonPositions.isEmpty() && position < lessonPositions.get(lessonPositions.size() - 1))
                holder0.textViewEmptyLesson.setText("Окно");
            else
                holder0.textViewEmptyLesson.setText("Нет пары");
            if (userType == UserType.STUDENT)
                holder0.imageButtonAdd.setVisibility(View.INVISIBLE);
            else
                holder0.imageButtonAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onAddClickListener.onAddClick(holder.getAdapterPosition());
                    }
                });

        } else {
            ScheduleViewHolder holder1 = (ScheduleViewHolder) holder;
            holder1.textViewTeacher.setVisibility(View.VISIBLE);
            holder1.textViewLessonNumber.setText(String.valueOf(schedule.lessonsTime.getLessonNumber()).concat("."));
            holder1.textViewLessonName.setText(schedule.lesson.getLessonName());
            holder1.textViewLessonTime.setText(schedule.lessonsTime.getBeginningTime()
                    .concat("-").concat(schedule.lessonsTime.getEndingTime()));
            holder1.textViewClassroomName.setText(String.valueOf(schedule.classroom.getClassroomNumber()));
            holder1.textViewTeacherName.setText(schedule.teacher.toString());
            if (schedule.lesson.getLessonType().equals("Практика"))
                holder1.imageViewLineLesson.setBackgroundColor(
                        ContextCompat.getColor(holder1.imageViewLineLesson.getContext(), R.color.blue));
            else holder1.imageViewLineLesson.setBackgroundColor(
                    ContextCompat.getColor(holder1.imageViewLineLesson.getContext(), R.color.pink));
            if (position == lessonPositions.get(lessonPositions.size() - 1))
                holder1.textViewBreak.setText("Окончание пар (".concat(schedule.lessonsTime.getEndingTime()).concat(")"));
            else
                holder1.textViewBreak.setText("Перерыв 10 минут");
            if (userType == UserType.STUDENT) {
                holder1.imageButtonEdit.setVisibility(View.INVISIBLE);
                holder1.imageButtonDelete.setVisibility(View.INVISIBLE);
            } else {
                holder1.imageButtonEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onUpdateClickListener.onUpdateClick(schedule);
                    }
                });

                holder1.imageButtonDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onDeleteClickListener.onDeleteClick(schedule);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return scheduleList.size();
    }

    static class ScheduleEmptyViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewLessonNumber;
        private TextView textViewEmptyLesson;
        private ImageButton imageButtonAdd;

        public ScheduleEmptyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewLessonNumber = itemView.findViewById(R.id.textViewLessonNumber);
            textViewEmptyLesson = itemView.findViewById(R.id.textViewEmptyLesson);
            imageButtonAdd = itemView.findViewById(R.id.imageButtonAdd);
        }
    }

    static class ScheduleViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewLessonNumber;
        private TextView textViewLessonName;
        private TextView textViewLessonTime;
        private TextView textViewClassroomName;
        private TextView textViewTeacher;
        private TextView textViewTeacherName;
        private TextView textViewBreak;
        private ImageView imageViewLineLesson;
        private ImageButton imageButtonEdit;
        private ImageButton imageButtonDelete;

        public ScheduleViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewLessonNumber = itemView.findViewById(R.id.textViewLessonNumber);
            textViewLessonName = itemView.findViewById(R.id.textViewLessonName);
            textViewLessonTime = itemView.findViewById(R.id.textViewLessonTime);
            textViewClassroomName = itemView.findViewById(R.id.textViewClassroomName);
            textViewTeacher = itemView.findViewById(R.id.textViewTeacher);
            textViewTeacherName = itemView.findViewById(R.id.textViewTeacherName);
            textViewBreak = itemView.findViewById(R.id.textViewBreak);
            imageViewLineLesson = itemView.findViewById(R.id.imageViewLineLesson);
            imageButtonEdit = itemView.findViewById(R.id.imageButtonEdit);
            imageButtonDelete = itemView.findViewById(R.id.imageButtonDelete);
        }
    }

    public interface OnAddClickListener {

        void onAddClick(int position);
    }

    public interface OnUpdateClickListener {

        void onUpdateClick(StudentScheduleWithRelations schedule);
    }

    public interface OnDeleteClickListener {

        void onDeleteClick(StudentScheduleWithRelations schedule);
    }
}
