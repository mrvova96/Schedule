package com.example.schedule.ui.schedule;

import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
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
import com.example.schedule.databinding.FragmentScheduleAddBinding;
import com.example.schedule.presentation.schedule.ScheduleAddViewModel;
import com.example.schedule.ui.main.MainFragment;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class ScheduleAddFragment extends Fragment {

    private static final String FACULTY_ID = "facultyID";
    private static final String GROUP_ID = "groupID";
    private static final String TEACHER_ID = "teacherID";
    private static final String DAY_ID = "dayID";
    private static final String LESSON_NUMBER = "lessonNumber";

    private int facultyID;
    private long groupID;
    private long teacherID;
    private int dayID;
    private int lessonNumber;
    private boolean isSetAdapter;

    private FragmentScheduleAddBinding binding;
    private FragmentMainBinding parentBinding;
    private ScheduleAddViewModel viewModel;
    private Snackbar snackbar;

    public static ScheduleAddFragment newInstance(int facultyID, long groupID, long teacherID,
                                                  int dayID, int lessonNumber) {
        ScheduleAddFragment fragment = new ScheduleAddFragment();
        Bundle args = new Bundle();
        args.putInt(FACULTY_ID, facultyID);
        args.putLong(GROUP_ID, groupID);
        args.putLong(TEACHER_ID, teacherID);
        args.putInt(DAY_ID, dayID);
        args.putInt(LESSON_NUMBER, lessonNumber);
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
        binding = FragmentScheduleAddBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        if (getParentFragment() != null)
            parentBinding = ((MainFragment) getParentFragment()).getBinding();

        viewModel = new ViewModelProvider(this).get(ScheduleAddViewModel.class);

        if (getArguments() != null) {
            facultyID = getArguments().getInt(FACULTY_ID);
            groupID = getArguments().getLong(GROUP_ID);
            teacherID = getArguments().getLong(TEACHER_ID);
            dayID = getArguments().getInt(DAY_ID);
            lessonNumber = getArguments().getInt(LESSON_NUMBER);
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
                if (!parent.getSelectedItem().toString().equals("Список пуст")) {
                    long teacherID = ((Teacher) parent.getSelectedItem()).getID();
                    viewModel.refreshLessons(teacherID);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        viewModel.getGroups().observe(getViewLifecycleOwner(), new Observer<List<Group>>() {
            @Override
            public void onChanged(List<Group> groups) {
                if (groups.isEmpty()) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.item_spinner, new String[]{"Список пуст"});
                    binding.spinnerSelectGroup1.setEnabled(false);
                    binding.spinnerSelectGroup1.setBackground(null);
                    binding.spinnerSelectGroup1.setAdapter(adapter);
                    binding.spinnerSelectGroup2.setEnabled(false);
                    binding.spinnerSelectGroup2.setBackground(null);
                } else {
                    ArrayAdapter<Group> adapter = new ArrayAdapter<>(getContext(), R.layout.item_spinner, groups);
                    adapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
                    if (!isSetAdapter) {
                        isSetAdapter = true;
                        binding.spinnerSelectGroup1.setAdapter(adapter);
                    }
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
                if (teachers.isEmpty()) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.item_spinner, new String[]{"Список пуст"});
                    binding.spinnerSelectTeacher.setEnabled(false);
                    binding.spinnerSelectLesson.setEnabled(false);
                    binding.spinnerSelectTeacher.setBackground(ContextCompat.getDrawable(requireContext(), android.R.color.transparent));
                    binding.spinnerSelectLesson.setBackground(ContextCompat.getDrawable(requireContext(), android.R.color.transparent));
                    binding.spinnerSelectTeacher.setAdapter(adapter);
                    binding.spinnerSelectLesson.setAdapter(adapter);
                } else {
                    ArrayAdapter<Teacher> adapter = new ArrayAdapter<>(getContext(), R.layout.item_spinner, teachers);
                    adapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
                    binding.spinnerSelectTeacher.setAdapter(adapter);
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
                }
            }
        });

        viewModel.getClassrooms().observe(getViewLifecycleOwner(), new Observer<List<Classroom>>() {
            @Override
            public void onChanged(List<Classroom> classrooms) {
                if (classrooms.isEmpty()) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.item_spinner, new String[]{"Список пуст"});
                    binding.spinnerSelectClassroom.setEnabled(false);
                    binding.spinnerSelectClassroom.setBackground(ContextCompat.getDrawable(requireContext(), android.R.color.transparent));
                    binding.spinnerSelectClassroom.setAdapter(adapter);
                } else {
                    ArrayAdapter<Classroom> adapter = new ArrayAdapter<>(getContext(), R.layout.item_spinner, classrooms);
                    adapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
                    binding.spinnerSelectClassroom.setAdapter(adapter);
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

        viewModel.isAdded().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isAdded) {
                if (isAdded) {
                    showMessage("Запись успешно добавлена");
                    setUI();
                    getParentFragmentManager().popBackStack();
                } else
                    showMessage("Преподаватель в данной аудитории уже ведет предмет у двух групп");
            }
        });

        binding.buttonAddSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dataIsCorrect()) {
                    saveSchedule();
                }
            }
        });
    }

    private void saveSchedule() {
        long groupID = this.groupID;
        long teacherID = this.teacherID;
        int lessonID = ((Lesson) binding.spinnerSelectLesson.getSelectedItem()).getID();
        int classroomID = ((Classroom) binding.spinnerSelectClassroom.getSelectedItem()).getID();
        if (groupID == 0) {
            long groupID1 = ((Group) binding.spinnerSelectGroup1.getSelectedItem()).getID();
            if (!binding.checkBoxGroup2.isChecked()) {
                viewModel.addForTeacher(new Schedule(classroomID, teacherID, groupID1, lessonID, lessonNumber, dayID),
                        null);
            } else {
                long groupID2 = ((Group) binding.spinnerSelectGroup2.getSelectedItem()).getID();
                viewModel.addForTeacher(new Schedule(classroomID, teacherID, groupID1, lessonID, lessonNumber, dayID),
                        new Schedule(classroomID, teacherID, groupID2, lessonID, lessonNumber, dayID));
            }
        }
        if (teacherID == 0) {
            teacherID = ((Teacher) binding.spinnerSelectTeacher.getSelectedItem()).getID();
            viewModel.addForStudent(new Schedule(classroomID, teacherID, groupID, lessonID, lessonNumber, dayID));
        }

    }

    private boolean dataIsCorrect() {
        if (groupID == 0) {
            if (binding.spinnerSelectGroup1.getSelectedItem().toString().equals("Список пуст")
                    || binding.spinnerSelectLesson.getSelectedItem().toString().equals("Список пуст")
                    || binding.spinnerSelectClassroom.getSelectedItem().toString().equals("Список пуст")) {
                showMessage("Пожалуйста, заполните все поля");
                return false;
            }
            if (binding.checkBoxGroup2.isChecked() && binding.spinnerSelectGroup1.getSelectedItem().toString()
                    .equals(binding.spinnerSelectGroup2.getSelectedItem().toString())) {
                showMessage("Пожалуйста, выберите разные группы");
                return false;
            }
        }
        if (teacherID == 0)
            if (binding.spinnerSelectTeacher.getSelectedItem().toString().equals("Список пуст")
                    || binding.spinnerSelectLesson.getSelectedItem().toString().equals("Список пуст")
                    || binding.spinnerSelectClassroom.getSelectedItem().toString().equals("Список пуст")) {
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
        TextView snackbarText = snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text);
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