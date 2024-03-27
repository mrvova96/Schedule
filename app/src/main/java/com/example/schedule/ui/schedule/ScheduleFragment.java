package com.example.schedule.ui.schedule;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.schedule.R;
import com.example.schedule.adapter.recyclerView.ScheduleStudentAdapter;
import com.example.schedule.adapter.recyclerView.ScheduleTeacherAdapter;
import com.example.schedule.data.database.entity.Schedule;
import com.example.schedule.data.database.entity.relation.StudentScheduleWithRelations;
import com.example.schedule.data.database.entity.relation.TeacherScheduleWithRelations;
import com.example.schedule.data.enums.UserType;
import com.example.schedule.databinding.FragmentMainBinding;
import com.example.schedule.databinding.FragmentScheduleBinding;
import com.example.schedule.presentation.schedule.ScheduleViewModel;
import com.example.schedule.ui.main.MainFragment;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class ScheduleFragment extends Fragment {

    private static final String USER_TYPE = "userType";
    private static final String FACULTY_ID = "facultyID";
    private static final String GROUP_ID = "groupID";
    private static final String TEACHER_ID = "teacherID";
    private static final String DAY_ID = "dayID";

    private UserType userType;
    private int facultyID;
    private long groupID;
    private long teacherID;
    private int dayID;

    private FragmentScheduleBinding binding;
    private FragmentMainBinding parentBinding;
    private ScheduleViewModel viewModel;
    private ScheduleTeacherAdapter scheduleTeacherAdapter;
    private ScheduleStudentAdapter scheduleStudentAdapter;
    private Snackbar snackbar;

    public static ScheduleFragment newInstance(UserType userType, int facultyID, long groupID, long teacherID, int dayID) {
        ScheduleFragment fragment = new ScheduleFragment();
        Bundle args = new Bundle();
        args.putSerializable(USER_TYPE, userType);
        args.putInt(FACULTY_ID, facultyID);
        args.putLong(GROUP_ID, groupID);
        args.putLong(TEACHER_ID, teacherID);
        args.putInt(DAY_ID, dayID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (groupID == 0)
            viewModel.refreshTeacherScheduleList(teacherID, dayID);
        else if (teacherID == 0)
            viewModel.refreshStudentScheduleList(groupID, dayID);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentScheduleBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        viewModel = new ViewModelProvider(this).get(ScheduleViewModel.class);

        if (getParentFragment() != null)
            parentBinding = ((MainFragment) getParentFragment().getParentFragment()).getBinding();

        if (getArguments() != null) {
            userType = (UserType) getArguments().getSerializable(USER_TYPE);
            facultyID = getArguments().getInt(FACULTY_ID);
            groupID = getArguments().getLong(GROUP_ID);
            teacherID = getArguments().getLong(TEACHER_ID);
            dayID = getArguments().getInt(DAY_ID);
        }

        initAdapter();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel.getTeacherScheduleList().observe(getViewLifecycleOwner(), new Observer<List<TeacherScheduleWithRelations>>() {
            @Override
            public void onChanged(List<TeacherScheduleWithRelations> scheduleList) {
                scheduleTeacherAdapter.setScheduleList(scheduleList, userType);
            }
        });

        viewModel.getStudentScheduleList().observe(getViewLifecycleOwner(), new Observer<List<StudentScheduleWithRelations>>() {
            @Override
            public void onChanged(List<StudentScheduleWithRelations> scheduleList) {
                scheduleStudentAdapter.setScheduleList(scheduleList, userType);
            }
        });

        scheduleTeacherAdapter.setOnAddClickListener(new ScheduleTeacherAdapter.OnAddClickListener() {
            @Override
            public void onAddClick(int position) {
                int lessonNumber = position + 1;
                setUI("Добавление записи");
                launchNextFragment(ScheduleAddFragment.newInstance(
                        facultyID, groupID, teacherID, dayID, lessonNumber));
            }
        });

        scheduleTeacherAdapter.setOnUpdateClickListener(new ScheduleTeacherAdapter.OnUpdateClickListener() {
            @Override
            public void onUpdateClick(TeacherScheduleWithRelations schedule1, TeacherScheduleWithRelations schedule2) {
                setUI("Обновление записи");
                if (schedule2 != null)
                    launchNextFragment(ScheduleUpdateFragment.newInstance(
                            new Schedule(schedule1.schedule.getID(), schedule1.classroom.getID(),
                                    schedule1.schedule.getTeacherID(), schedule1.group.getID(),
                                    schedule1.lesson.getID(), schedule1.lessonsTime.getLessonNumber(), dayID),
                            new Schedule(schedule2.schedule.getID(), schedule2.classroom.getID(),
                                    schedule2.schedule.getTeacherID(), schedule2.group.getID(),
                                    schedule2.lesson.getID(), schedule2.lessonsTime.getLessonNumber(), dayID),
                            facultyID, groupID, teacherID));
                else
                    launchNextFragment(ScheduleUpdateFragment.newInstance(
                            new Schedule(schedule1.schedule.getID(), schedule1.classroom.getID(),
                                    schedule1.schedule.getTeacherID(), schedule1.group.getID(),
                                    schedule1.lesson.getID(), schedule1.lessonsTime.getLessonNumber(), dayID),
                            null, facultyID, groupID, teacherID));
            }
        });

        scheduleTeacherAdapter.setOnDeleteClickListener(new ScheduleTeacherAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(TeacherScheduleWithRelations schedule1, TeacherScheduleWithRelations schedule2) {
                new AlertDialog.Builder(requireContext())
                        .setMessage("Удалить данную запись?")
                        .setPositiveButton("Удалить", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                viewModel.remove(new Schedule(
                                                schedule1.schedule.getID(), schedule1.classroom.getID(),
                                                schedule1.schedule.getTeacherID(), schedule1.group.getID(),
                                                schedule1.lesson.getID(), schedule1.lessonsTime.getLessonNumber(),
                                                dayID),
                                        groupID, teacherID);
                                if (schedule2 != null) {
                                    viewModel.remove(new Schedule(
                                                    schedule2.schedule.getID(), schedule2.classroom.getID(),
                                                    schedule2.schedule.getTeacherID(), schedule2.group.getID(),
                                                    schedule2.lesson.getID(), schedule2.lessonsTime.getLessonNumber(),
                                                    dayID),
                                            groupID, teacherID);
                                }
                                showMessage("Запись успешно удалена");
                            }
                        })
                        .setNegativeButton("Назад", null)
                        .show();
            }
        });

        scheduleStudentAdapter.setOnAddClickListener(new ScheduleStudentAdapter.OnAddClickListener() {
            @Override
            public void onAddClick(int position) {
                int lessonNumber = position + 1;
                setUI("Добавление записи");
                launchNextFragment(ScheduleAddFragment.newInstance(
                        facultyID, groupID, teacherID, dayID, lessonNumber));
            }
        });

        scheduleStudentAdapter.setOnUpdateClickListener(new ScheduleStudentAdapter.OnUpdateClickListener() {
            @Override
            public void onUpdateClick(StudentScheduleWithRelations schedule) {
                setUI("Обновление записи");
                launchNextFragment(ScheduleUpdateFragment.newInstance(new Schedule(
                                schedule.schedule.getID(), schedule.classroom.getID(),
                                schedule.teacher.getID(), schedule.schedule.getGroupID(),
                                schedule.lesson.getID(), schedule.lessonsTime.getLessonNumber(),
                                dayID),
                        null, facultyID, groupID, teacherID));
            }
        });

        scheduleStudentAdapter.setOnDeleteClickListener(new ScheduleStudentAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(StudentScheduleWithRelations schedule) {
                new AlertDialog.Builder(requireContext())
                        .setMessage("Удалить данную запись?")
                        .setPositiveButton("Удалить", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                viewModel.remove(new Schedule(
                                                schedule.schedule.getID(), schedule.classroom.getID(),
                                                schedule.schedule.getTeacherID(), schedule.schedule.getGroupID(),
                                                schedule.schedule.getLessonID(), schedule.lessonsTime.getLessonNumber(),
                                                dayID),
                                        groupID, teacherID);
                                showMessage("Запись успешно удалена");
                            }
                        })
                        .setNegativeButton("Назад", null)
                        .show();
            }
        });
    }

    private void initAdapter() {
        scheduleTeacherAdapter = new ScheduleTeacherAdapter();
        scheduleStudentAdapter = new ScheduleStudentAdapter();
        if (groupID == 0) {
            binding.recyclerViewSchedule.setAdapter(scheduleTeacherAdapter);
        } else if (teacherID == 0) {
            binding.recyclerViewSchedule.setAdapter(scheduleStudentAdapter);
        }
        binding.recyclerViewSchedule.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void launchNextFragment(Fragment fragment) {
        getParentFragment().getParentFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
                .replace(R.id.fragmentContainerAdmin, fragment)
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit();
    }

    private void showMessage(String message) {
        snackbar = Snackbar.make(requireView(), message, 3000);
        snackbar.setBackgroundTint(getResources().getColor(R.color.pink, null));
        snackbar.setActionTextColor(getResources().getColor(R.color.black, null));
        TextView snackbarText = snackbar.getView()
                .findViewById(com.google.android.material.R.id.snackbar_text);
        snackbarText.setTextSize(16);
        snackbarText.setTextColor(getResources().getColor(R.color.black, null));
        snackbar.setAction(R.string.snackbar_action, v -> snackbar.dismiss());
        snackbar.show();
    }

    private void setUI(String text) {
        parentBinding.textViewAdminPanel.setVisibility(View.VISIBLE);
        parentBinding.textViewAdminPanel.setText(text);
        parentBinding.tabLayout.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}