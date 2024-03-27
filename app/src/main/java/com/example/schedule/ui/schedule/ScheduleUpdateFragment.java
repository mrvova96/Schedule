package com.example.schedule.ui.schedule;

import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.schedule.R;
import com.example.schedule.data.database.entity.Classroom;
import com.example.schedule.data.database.entity.Group;
import com.example.schedule.data.database.entity.Lesson;
import com.example.schedule.data.database.entity.Schedule;
import com.example.schedule.data.database.entity.Teacher;
import com.example.schedule.databinding.FragmentMainBinding;
import com.example.schedule.databinding.FragmentScheduleUpdateBinding;
import com.example.schedule.presentation.schedule.ScheduleUpdateViewModel;
import com.example.schedule.ui.main.MainFragment;
import com.google.android.material.snackbar.Snackbar;

import java.io.Serializable;
import java.util.List;

public class ScheduleUpdateFragment extends Fragment {

    private static final String SCHEDULE_1 = "schedule1";
    private static final String SCHEDULE_2 = "schedule2";
    private static final String FACULTY_ID = "facultyID";
    private static final String GROUP_ID = "groupID";
    private static final String TEACHER_ID = "teacherID";

    private Schedule schedule1;
    private Schedule schedule2;
    private int facultyID;
    private long groupID;
    private long teacherID;
    private boolean isSetAdapter;

    private FragmentScheduleUpdateBinding binding;
    private FragmentMainBinding parentBinding;
    private ScheduleUpdateViewModel viewModel;
    private Snackbar snackbar;

    public static ScheduleUpdateFragment newInstance(Schedule schedule1, Schedule schedule2, int facultyID,
                                                     long groupID, long teacherID) {
        ScheduleUpdateFragment fragment = new ScheduleUpdateFragment();
        Bundle args = new Bundle();
        args.putSerializable(SCHEDULE_1, schedule1);
        args.putSerializable(SCHEDULE_2, schedule2);
        args.putInt(FACULTY_ID, facultyID);
        args.putLong(GROUP_ID, groupID);
        args.putLong(TEACHER_ID, teacherID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (groupID == 0) {
            binding.textViewGroup1.setVisibility(View.VISIBLE);
            binding.layoutSelectGroup1.setVisibility(View.VISIBLE);
            viewModel.refreshGroups(facultyID);
            viewModel.refreshLessons(teacherID);
        }
        if (teacherID == 0) {
            binding.checkBoxGroup2.setVisibility(View.GONE);
            binding.layoutSelectGroup2.setVisibility(View.GONE);
            binding.textViewTeacher.setVisibility(View.VISIBLE);
            binding.layoutSelectTeacher.setVisibility(View.VISIBLE);
            viewModel.refreshTeachers(facultyID);
        }
        viewModel.refreshClassrooms(facultyID);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentScheduleUpdateBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        viewModel = new ViewModelProvider(this).get(ScheduleUpdateViewModel.class);

        if (getParentFragment() != null)
            parentBinding = ((MainFragment) getParentFragment()).getBinding();

        if (getArguments() != null) {
            schedule1 = (Schedule) getArguments().getSerializable(SCHEDULE_1);
            schedule2 = (Schedule) getArguments().getSerializable(SCHEDULE_2);
            facultyID = getArguments().getInt(FACULTY_ID);
            groupID = getArguments().getLong(GROUP_ID);
            teacherID = getArguments().getLong(TEACHER_ID);
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setOnBackPressed();

        binding.checkBoxGroup2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.refreshGroups(facultyID);
            }
        });

        binding.spinnerSelectTeacher.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                long teacherID = ((Teacher) parent.getSelectedItem()).getID();
                viewModel.refreshLessons(teacherID);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        viewModel.getGroups().observe(getViewLifecycleOwner(), new Observer<List<Group>>() {
            @Override
            public void onChanged(List<Group> groups) {
                ArrayAdapter<Group> adapter = new ArrayAdapter<>(getContext(), R.layout.item_spinner, groups);
                adapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
                if (!isSetAdapter) {
                    isSetAdapter = true;
                    binding.spinnerSelectGroup1.setAdapter(adapter);
                    for (int i = 0; i < groups.size(); i++)
                        if (schedule1.getGroupID() == groups.get(i).getID()) {
                            binding.spinnerSelectGroup1.setSelection(i);
                            break;
                        }
                    if (schedule2 != null) {
                        binding.spinnerSelectGroup2.setAdapter(adapter);
                        binding.checkBoxGroup2.setChecked(true);
                        for (int i = 0; i < groups.size(); i++) {
                            if (schedule2.getGroupID() == groups.get(i).getID()) {
                                binding.spinnerSelectGroup2.setSelection(i);
                                break;
                            }
                        }
                    }
                } else {
                    if (binding.checkBoxGroup2.isChecked()) {
                        binding.spinnerSelectGroup2.setEnabled(true);
                        binding.spinnerSelectGroup2.getBackground().setColorFilter(getResources().getColor(R.color.black, null), PorterDuff.Mode.SRC_ATOP);
                        binding.spinnerSelectGroup2.setAdapter(adapter);
                    } else {
                        binding.spinnerSelectGroup2.setEnabled(false);
                        binding.spinnerSelectGroup2.getBackground().setColorFilter(getResources().getColor(android.R.color.transparent, null), PorterDuff.Mode.SRC_ATOP);
                        binding.spinnerSelectGroup2.setAdapter(null);
                    }
                }
            }
        });

        viewModel.getTeachers().observe(getViewLifecycleOwner(), new Observer<List<Teacher>>() {
            @Override
            public void onChanged(List<Teacher> teachers) {
                ArrayAdapter<Teacher> adapter = new ArrayAdapter<>(getContext(), R.layout.item_spinner, teachers);
                adapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
                binding.spinnerSelectTeacher.setAdapter(adapter);
                for (int i = 0; i < teachers.size(); i++)
                    if (schedule1.getTeacherID() == teachers.get(i).getID()) {
                        binding.spinnerSelectTeacher.setSelection(i);
                        break;
                    }
            }
        });

        viewModel.getLessons().observe(getViewLifecycleOwner(), new Observer<List<Lesson>>() {
            @Override
            public void onChanged(List<Lesson> lessons) {
                if (lessons.isEmpty()) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.item_spinner, new String[]{"Список пуст"});
                    binding.spinnerSelectLesson.setEnabled(false);
                    binding.spinnerSelectLesson.getBackground().setColorFilter(getResources().getColor(android.R.color.transparent, null), PorterDuff.Mode.SRC_ATOP);
                    binding.spinnerSelectLesson.setAdapter(adapter);
                } else {
                    ArrayAdapter<Lesson> adapter = new ArrayAdapter<>(getContext(), R.layout.item_spinner, lessons);
                    adapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
                    binding.spinnerSelectLesson.setEnabled(true);
                    binding.spinnerSelectLesson.getBackground().setColorFilter(getResources().getColor(R.color.black, null), PorterDuff.Mode.SRC_ATOP);
                    binding.spinnerSelectLesson.setAdapter(adapter);
                    for (int i = 0; i < lessons.size(); i++)
                        if (schedule1.getLessonID() == lessons.get(i).getID()) {
                            binding.spinnerSelectLesson.setSelection(i);
                            break;
                        }
                }
            }
        });

        viewModel.getClassrooms().observe(getViewLifecycleOwner(), new Observer<List<Classroom>>() {
            @Override
            public void onChanged(List<Classroom> classrooms) {
                ArrayAdapter<Classroom> adapter = new ArrayAdapter<>(getContext(), R.layout.item_spinner, classrooms);
                adapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
                binding.spinnerSelectClassroom.setAdapter(adapter);
                for (int i = 0; i < classrooms.size(); i++)
                    if (schedule1.getClassroomID() == classrooms.get(i).getID()) {
                        binding.spinnerSelectClassroom.setSelection(i);
                        break;
                    }
            }
        });

        viewModel.checkClassroomBusy().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean classroomIsBusy) {
                showMessage("Данная аудитория в это время занята");
            }
        });

        viewModel.checkGroupBusy().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer groupBusyCode) {
                switch (groupBusyCode) {
                    case 0:
                        showMessage("Данная группа в это время занята");
                        break;
                    case 1:
                        showMessage("Первая группа в это время занята");
                        break;
                    case 2:
                        showMessage("Вторая группа в это время занята");
                        break;
                }
            }
        });

        viewModel.checkTeacherBusy().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean teacherIsBusy) {
                showMessage("Данный преподаватель в это время занят");
            }
        });

        viewModel.isUpdated().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isUpdated) {
                if (isUpdated) {
                    showMessage("Запись успешно обновлена");
                    setUI();
                    getParentFragmentManager().popBackStack();
                } else
                    showMessage("Преподаватель в данной аудитории уже ведет предмет у двух групп");
            }
        });

        binding.buttonUpdateSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dataIsCorrect()) {
                    updateSchedule();
                }
            }
        });
    }

    private void updateSchedule() {
        int lessonID = ((Lesson) binding.spinnerSelectLesson.getSelectedItem()).getID();
        int classroomID = ((Classroom) binding.spinnerSelectClassroom.getSelectedItem()).getID();
        if (groupID == 0) {
            long groupID1 = ((Group) binding.spinnerSelectGroup1.getSelectedItem()).getID();
            Schedule scheduleForUpdate1 = new Schedule(schedule1.getID(), classroomID, schedule1.getTeacherID(),
                    groupID1, lessonID, schedule1.getLessonNumber(), schedule1.getDayID());
            Schedule scheduleForUpdate2 = null;
            if (binding.checkBoxGroup2.isChecked()) {
                long groupID2 = ((Group) binding.spinnerSelectGroup2.getSelectedItem()).getID();
                if (schedule2 != null)
                    scheduleForUpdate2 = new Schedule(schedule2.getID(), classroomID, schedule2.getTeacherID(),
                            groupID2, lessonID, schedule2.getLessonNumber(), schedule2.getDayID());
                else
                    scheduleForUpdate2 = new Schedule(classroomID, schedule1.getTeacherID(),
                            groupID2, lessonID, schedule1.getLessonNumber(), schedule1.getDayID());
            }
            viewModel.updateForTeacher(scheduleForUpdate1, scheduleForUpdate2, schedule1, schedule2);
        }
        if (teacherID == 0) {
            long teacherID = ((Teacher) binding.spinnerSelectTeacher.getSelectedItem()).getID();
            Schedule scheduleForUpdate = new Schedule(schedule1.getID(), classroomID, teacherID,
                    schedule1.getGroupID(), lessonID, schedule1.getLessonNumber(), schedule1.getDayID());
            viewModel.updateForStudent(schedule1, scheduleForUpdate);
        }
    }

    private boolean dataIsCorrect() {
        if (groupID == 0) {
            if (binding.checkBoxGroup2.isChecked() && binding.spinnerSelectGroup1.getSelectedItem().toString()
                    .equals(binding.spinnerSelectGroup2.getSelectedItem().toString())) {
                showMessage("Пожалуйста, выберите разные группы");
                return false;
            }
        }
        if (teacherID == 0)
            if (binding.spinnerSelectLesson.getSelectedItem().toString().equals("Список пуст")) {
                showMessage("Пожалуйста, заполните все поля");
                return false;
            }
        return true;
    }

    private void setOnBackPressed() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (snackbar != null)
                    snackbar.dismiss();
                setUI();
                getParentFragmentManager().popBackStack();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
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

    private void setUI() {
        parentBinding.textViewAdminPanel.setVisibility(View.INVISIBLE);
        parentBinding.tabLayout.setVisibility(View.VISIBLE);
        if (groupID == 0)
            parentBinding.textViewAdminPanel.setText("Выберите преподавателя");
        if (teacherID == 0)
            parentBinding.textViewAdminPanel.setText("Выберите группу");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        parentBinding = null;
    }
}