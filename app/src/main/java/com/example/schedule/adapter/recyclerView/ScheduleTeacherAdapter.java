package com.example.schedule.adapter.recyclerView;

import android.util.ArraySet;
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
import com.example.schedule.data.database.entity.Group;
import com.example.schedule.data.database.entity.relation.TeacherScheduleWithRelations;
import com.example.schedule.data.enums.UserType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class ScheduleTeacherAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<TeacherScheduleWithRelations> scheduleList = new ArrayList<>();
    private TreeSet<Integer> lessonPositions;
    private Map<Integer, List<TeacherScheduleWithRelations>> mapByPos = new HashMap<>();
    private UserType userType;

    public void setScheduleList(List<TeacherScheduleWithRelations> scheduleList, UserType userType) {
        List<TeacherScheduleWithRelations> list = new ArrayList<>(6);
        lessonPositions = new TreeSet<>();
        for (int i = 0; i < 6; i++)
            list.add(new TeacherScheduleWithRelations());
        for (TeacherScheduleWithRelations schedule : scheduleList) {
            lessonPositions.add(schedule.lessonsTime.getLessonNumber() - 1);
            list.set(schedule.lessonsTime.getLessonNumber() - 1, schedule);
        }
        for (int position : lessonPositions) {
            List<TeacherScheduleWithRelations> scheduleListByPos = new ArrayList<>();
            for (TeacherScheduleWithRelations schedule : scheduleList)
                if (schedule.lessonsTime.getLessonNumber() - 1 == position)
                    scheduleListByPos.add(schedule);
            mapByPos.put(position, scheduleListByPos);
        }
//        ScheduleTeacherDiffUtilCallback productDiffUtilCallback = new ScheduleTeacherDiffUtilCallback(this.scheduleList, list);
//        DiffUtil.DiffResult productDiffResult = DiffUtil.calculateDiff(productDiffUtilCallback);
        this.scheduleList = list;
        this.userType = userType;
//        productDiffResult.dispatchUpdatesTo(this);
        notifyDataSetChanged();
    }

    private OnAddClickListener onAddClickListener;
    private OnUpdateClickListener onUpdateClickListener;
    private OnDeleteClickListener onDeleteClickListener;

    public void setOnAddClickListener(ScheduleTeacherAdapter.OnAddClickListener onAddClickListener) {
        this.onAddClickListener = onAddClickListener;
    }

    public void setOnUpdateClickListener(ScheduleTeacherAdapter.OnUpdateClickListener onUpdateClickListener) {
        this.onUpdateClickListener = onUpdateClickListener;
    }

    public void setOnDeleteClickListener(ScheduleTeacherAdapter.OnDeleteClickListener onDeleteClickListener) {
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
        TeacherScheduleWithRelations schedule = scheduleList.get(position);

        if (schedule.schedule.getID() == 0) {
            ScheduleEmptyViewHolder holder0 = (ScheduleEmptyViewHolder) holder;
            holder0.textViewLessonNumber.setText(String.valueOf(position + 1).concat("."));
            if (!lessonPositions.isEmpty() && position < lessonPositions.last())
                holder0.textViewEmptyLesson.setText("Окно");
            else
                holder0.textViewEmptyLesson.setText("Нет пары");
            if (userType == UserType.TEACHER)
                holder0.imageButtonAdd.setVisibility(View.INVISIBLE);
            else
                holder0.imageButtonAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onAddClickListener.onAddClick(holder.getAdapterPosition());
                    }
                });

        } else {
            TeacherScheduleWithRelations schedule1 = mapByPos.get(position).get(0);
            TeacherScheduleWithRelations schedule2 = mapByPos.get(position).size() == 2
                    ? mapByPos.get(position).get(1) : null;

            ScheduleViewHolder holder1 = (ScheduleViewHolder) holder;
            holder1.textViewGroup.setVisibility(View.VISIBLE);
            holder1.textViewLessonNumber.setText(String.valueOf(schedule1.lessonsTime.getLessonNumber()).concat("."));
            holder1.textViewLessonName.setText(schedule1.lesson.getLessonName());
            holder1.textViewLessonTime.setText(schedule1.lessonsTime.getBeginningTime()
                    .concat("-").concat(schedule1.lessonsTime.getEndingTime()));
            holder1.textViewClassroomName.setText(String.valueOf(schedule1.classroom.getClassroomNumber()));
            if (schedule2 == null) {
                holder1.textViewGroup.setText("Группа");
                holder1.textViewGroupName.setText(schedule1.group.getGroupName());
            }
            else {
                holder1.textViewGroup.setText("Группы");
                holder1.textViewGroupName.setText(schedule1.group.getGroupName()
                        .concat(", ").concat(schedule2.group.getGroupName()));
            }
            if (schedule1.lesson.getLessonType().equals("Практика"))
                holder1.imageViewLineLesson.setBackgroundColor(
                        ContextCompat.getColor(holder1.imageViewLineLesson.getContext(), R.color.blue));
            else holder1.imageViewLineLesson.setBackgroundColor(
                    ContextCompat.getColor(holder1.imageViewLineLesson.getContext(), R.color.pink));
            if (position == lessonPositions.last())
                holder1.textViewBreak.setText("Окончание пар (".concat(schedule1.lessonsTime.getEndingTime()).concat(")"));
            else
                holder1.textViewBreak.setText("Перерыв 10 минут");
            if (userType == UserType.TEACHER) {
                holder1.imageButtonEdit.setVisibility(View.INVISIBLE);
                holder1.imageButtonDelete.setVisibility(View.INVISIBLE);
            } else {
                holder1.imageButtonEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onUpdateClickListener.onUpdateClick(schedule1, schedule2);
                    }
                });

                holder1.imageButtonDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onDeleteClickListener.onDeleteClick(schedule1, schedule2);
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
        private TextView textViewGroup;
        private TextView textViewGroupName;
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
            textViewGroup = itemView.findViewById(R.id.textViewGroup);
            textViewGroupName = itemView.findViewById(R.id.textViewGroupName);
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

        void onUpdateClick(TeacherScheduleWithRelations schedule1, TeacherScheduleWithRelations schedule2);
    }

    public interface OnDeleteClickListener {

        void onDeleteClick(TeacherScheduleWithRelations schedule1, TeacherScheduleWithRelations schedule2);
    }
}
